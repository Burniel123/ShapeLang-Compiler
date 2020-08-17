package org.shapelang.tokeniser;

import org.shapelang.common.parsercom.*;
import org.shapelang.shapes.SLCircle;
import org.shapelang.shapes.Shape;
import org.shapelang.common.*;

import java.util.Arrays;
import java.util.Map; // useful if implementation is to be swapped out later
import java.util.HashMap; // concrete implementation for above
import java.util.Optional;

// as far as possible, this parser is functional
// some loops will be found. This is for efficiency reasons
// this could be done entirely functionally, but this would involve (more) recursion



public class Parser {
	private final static String newPara = "*\n*"; // TODO - ensure this works
	private final static String CANV_INIT_ERR = "Initialise canvas with <size> could not be found.\nPlease ensure this is at the top of the file!";
	private final static String CMD_ERR = "Error deciphering following text: ";
	private final static String LOOP_OOB_ERR = "Error with number in for loop.\nCheck your for loops have valid numbers";
	private final static String LOOP_BOUND_ERR = "Error find loop bounds.\nCheck you have input valid bounds for all loops";
	private final static String COORD_OOB_ERR = "Error with the co-ordinates of a shape.\nCheck your shapes have coordinates in form (x,y)";
	private final static String PUT_SYN_ERR = "Error with syntax of Put statement.\nCheck your Put statements are all correct";
	private final static String NO_INIT_ERR = "No shape with that identifier was found.\nCheck you've initialised that shape.";

	// maps a string to a tokenised representation of the text
	// pre: null != toTokenise
	// post: state = state0 /\ String -> tokens
	public static CanvasInit tokenise(String toTokenise) throws TokeniseException {
		final String lc = toTokenise.toLowerCase();
		final String fst = dropWhileNEQ(lc, "initialise"); // TODO - check this is fine
		final String[] lines = fst.split(newPara);
		final String[] words = wordify(lines[0]);

		switch (words[0]) {
			case "initialise":
				final CanvasInit ci = new CanvasInit();
				ci.size = sizeString(words[2]);
				ci.next = tokenise(Arrays.copyOfRange(lines, 1, lines.length),Optional.empty()).fst;
				return ci;
			break;
			default:
				throw new TokeniseException(CANV_INIT_ERR);
				break;
		}

		return null; // it shouldn't ever hit this point
	}



	// when it calls itself for a loop, pos in loop required
	private static Twople<Text, Integer> tokenise(String[] lines, Optional<HashMap<String,Shape>> prevHMP)
			throws TokeniseException {
		final Text head = new Text();
		final Map<String, Shape> idMap = hashMapify(prevHMP);
		// stores mapping from identifiers to objects
		// only tokenise should add or remove from this (to keep state out of other methods)

		Optional<Text> cur = Optional.of(head);
		// current text being parsed
		int count = 0;

		while (count < lines.length) {
			final String[] line = lines[count].split("* *");
			final StmtType curAct; // current action

			// TODO - find a way of using StmtType to downcast correctly
			switch (line[0]) {
				case "put":
					final Twople<String, Shape> binding
							= shapeify(line);
					idMap.put(binding.fst, binding.snd);
					// this is the only place the hash map should have object added to it
					curAct = putify(binding.snd, line);
					break;
				case "move":
					curAct = moveify(idMap, line);
					break;
				case "resize":
					curAct = resizeify(idMap, line);
					break;

				// TODO - how to deal with only certain shapes being allowed in loop
				// TODO - refactor this and for into aux function
				case "loop":
				case "for":
					final Twople<Loop,Integer> loopRes = loopify(lines,count);
					curAct = loopRes.fst;
					count = loopRes.snd;
					break;
				case "block":
					curAct = restrictify
							(idMap, line, new Block());
					break;
				case "sequential":
					curAct = restrictify
							(idMap, line, new SequentialBlock());
					break;
				case "endloop":
				case "endfor":
				case "endsequential":
					cur = Optional.empty();
					return new Twople(head, count);
				break;
				default:
					throw new TokeniseException(CMD_ERR + lines[0]);
					break;
			}

			cur.stmt = curAct;

			// move onto next line
			final Text nxt = new Text();
			cur.next = Optional.of(nxt);
			cur = Optional.of(nxt);
			count++;
		}

		return new Twople(head, count);
	}

	private static Move moveify(Map<String,Shape> idMap, String[] line) throws TokeniseException {
		final Move mv = new Move();
		final String ident = line[1];
		final Optional<Shape> maybeShape = getShape(idMap, ident);

		if(maybeShape.isPresent()) {
			mv.shapeRef = maybeShape.get();
			mv.newCoord = coordinatify(line);
		}
		else
			throw new TokeniseException(NO_INIT_ERR);

		return mv;
	}

	private static Resize resizeify(Map<String,Shape> map, String[] line) throws TokeniseException {
		final Resize rsz = new Resize();
		final String ident = line[1];
		final Optional<Shape> maybeShape
				= getShape(map,ident);

		if(maybeShape.isPresent()) {
			rsz.shapeRef = maybeShape.get();
			rsz.size = coordinatify(line);
		}
		else
			throw new TokeniseException(NO_INIT_ERR);

		return rsz;
	}

	// so called because for map: A -> B, restrictify: A -> C st. C subset B
	private static StmtType restrictify(Map<String,Shape> map, String[] line, StmtType x) {
		switch(line[0]) {
			case "_":
				x.shapes = map.values(); // TODO - ensure this casts correctly from collection to array
				break;
			default: final String[] idents = Arrays.copyOfRange(line,1,line.length)
				x.shapes = getMappings(map, idents);
				break;
		}
		return x;

	}	

	private static <K,V> V[] getMappings(Map<K,V> map, K[] keys) {
		final V[] values = (V[])new Object[keys.length]; // I hate this, type erasure is a sin
		// TODO - I have a funny feeling this cast won't work due to _type erasure_, but make sure it behaves
		for(int i = 0; i < keys.length; i++) { // not iterator cos i needed for key and values
			final K key = keys[i];
			values[i] = map.get(key);
		}
		return values;
	}

	private static <K,V> Optional<V> getShape(Map<K,V> map, K key) {
		final V value = map.getOrDefault(key, null); // careful with npe
		if(null == value)
			return Optional.empty();
		else
			return Optional.of(value);
	}

	private static Put putify(Shape shapeRef, String[] line) throws TokeniseException {
		final Put put = new Put();
		put.shapeRef = shapeRef;
		put.coords = coordinatify(line);
		return put;
	}

	// maps line -> (id -> ShapeRef) st. line = "Put ":shapeRef:id:shapeInfo
	private static Twople<String,Shape> shapeify(String[] line) throws TokeniseException{
		boolean shapeFound = false;
		final Shape shape;

		if("put".equals(line[0])) {
			switch(line[1]) {
				case "circle":
					shape = SLCircle.parseSize(line);
					break;
				default:
					throw new TokeniseException(PUT_SYN_ERR);
					break;
			}
			final Twople<Integer,Integer> initialPlace = coordinatify(line);
			shape.place(initialPlace.fst,initialPlace.snd);

			return new Twople(line[2],shape);
		}

		throw new TokeniseException(PUT_SYN_ERR);
	}

	private static Twople<Integer,Integer> coordinatify(String[] line) throws TokeniseException {
		boolean next = false;
		for(String word: line) {
			if(!next)
				next = word.equals("to") || word.equals("at");
			// TODO - ascertain at does not adversely affect syntax in any way
			else
				return sizeString(word);
		}

		throw new TokeniseException(COORD_OOB_ERR);

	}

	private static int loopIterify(String[] line) throws TokeniseException {
		boolean next = false;
		for(String word: line) {
			if(!next)
				next = word.equals("do");
			else
				return Integer.parseInt(word);
		}

		throw new TokeniseException(LOOP_BOUND_ERR);
	}

	private static Twople<Loop, Integer> loopify(String[] lines, int count) throws TokeniseException {
		final Loop loop = new Loop();
		final String[] words = wordify(lines[count]);

		switch(words[0]) {
			case("for"):
				loop.numIter = Optional.of(loopIterify(words));
				break;
			case("loop"):
				loop.numIter = Optional.empty();
				break;
		}

		final Twople<Text,Integer> inner = tokenise(); // TODO - figure out params for tokenise
		loop.contents = inner.fst;
		return new Twople(loop,inner.snd);
	}

	private static String[] wordify(String line){
		return line.split("* *");
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

	private static String dropWhileNEQ(String toDrop, String eq) {
		int count = 0;
		
		while(toDrop.length() - eq.length() > count && eq.equals(toDrop.substring(count,count+eq.length())));
				count++;

		return toDrop.substring(count);
	}

	private static HashMap<String,Shape> hashMapify(Optional<HashMap<String,Shape>> prev) {
		return prev.orElse(new HashMap<String,Shape>);
	}
}
