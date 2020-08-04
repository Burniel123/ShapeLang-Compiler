package com.parsercom;

public interface StmtType
{
	default ParserToken stmtType()
	{
		return OTHER;
	}
}
