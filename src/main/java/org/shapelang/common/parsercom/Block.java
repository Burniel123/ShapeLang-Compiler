package org.shapelang.common.parsercom;

import org.shapelang.common.parsercom.ParserToken;
import org.shapelang.common.parsercom.StmtType;
import org.shapelang.shapes.Shape;

public class Block implements StmtType
{
	@Override
	public ParserToken stmtType() {
		return ParserToken.BLOCK;
	}

	// specifies how long to block after all shapes have reached
	@Override
	public int time() {
		return 0;
	}

	public Block(Shape[] shapes) {
		this.shapes = shapes;
	}

	public final Shape[] shapes;
}
