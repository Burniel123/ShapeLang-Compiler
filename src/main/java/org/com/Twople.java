package com;

public class Twople<U,V> 
{ 
	// immutable because that's the standard in many languages. Make a new
	// tuple if you want to change state
	public final U fst;
	public final V snd;

	public Twople(U fst, V snd) {
		this.fst = fst;
		this.snd = snd;
	}

	// due to type erasure, an equals method cannot be implemented easily
}

