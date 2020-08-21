package org.shapelang.common.parsercom;

import org.shapelang.common.Twople;
import org.shapelang.common.parsercom.ParserToken;
import org.shapelang.shapes.Shape;

public class Move implements StmtType
{
	@Override
	public ParserToken stmtType() {
		return ParserToken.MOVE;
	}

	public Move(Shape shapeRef, Twople<Integer,Integer> coord) {
		this.shapeRef = shapeRef;
		this.coord = coord;
	}

	public final Shape shapeRef;
	public final Twople<Integer,Integer> coord;
}
