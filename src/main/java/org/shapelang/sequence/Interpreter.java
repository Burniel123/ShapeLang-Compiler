package org.shapelang.sequence;

import org.shapelang.common.parsercom.*;

import java.util.Queue;

public class Interpreter {
    public static void interpret(Queue<Action> actQ) {
        while(!actQ.isEmpty()) {
            final Action act = actQ.remove();
            interpret(act);

        }
    }

    private static void interpret(Action act) {
        final StmtType stmt = act.stmt();
        switch(stmt.stmtType()) {
            case PUT:
                execPut((Put) stmt);
                break;
            case MOVE:
                execMove((Move) stmt);
                break;
            case LOOP:
                break;
            case SEQ:
                break;
            case BLOCK:
                break;
            case OTHER:
                break;
            case RESIZE:
                break;
            default: break;
        }
    }

    private static void execMove(Move mv) {
        mv.shapeRef.moveTransition(mv.coord.fst,mv.coord.snd,mv.time());
    }

    private static void execPut(Put put) {
        put.shapeRef.place(put.coords.fst,put.coords.snd);
    }
}
