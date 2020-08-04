package org.shapelang.common.parsercom;

public interface StmtType
{
	default ParserToken stmtType()
	{
		return ParserToken.OTHER;
	}
}
