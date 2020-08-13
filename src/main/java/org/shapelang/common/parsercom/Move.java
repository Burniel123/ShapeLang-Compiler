package blah;

import org.shapelang.common.Twople;
import org.shapelang.common.parsercom.ParserToken;
import org.shapelang.shapes.Shape;

public class Move implements StmtType
{
	@Override
	public ParserToken StmtType() {
		return ParserToken.MOVE;
	}

	public final Shape shapeRef;
	public final Twople<Integer,Integer> newCoord;
}
