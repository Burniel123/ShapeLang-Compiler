package org.shapelang.common.parsercom;

import org.shapelang.shapes.Shape;

public class SequentialBlock implements StmtType
{
	@Override
	public ParserToken stmtType() {
		return ParserToken.SEQ;
	}

	public final Shape[] shapes;
	public final Text text;
}
