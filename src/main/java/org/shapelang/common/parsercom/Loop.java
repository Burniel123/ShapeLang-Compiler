package org.shapelang.common.parsercom;

import java.util.Optional;

public class Loop implements StmtType
{
	@Override
	public ParserToken stmtType()
	{
		return ParserToken.LOOP;
	}

	public final Optional<Integer> numIter; // None indicates infinite loop
	public final Shape[] shapes;
	public final Text contents;
}
