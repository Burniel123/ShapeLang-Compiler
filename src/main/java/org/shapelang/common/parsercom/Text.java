package org.shapelang.common.parsercom;

import java.util.Optional;

public class Text
{
	public Text(StmtType stmt) {
		this.stmt = stmt;
	}

	public Optional<Text> getNext() {
		return next;
	}

	public void setNext(Optional<Text> next) {
		this.next = next;
	}

	public final StmtType stmt;
	private Optional<Text> next; // the only non functional part of the tokens
	// this has been left _purely_ to allow for loop use instead of recursion
}
