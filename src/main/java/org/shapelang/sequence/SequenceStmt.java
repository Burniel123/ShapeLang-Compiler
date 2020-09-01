package org.shapelang.sequence;

import org.shapelang.common.Twople;
import org.shapelang.common.parsercom.*;

import java.util.List;
import java.util.Queue;

public class SequenceStmt {
    public static Queue<Action> sequence(CanvasInit ci) {
        final Text head = ci.next;
        final List<Queue<Action>> sequenceQueues = sequenceAll(head);
        return mergeSequences(sequenceQueues);
    }

    public static List<Queue<Action>> sequenceAll(Text head) {
        // TODO - implement
        return null;
    }

    public static Queue<Action> mergeSequences(List<Queue<Action>> seqs) {
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