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

	public final Shape[] shapes;
}
