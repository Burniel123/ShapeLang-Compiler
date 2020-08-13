package org.shapelang.common.parsercom;

import org.shapelang.common.Twople;
import org.shapelang.shapes.Shape;

public class Resize implements StmtType
{
	@Override
	public ParserToken stmtType() {
		return ParserToken.RESIZE;
	}

	public final Shape shapeRef;
	public final Twople<Integer,Integer> size;
}
