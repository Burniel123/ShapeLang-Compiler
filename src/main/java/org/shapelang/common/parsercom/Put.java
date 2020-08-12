package org.shapelang.common.parsercom;

import org.shapelang.shapes.Shape;

public class Put implements StmtType
{
	@Override
	public ParserToken stmtType() {
		return ParserToken.PUT;
	}

	public void shapeSizeParse(String[] words) {
		shapeRef.shapeSizeParse(words);
	}

	public final Shape shapeRef;	
	public final Twople<Integer,Integer> coords;
}
