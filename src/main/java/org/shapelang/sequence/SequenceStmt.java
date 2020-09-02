package org.shapelang.sequence;

import org.shapelang.common.Twople;
import org.shapelang.common.parsercom.*;
import org.shapelang.shapes.Shape;

import java.util.*;

public class SequenceStmt {
    public static Queue<Action> sequence(CanvasInit ci) {
        final Text head = ci.next;
        final Collection<Queue<Action>> sequenceQueues = sequenceAll(head);
        return mergeSequences(sequenceQueues);
    }

    private static Collection<Queue<Action>> sequenceAll(Text head) {
        Optional<Text> curMaybe = Optional.of(head);
        Map<Shape,Queue<Action>> qs = getConcMap(); // reassigned every time
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
        // I think it does
        // Oh how the scala lazy would help here
        return map;
    }

    private static Map<Shape,Queue<Action>> seqStmt(Map<Shape,Queue<Action>> map, Sequential seq) {
        int currentTime = 0;
        final Text head = seq.text;
        Optional<Text> curMaybe = Optional.of(head);

        while(curMaybe.isPresent()) {
            final Text cur = curMaybe.get();

            map = addStmt(map,cur.stmt);
            // TODO - fix sequential timing
            // currently none is implemented: might need to make the time field mutable

            currentTime += cur.stmt.time();
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
        final Map<Shape,Queue<Action>> cur = map;

        for(Shape shape: shapes) {
            final Queue<Action> q = cur.get(shape);
            q.add(new Action(block,block.time()));
            cur.put(shape,q);
        }

        return cur;
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

    // will interleave two queues based off time
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

        return merge;
    }
}


public class Action {
    final private Twople<StmtType,Integer> act;

    public Action(StmtType stmt, int time) {
        act = new Twople<>(stmt,time);
    }

    public StmtType stmt() {
        return act.fst;
    }

    public int time() {
        return act.snd;
    }
}