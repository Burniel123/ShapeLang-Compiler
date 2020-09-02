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
            default:
                System.out.println("oops");
                return null;
                break;
        }

        return null;
    }

    private static Map<Shape,Queue<Action>> blockStmt(Map<Shape,Queue<Action>> map, Block block) {
        final Shape[] shapes = block.shapes;
        Map<Shape,Queue<Action>> cur = map;

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

    private static Queue<Action> mergeSequences(List<Queue<Action>> seqs) {
        // TODO - implement
        return null;
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