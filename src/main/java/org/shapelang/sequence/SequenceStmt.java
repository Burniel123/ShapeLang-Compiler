package org.shapelang.sequence;

import org.shapelang.common.Twople;
import org.shapelang.common.parsercom.*;
import org.shapelang.shapes.Shape;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Queue;

public class SequenceStmt {
    public static Queue<Action> sequence(CanvasInit ci) {
        final Text head = ci.next;
        Optional<Text> curMaybe = Optional.of(head);


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