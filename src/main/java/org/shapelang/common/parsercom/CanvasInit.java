package org.shapelang.common.parsercom;

import org.shapelang.common.Twople;

public class CanvasInit
{
	public CanvasInit(Twople<Integer,Integer> size, Text next) {
		this.size = size;
		this.next = next;
	}
	public final Twople<Integer,Integer> size;
	public final Text next;
}
