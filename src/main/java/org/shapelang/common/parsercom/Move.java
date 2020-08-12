package blah;

public class Move implements StmtType
{
	@Override
	public ParserToken StmtType() {
		return ParserToken.MOVE;
	}

	public final Shape shapeRef;
	public final Twople<Integer,Integer> newCoord;
}
