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

    // hopefully this is all we need
    // I _highly_ doubt it
    private static void interpret(Action act) {
        final StmtType stmt = act.stmt();
        switch(stmt.stmtType()) {
            case PUT:
                execPut((Put) stmt);
                break;
            case MOVE:
                execMove((Move) stmt);
                break;
            case RESIZE:
                execResize((Resize) stmt);
                break;
            default: break;
        }

    }

    private static void execResize(Resize rsz) {
        rsz.shapeRef.resizeTransition(rsz.factor,rsz.time());
    }

    private static void execMove(Move mv) {
        mv.shapeRef.moveTransition(mv.coord.fst,mv.coord.snd,mv.time());
    }

    private static void execPut(Put put) {
        put.shapeRef.place(put.coords.fst,put.coords.snd);
    }
}
