package org.shapelang.sequence;

import org.shapelang.common.Twople;
import org.shapelang.common.parsercom.StmtType;

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