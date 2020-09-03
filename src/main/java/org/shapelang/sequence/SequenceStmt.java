package org.shapelang.sequence;

import org.jetbrains.annotations.NotNull;
import org.shapelang.common.Twople;
import org.shapelang.common.parsercom.*;
import org.shapelang.shapes.Shape;

import java.util.*;

public class SequenceStmt {
    public static Queue<Action> sequence(CanvasInit ci) {
        final Text head = ci.next;
        final Collection<Queue<Action>> sequenceQueues = sequenceAll(head,Optional.empty());
        return mergeSequences(sequenceQueues);
    }

    private static Collection<Queue<Action>> sequenceAll(Text head, Optional<Map<Shape,Queue<Action>>> mapbe) {
        Optional<Text> curMaybe = Optional.of(head);

        Map<Shape,Queue<Action>> qs = null; // reassigned every time
        // NOT NULL FOR LONG
        qs = mapbe.orElseGet(SequenceStmt::getConcMap);
        // viewed as immutable

        while(curMaybe.isPresent()) {
            final Text cur = curMaybe.get();
            qs = addStmt(qs,cur.stmt);
            curMaybe = cur.getNext();
        }

        return qs.values();
    }

    // just has to
    private static Map<Shape,Queue<Action>> addStmt(Map<Shape,Queue<Action>> map, StmtType stmt) {
        switch(stmt.stmtType()) {
            case PUT:
                return putStmt(map,(Put) stmt);
            case BLOCK:
                return blockStmt(map, (Block) stmt);
            case MOVE:
                return moveStmt(map, (Move) stmt);
            case SEQ:
                return seqStmt(map, (Sequential) stmt);
            case LOOP:
                return loopStmt(map, (Loop) stmt);
            case RESIZE:
                return resizeStmt(map, (Resize) stmt);
            case OTHER:
                System.out.println("that isn't supported yet");
                return map;
            default:
                System.out.println("oops");
                return map;
        }
    }

    private static Map<Shape,Queue<Action>> resizeStmt(Map<Shape,Queue<Action>> map, Resize rsz) {
        final Queue<Action> q = map.get(rsz.shapeRef);
        q.add(new Action(rsz,rsz.time()));
        return map;
    }

    private static Map<Shape,Queue<Action>> loopStmt(Map<Shape,Queue<Action>> map, Loop loop) {
        // TODO - does this require a 'generator' of some kind to deal with infinite loops
        final Shape[] shapes = loop.shapes;
        final Queue<Action>[] qs = new Queue[shapes.length];

        /* general plan:
         * remove all queues pertaining to the shapes in the loop
         * do the loop ones via a semi recursive call - can't semi recurse directly, no
         * insert the original q and the loop queue into queue loop
         */


        for(int i = 0; i < shapes.length; i++) {
            final Shape shape = shapes[i];
            final Put def = new Put(shape,new Twople<>(0,0));

            qs[i] = map.remove(shape);
            final Queue<Action> defQ = getConcQ();
            defQ.add(new Action(def,0));

            map.put(shape,defQ);
        }

        sequenceAll(loop.contents,Optional.of(map));
        // i dislike this, but it will modify state

        for(int i = 0; i < shapes.length; i++) {
            final Shape shape = shapes[i];
            final Queue<Action> prevQ = qs[i];

            final Queue<Action> loopQ = map.remove(shape);

            map.put(shape,new QueueLoop(prevQ,loopQ,loop.numIter.orElse(0)));
        }

        return map;
    }


    // TODO - could this be written generically
    private static class QueueLoop implements Queue<Action> {
        private final Queue<Action> prev;
        private final Queue<Action> after = getConcQ();
        private final Action[] orig;
        private Queue<Action> loop = null;
        private final boolean isInfinite;
        private int numIter;

        public QueueLoop(Queue<Action> prev, Queue<Action> loop, int numIter) {
            this.prev = prev;
            this.loop = loop;
            orig = loop.toArray(new Action[0]);
            isInfinite = numIter <= 0;
            this.numIter = numIter;
        }

        private void refresh() {
            if(loop.isEmpty() && (isInfinite || numIter > 0)) {
                loop = (Queue<Action>) Arrays.asList(orig.clone());
                numIter--;
            }
        }

        @Override
        public int size() {
            int factor = numIter;
            if(factor < 1)
                factor = 1;
            return prev.size() + (loop.size() * factor) + after.size();
        }

        @Override
        public boolean isEmpty() {
            return prev.isEmpty() && loop.isEmpty() && after.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return prev.contains(o) || loop.contains(o) || after.contains(o);
        }

        /*
         * THIS METHOD DOES NOT WORK CORRECTLY FOR INFINITE LISTS DUE TO EAGER EVAL
         */
        @NotNull
        @Override
        public Iterator<Action> iterator() {
            return after.iterator(); // TODO - fix
        }

        @NotNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NotNull
        @Override
        public <T> T[] toArray(@NotNull T[] a) {
            return null;
        }

        @Override
        public boolean add(Action action) {
            return after.add(action);
        }

        @Override
        public boolean remove(Object o) {
            boolean isRemoved = prev.remove(o);
            if(!isRemoved)
                isRemoved = loop.remove(o);
            if(!isRemoved)
                isRemoved = after.remove(o);
            return isRemoved;
        }

        @Override
        public boolean containsAll(@NotNull Collection<?> c) {
            final Iterator<?> it = c.iterator();
            boolean cont = true;
            while(it.hasNext() && cont) {
                final Object cur = it.next();
                cont = prev.contains(cur) || loop.contains(cur) || after.contains(cur);
            }

            return cont;
        }

        @Override
        public boolean addAll(@NotNull Collection<? extends Action> c) {
            return after.addAll(c);
        }

        @Override
        public boolean removeAll(@NotNull Collection<?> c) {
            boolean allRem = true;
            for (Object cur : c) {
                boolean curRem = prev.remove(cur);
                if(!curRem)
                    curRem = loop.remove(cur);
                if(!curRem)
                    curRem = after.remove(cur);
                allRem = allRem && curRem;
            }

            return allRem;
        }

        @Override
        public boolean retainAll(@NotNull Collection<?> c) {
            // good enough for req functionality - not a great implementation
            return prev.retainAll(c) || loop.retainAll(c) || after.retainAll(c);
        }

        @Override
        public void clear() {
            prev.clear();
            loop.clear();
            after.clear();
        }

        @Override
        public boolean offer(Action action) {
            return after.offer(action);
        }

        @Override
        public Action remove() {
            refresh();
            if(!prev.isEmpty())
                return prev.remove();
            else if(!loop.isEmpty())
                return loop.remove();
            else
                return after.remove();
        }

        @Override
        public Action poll() {
            refresh();
            if(!prev.isEmpty())
                return prev.remove();
            else if(!loop.isEmpty())
                return loop.remove();
            else
                return after.poll();
        }

        @Override
        public Action element() {
            refresh();
            if(!prev.isEmpty())
                return prev.element();
            else if(!loop.isEmpty())
                return loop.element();
            else
                return after.element();
        }

        @Override
        public Action peek() {
            refresh();
            if(!prev.isEmpty())
                return prev.peek();
            else if(!loop.isEmpty())
                return loop.peek();
            else
                return after.peek();
        }
    }

    private static Map<Shape,Queue<Action>> seqStmt(Map<Shape,Queue<Action>> map, Sequential seq) {
        int currentTime = 0;
        final Text head = seq.text;
        Optional<Text> curMaybe = Optional.of(head);

        while(curMaybe.isPresent()) {
            final Text cur = curMaybe.get();

            final StmtType origStmt = cur.stmt;

            // MutTime has an additional time field that can 'offset' the previous time
            // Basically a wrapper around a StmtType that can be used to set a different time
            final MutTime stmt = new MutTime(origStmt,currentTime);

            map = addStmt(map,stmt);

            currentTime += origStmt.time();
            curMaybe = cur.getNext();
        }

        return map;
    }

    private static Map<Shape,Queue<Action>> moveStmt(Map<Shape,Queue<Action>> map, Move move) {
        final Queue<Action> q = map.get(move.shapeRef);
        q.add(new Action(move,move.time()));
        return map;
    }

    private static Map<Shape,Queue<Action>> blockStmt(Map<Shape,Queue<Action>> map, Block block) {
        final Shape[] shapes = block.shapes;

        for(Shape shape: shapes) {
            final Queue<Action> q = map.get(shape);
            q.add(new Action(block,block.time()));
            map.put(shape,q);
        }

        return map;
    }

    private static Map<Shape,Queue<Action>> putStmt(Map<Shape,Queue<Action>> map, Put put) {
        final Queue<Action> q = getConcQ();
        q.add(new Action(put,put.time()));
        map.put(put.shapeRef,q);
        return map;
    }

    private static<K,V> Map<K,V> getConcMap() {
        return new HashMap<>();
    }

    private static<U> Queue<U> getConcQ() {
        return new LinkedList<>();
    }


    private static Queue<Action> mergeSequences(Collection<Queue<Action>> seqs) {
        final Queue<Action>[] qs = (Queue<Action>[]) seqs.toArray();
        final Iterator<Twople<Integer,Integer>> lrs = genSplits(qs.length);

        while(lrs.hasNext()) {
            final Twople<Integer,Integer> lr = lrs.next();
            final int l = lr.fst;
            final int r = lr.snd;
            qs[l] = mergeQs(qs[l],qs[r]);
        }

        return qs[0];
    }

    // TODO - ensure correct
    // TODO - will this work for infinite queues (those made by QueueLoop)?
    private static Iterator<Twople<Integer,Integer>> genSplits(int max) {
        int size = 1;
        final Queue<Twople<Integer,Integer>> q = getConcQ();
        while(size < max) {
            int l = 0;
            int r = l+size;
            final int incr = size * 2;

            while(r < max) {
                q.add(new Twople<>(l,r));
                l += incr;
                r += incr;
            }

            size *= 2;
        }

        return q.iterator();
    }

    // will interleave two queues based on time
    private static Queue<Action> mergeQs(Queue<Action> x, Queue<Action> y) {
        boolean isFin = x.isEmpty() || y.isEmpty();
        Action xCur = x.remove();
        Action yCur = y.remove();
        int xTime = 0;
        int yTime = 0;
        boolean xPriority = true;

        final Queue<Action> merge = getConcQ();

        while(!isFin) {
            if(xTime == yTime) {
                final Action toQ;
                if(xPriority) {
                    toQ = xCur;
                    xCur = x.remove();
                    xTime += xCur.time();
                }
                else {
                    toQ = y.remove();
                    yCur = y.remove();
                    yTime += yCur.time();
                }
                xPriority = !xPriority;
                merge.add(toQ);
            }

            else if(xTime > yTime) {
                merge.add(yCur);
                yCur = y.remove();
                yTime += yCur.time();
            }

            else {
                merge.add(xCur);
                xCur = x.remove();
                xTime += xCur.time();
            }

            isFin = x.isEmpty() || y.isEmpty();
        }

        while(!x.isEmpty())
            merge.add(x.remove());
        while(!y.isEmpty())
            merge.add(y.remove());


        return merge;
    }

    private static class MutTime implements StmtType {
        private final int timeOffset;
        private final StmtType stmt;

        public MutTime(StmtType stmt, int timeOffset) {
            this.stmt = stmt;
            this.timeOffset = timeOffset;
        }

        @Override
        public ParserToken stmtType() {
            return stmt.stmtType();
        }

        @Override
        public int time() {
            return stmt.time() + timeOffset;
        }
    }
}
