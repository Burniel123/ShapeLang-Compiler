package Common;

// for all y'all not familiar with fp, better get used to it quick

public class CanvInit
{
	public final TwoSize size;
	public final Text next;
}

public class TwoSize
{
	public final Twople<Int,Int> size;
}

public class Text
{
	public final Statement stmt;
	public final Optional<Text> next;
}

public class Statement
{
	// how to deal with this effectively (AND FUNCTIONALLY)?
	// could we use a counter and match on the values?
	// would allow for easier extension
	// but in this case, type would depend on the int, which isn't 
	// necessarily something we want
	// as java gets rid of all type info at runtime, is this feasible?
	// do we need to abuse inheritance?
}

public class Initialise<U>
{
	
}

public class Place
{
	public final Twople<Int,Int> coord;
}

abstract class Shape // come back to this: could specify points as a list
{
	
}

public class Loop
{
	public Optional<Integer> noIter; // None indicates an infinite loop
	public Text contents;
}

public class Action
{
	public Action()
	{
		throw new NotImplementedException("oops");
	}
}

public class Sequential
{
	public final Shape idents;
	public final Optional<Text> contents; // None indicates it's a block rather than sequential
}

public class ParComp // for parallel actions on the same object
{
	public final List<Action> acts;
}
