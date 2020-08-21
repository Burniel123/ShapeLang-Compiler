package org.shapelang.common.parsercom;

import org.shapelang.shapes.Shape;

import java.util.Optional;

public class Loop implements StmtType
{
	@Override
	public ParserToken stmtType()
	{
		return ParserToken.LOOP;
	}

	public Loop(Optional<Integer> numIter, Shape[] shapes, Text contents) {
		this.numIter = numIter;
		this.shapes = shapes;
		this.contents = contents;
	}

	public final Optional<Integer> numIter; // None indicates infinite loop
	public final Shape[] shapes;
	public final Text contents;
}
