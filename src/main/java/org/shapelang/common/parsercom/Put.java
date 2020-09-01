package org.shapelang.common.parsercom;

import org.shapelang.common.Twople;
import org.shapelang.shapes.Shape;

public class Put implements StmtType
{
	@Override
	public ParserToken stmtType() {
		return ParserToken.PUT;
	}

	public Put(Shape shapeRef, Twople<Integer,Integer> coords) {
		this.shapeRef = shapeRef;
		this.coords = coords;
	}

	// particular instance of time: always takes 0
	public int time() {
		return 0;
	}

	public final Shape shapeRef;	
	public final Twople<Integer,Integer> coords;
}
