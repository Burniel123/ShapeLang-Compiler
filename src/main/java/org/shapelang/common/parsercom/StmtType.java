package org.shapelang.common.parsercom;

public interface StmtType
{
	ParserToken stmtType(); // allows for correct casting

	/*
	 * pre: time >= 0
	 */
	int time(); // the amount of time the action will take
	// used for sequencing; 0 is a perfectly acceptable time
}
