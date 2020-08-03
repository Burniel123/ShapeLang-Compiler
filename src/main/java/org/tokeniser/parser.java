import com.Twople;
import com.parsercom.*;
// as far as possible, this parser is functional
// some loops will be found. This is for efficiency reasons
// this could be done entirely functionally, but this would involve recursion



public class Parser
{

	private final static String newPara = "\u0x0a"; // TODO - find actual utf 16 for paragraph break
	private final static String CANV_INIT_ERR = "Initialise canvas with <size> could not be found.\nPlease ensure this is at the top of the file!";
	private final static String CMD_ERR = "Error deciphering following text: ";

	public static CanvasInit tokenise(String toTokenise) throws TokeniseException
	{
		final String lc = toTokenise.toLowerCase();
		final String fst = lc.dropWhileNEQ("initialise");
		final String[] lines = fst.splitAt(newPara);
		// TODO - extend this so Initialise doesn't have to be on literally the first line
		final String words[] = wordify(lines);

		switch (words[0]) {
			case "initialise" : 
				final CanvasInit ci = new CanvasInit();
				ci.size = sizeString(words[2]);
				cs.next = tokenise(Arrays.copyOfRange(words,1,words.length());
				return ci;
				break;
			default: throw new TokeniseException(CANV_INIT_ERR);
				break;
		}

		return null; // it shouldn't ever hit this point
	}
		
	

	private static [] tokenise(String[] lines) throws TokeniseException
	{
		final Text head = new Text();
		Text cur = head; // this is part of our loop variant. This will 
		// change so as to avoid recursion

		for(String line: lines)
		{
			switch(lines[0]) {
				case "Put" :

					break;
				default: throw new TokeniseException(CMD_ERR+lines[0]);
		}
	}


	private static String[] wordify(String line){
		return line.splitAt(" ");
	}

	private static Twople<Integer,Integer> sizeString(String tuple) {
		// specific format: [some stuff]"("[num1],[num2]")"[some stuff] -> ([num],[num])
		while(
	}

	private static dropWhileNEQ(String toDrop, String eq)
	{ // TODO - investigate if built in method exists
		int count = 0;
		
		while(toDrop.length() - eq.length > count && 
			eq != toDrop.substring(count,count+eq.length()))
				count++;

		return toDrop.substring(count);
	}
}
