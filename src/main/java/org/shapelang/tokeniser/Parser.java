package org.shapelang.tokeniser;

import org.shapelang.common.*;
// as far as possible, this parser is functional
// some loops will be found. This is for efficiency reasons
// this could be done entirely functionally, but this would involve recursion



public class Parser
{

	private final static String newPara = "\n"; // TODO - ensure this works
	private final static String CANV_INIT_ERR = "Initialise canvas with <size> could not be found.\nPlease ensure this is at the top of the file!";
	private final static String CMD_ERR = "Error deciphering following text: ";
	private final static String LOOP_OOB_ERR = "Error with number in for loop.\nCheck your for loops have valid numbers";

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
				ci.next = tokenise(Arrays.copyOfRange(words,1,words.length())).fst;
				return ci;
				break;
			default: throw new TokeniseException(CANV_INIT_ERR);
				break;
		}

		return null; // it shouldn't ever hit this point
	}
		
	// when it calls itself for a loop, pos in loop required
	private static Twople<Text,Integer> tokenise(String[] lines) throws TokeniseException
	{
		final Text head = new Text();
		Text cur = head; // this is part of our loop variant. This will 
		// change so as to avoid recursion
		int count = 0;

		while(count < lines.length)
		{
			final String[] line = lines[count].splitAt(" ");
			final Statement curAct; // current action

			switch(line[0]) {
				case "put":
					
					break;
				case "move":
					break;
				case "resize":
					break;
				case "loop":
					final Loop loop = new Loop();

					loop.numIter = Optional.empty();

					final Twople<Text,Integer> res = 
						loopify(lines,count);
					loop.contents = twople.fst;
					count = twople.snd;
					
					curAct = loop;
					break;
				case "for":
					final Loop loop = new Loop();
					
					loop.numIter = 
						Optional.of(loopIterify(line));
					if(0 > loop.numIter)
						throw new TokeniseException(LOOP_OOB_ERR);

					final Twople<Text,Integer> res =
						loopify(lines,count);
					loop.contents = twople.fst;
					count = twople.snd;

					curAct = loop;
					break;
				case "endloop":
					return new Twople(head,count);
					break;
				case "endfor":
					return new Twople(head,count);
					break;
				case "block":
					break;
				case "sequential":
					break;
				case "endsequential":
					break;
				default: throw new TokeniseException(CMD_ERR+lines[0]);
					break;
			}

			cur.stmt = curAct;

			// move onto next line
			final Text nxt = new Text();
			cur.next = nxt;
			cur = cur.next;
			count++;
		}

		return new Twople(head,count);
	}

	private static int loopIterify(String[] line) {
		boolean next = false;
		for(String word: line) {
			switch (next) {
				case true: return Integer.parseInt(line);
					 break;
				case false: next = line.equals("do");
					 break;
			}
		}

		return -1;
	}

	private static Twople<Text,Integer> loopify(String[] lines, int count) {
		return tokenise(lines.copyOfRange
				(lines, count+1, lines.length));
	}

	private static String[] wordify(String line){
		return line.splitAt(" ");
	}

	private static Twople<Integer,Integer> sizeString(String tuple) {
		// specific format: [some stuff]"("[num1],[num2]")"[some stuff] -> ([num],[num])
		
		final int fst;
		final int snd;
		int pos = 0;
		
		while('(' != tuple.charAt(pos))
			pos++;
		String fstDig = "";

		while(',' != tuple.charAt(pos)) {
			fstDig += tuple.charAt(pos);
			pos++;
		}
		// TODO - account for spaces

		fst = Integer.parseInt(fstDig);
		pos += 1;
		String sndDig = "";

		while(')' != tuple.charAt(pos)) {
			sndDig += tuple.charAt(pos);
			pos++;
		}

		snd = Integer.parseInt(sndDig);

		return new Twople(fst,snd);
	}

	private static String dropWhileNEQ(String toDrop, String eq)
	{ // TODO - investigate if built in method exists
		int count = 0;
		
		while(toDrop.length() - eq.length > count && 
			eq != toDrop.substring(count,count+eq.length()))
				count++;

		return toDrop.substring(count);
	}
}
