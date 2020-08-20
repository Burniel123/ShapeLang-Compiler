package org.shapelang.common.parsercom;

import java.util.Optional;

public class Text
{
	public Text(StmtType stmt, Optional<Text> next) {
		this.stmt = stmt;
		this.next = next;
	}

	public final StmtType stmt;
	public final Optional<Text> next;
}
