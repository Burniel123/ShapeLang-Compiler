package org.shapelang.common.parsercom;

import java.util.Optional;

public class Loop implements StmtType
{
	public Loop(Optional<Integer> numIter) {
		this.numIter = numIter;
	}

	@Override
	public ParserToken stmtType()
	{
		return ParserToken.LOOP;
	}

	public final Optional<Integer> numIter; // None indicates infinite loop
	public final Text contents;
}
