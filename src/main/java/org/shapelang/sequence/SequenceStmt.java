package org.shapelang.sequence;

import org.shapelang.common.Twople;
import org.shapelang.common.parsercom.*;
import org.shapelang.shapes.Shape;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Queue;

public class SequenceStmt {
    // ideally you want this to return a queue of Twoples st.
    // shape is shape to be operated upon
    // StmtType is action to perform
    public static Collection<Queue<Twople<Shape,StmtType>>> sequenceStmt(CanvasInit ci) {
        Collection<Queue<Twople<Shape,StmtType>>> sequences = getConc(); // not final to allow for 'immutable' object
        final Text head = ci.next;
        Optional<Text> curMaybe = Optional.of(head);
        // mutable to allow for variant in loop inst. of recursion

        while(curMaybe.isPresent()) {
            final Text cur = curMaybe.get();

            sequences = addStmt(sequences, cur.stmt);

            curMaybe = cur.getNext();
        }


        return sequences;
    }

    private static Collection<Queue<Twople<Put,StmtType>>> addStmt(Collection<Queue<Twople<Put,StmtType>>> seq, StmtType stmt) {
        final Collection<Queue<Twople<Put,StmtType>>> ret = seq;
        switch(stmt.stmtType()) {
            case PUT:
                return putStmt(seq, (Put) stmt);
                break;
            default: System.out.println("shouldn't be here");
        }
    }

    private static Collection<Queue<Twople<Put,StmtType>>> putStmt(Collection<Queue<Twople<Put,StmtType>>> seq, Put put) {
        return seq.add(new Twople(put,));
    }

    private static<E> Collection<E> getConc() {
        return new HashSet<Queue<StmtType>>;
    }
}
