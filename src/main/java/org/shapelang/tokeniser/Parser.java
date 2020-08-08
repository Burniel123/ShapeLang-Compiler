package org.shapelang.tokeniser;

import org.shapelang.shapes.Shape;
import org.shapelang.common.*;
import java.util.Map; // useful if implementation is to be swapped out later
import java.util.HashMap; // concrete implementation for above

// as far as possible, this parser is functional
// some loops will be found. This is for efficiency reasons
// this could be done entirely functionally, but this would involve recursion



public class Parser
{

	private final static String newPara = "\n"; // TODO - ensure this works
	private final static String CANV_INIT_ERR = "Initialise canvas with <size> could not be found.\nPlease ensure this is at the top of the file!";
	private final static String CMD_ERR = "Error deciphering following text: ";
	private final static String LOOP_OOB_ERR = "Error with number in for loop.\nCheck your for loops have valid numbers";
	private final static String COORD_OOB_ERR = "Error with the co-ordinates of a shape.\nCheck your shapes have coordinates in form (x,y)";
	private final static String PUT_SYN_ERR = "Error with syntax of Put statement.\nCheck your Put statements are all correct";
	private final static String NO_INIT_ERR = "No shape with that identifier was found.\nCheck you've initialised that shape.";

	public static CanvasInit tokenise(String toTokenise) throws TokeniseException {
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
	private static Twople<Text,Integer> tokenise(String[] lines) throws TokeniseException {
		final Text head = new Text();
		final Map<String,Shape> idMap = new HashMap();
		// stores mapping from identifiers to objects
		// only tokenise should add or remove from this
		Text cur = Optional.of(head);
	      	// current text being parsed	
		int count = 0;

		while(count < lines.length()) {
			final String[] line = lines[count].splitAt(" ");
			final Statement curAct; // current action

			switch(line[0]) {
				case "put":
					final Put put = new Put();
					final Twople<String,Shape> binding 
						= shapeify(line);
					idMap.put(binding.fst,binding.snd);

					put.shapeRef = binding.snd;
					put.shapeSizeParse(line);
					put.coords = coordinatify(line);

					curAct = put;
					break;
				case "move":
					final Move mv = new Move();
					final String ident = line[1];
					final Shape maybeShape 
						= getShape(idmap,ident);
					
					if(maybeShape.isPresent()) {
						mv.shapeRef = maybeShape.get();
						mv.newCoord = coordinatify(line);
					}
					else
						throw new TokeniseException(NO_INIT_ERR);
					
					curAct = mv;
					break;
				case "resize":
					final Resize rsz = new Resize();
					final String ident = line[1];
					final Shape maybeShape
						= getShape(idmap,ident);
					if(maybeShape.isPresent) {
						rsz.shapeRef = maybeShape.get();
						rsz.newCoorgd = coordinatify(line);
					}
					else
						throw new TokeniseExcepiton(NO_INIT_ERR);


					curAct = rsz;
					break;

				// TODO - how to deal with only certain shapes
				// being allowed in loop?
				case "loop":
					final Loop loop = new Loop();

					loop.numIter = Optional.empty();

					final Twople<Text,Integer> res = 
						loopify(lines,count); // TODO - how to treat mappings from 'super function'
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
					cur.next = Optional.of(head); // loops are cyclic 'lists' 
					return new Twople(head,count);
					break;
				case "endfor":
					cur.next = Optional.of(head);
					return new Twople(head,count);
					break;
				case "block":
					curAct = restrictify
						(idmap,line,new Block());
					break;
				case "sequential":
					curAct = restrictify
						(idmap,line,new SequentialBlock());
					break;
				case "endsequential":
					cur = Optional.empty();
					return new Twople(head,count);
					break;
				default: throw new TokeniseException(CMD_ERR+lines[0]);
					break;
			}

			cur.stmt = curAct;

			// move onto next line
			final Text nxt = new Text();
			cur.next = Optional.of(nxt);
			cur = nxt;
			count++;
		}

		return new Twople(head,count);
	}

	// so called because for map: A -> B, restrictify: A -> C st. C subset B
	private static StmtType restrictify
		(Map<String,Shapes> map, String[] line, StmtType x) {
		switch(line[0]) {
			case "_":
				x.shapes = map.values(); // TODO - ensure this casts correctly from collection to array
				break;
			default: final String[] idents = Array.copyOfRange(line,1,line.length) 
				x.shapes = getMappings(map, idents);
				break;
		}
		return x;

	}	

	private static V[] getMappings(Map<U,V> map, U[] keys) {
		final V[] values = new Array[keys.length];
		for(int i = 0; i < keys.length; i++) {
			final U key = keys[i];
			values[i] = map.get(key);
		}
		return values;
	}

	private static Optional<V> getShape<U,V>(Map<U,V> map, U key) {
		final V value 
			= map.getOrDefault(key, null); // careful with npe
		if(null == value)
			return Optional.empty();
		else
			return Optional.of(value);
	}

	// maps put line to shape and String identifier
	private static Twople<String,Shape> shapeify(String[] line) {
		boolean shapeFound = false;
		final Shape shape;
		for(String word: line) {
			switch(word) {
				case "circle": shape = new Circle();
					shapeFound = true;
					break;
				default: 
					if(shapeFound)
						return new Twople(word,shape);
					break;
			}
		}

		throw new TokeniseException(PUT_SYN_ERR);
	}

	private static Twople<Integer,Integer> coordinatify(String[] line) {
		boolean next = false;
		for(String word: line) {
			switch(next) {
				case true: return sizeString(line);
					break;
				case false: next = word.equals("to");
					break;
			}
		}

		throw new TokeniseException(COORD_OOB_ERR);

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
				(lines, count+1, lines.length()));
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
		
		while(toDrop.length() - eq.length() > count && 
			eq != toDrop.substring(count,count+eq.length()))
				count++;

		return toDrop.substring(count);
	}
}
