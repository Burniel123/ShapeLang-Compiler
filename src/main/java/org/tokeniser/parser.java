class TokeniseException extends Exception
{
	TokeniseException(String s)
	{
		println("You're probably missing a space or linebreak 
				"somewhere");
		println("See the below stack trace for more info (line number 
			"tends to be very helpful");

		super(s);
	}
}

public enum Tokens
{
	// instantiation
	PUT,
	COORD,
	2SIZE, // TODO - how to tokenise actual type size
	// control flow
	LOOP,
	FOR,
	ENDLOOP, // alias end of loops to same token
	SEQ,
	ENDSEQ,
	BLOCK,
	TIME,
	// modify shape 
	MOVE,
	RESIZE,
	ROTATE,
}

public class Parser
{
	private final static String newPara = "\u0x0a" // TODO - find actual utf 16 for paragraph break
	private static tokenise(String toTokenise) throws TokeniseException
	{
		String[] consume = toTokenise.split(newPara); // split into array
		
		for(String cur: consume) // iterate over consume
		{
			switch(cur)
			{
				case "
			}
		}
	}

	private static 
}
