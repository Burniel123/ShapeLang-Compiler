public enum Token
{
        // instantiation
        PUT,
        COORD,
        2SIZE, // TODO - how to tokenise actual type size
        // control flow
        LOOP,
        FOR,
        ENDLOOP, // alias end of loops to same token
        SEQ,
        ENDSEQ,
        BLOCK,
        TIME,
        PARALLEL, // deals with paralellism within a single shape
        // modify shape 
        MOVE,
        RESIZE,
        ROTATE,
        // for blank lines
        NOP, // should be optimised out when constructing AST
}

class TokeniseException extends Exception
{
        TokeniseException(String s)
        {
                println("You're probably missing a space or linebreak
                                "somewhere");
                println("See the below stack trace for more info (line number
                        "tends to be very helpful");

                super(s);
        }
}

public class Twople<U,V> { // why this isn't a default i will never know
        // immutable because that's the standard in many languages. Make a new
        // tuple if you want to change state
        public final U u;
        public final V v;
        public Tuple(U u, V v) {
                this.u = u;
                this.v = v;
        }
}
