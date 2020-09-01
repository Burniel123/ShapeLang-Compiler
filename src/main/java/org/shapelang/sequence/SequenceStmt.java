package org.shapelang.sequence;

import org.shapelang.common.Twople;
import org.shapelang.common.parsercom.*;
import org.shapelang.shapes.Shape;

import java.util.*;

public class SequenceStmt {
    public static Queue<Action> sequence(CanvasInit ci) {
        final Text head = ci.next;
        final List<Queue<Action>> sequenceQueues = sequenceAll(head);
        return mergeSequences(sequenceQueues);
    }

    private static List<Queue<Action>> sequenceAll(Text head) {
        Optional<Text> curMaybe = Optional.of(head);
        Map<Shape,Queue<Action>> maps = getConcMap(); // reassigned every time
        // viewed as immutable

        while(curMaybe.isPresent()) {
            final Text cur = curMaybe.get();
            maps = addStmt(cur.stmt);

        }

        // TODO - implement
        return null;
    }

    //
    private static Map<Shape,Queue<Action>> addStmt(Map<Shape,Queue<Action>> origMap, StmtType stmt) {
        switch(stmt) {
            case Put:
                break;
            default:
                System.out.println("oops");
                return null;
                break;
        }

        return null;
    }

    private static<K,V> Map<K,V> getConcMap() {
        return new HashMap<>();
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