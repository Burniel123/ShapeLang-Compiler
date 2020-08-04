package org.shapelang.common;

public class Twople<U,V> 
{ 
	// immutable because that's the standard in many languages. Make a new
	// tuple if you want to change state
	public final U u;
	public final V v;

	public Twople(U u, V v) {
		this.u = u;
		this.v = v;
	}
}

