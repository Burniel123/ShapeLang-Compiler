package org.shapelang.common.parsercom;

import org.shapelang.shapes.Shape;

public class Sequential implements StmtType
{
	@Override
	public ParserToken stmtType() {
		return ParserToken.SEQ;
	}

	public Sequential(Shape[] shapes, Text text) {
		this.shapes = shapes;
		this.text = text;
	}

	@Override
	public int time() {
		return 0;
	}

	public final Shape[] shapes;
	public final Text text;
}
