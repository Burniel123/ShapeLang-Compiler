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
	private final static String newPara = "\\*\n*"; // TODO - ensure this works
	private final static String CANV_INIT_ERR = "Initialise canvas with <size> could not be found.\nPlease ensure this is at the top of the file!";
	private final static String CMD_ERR = "Error deciphering following text: ";
	private final static String LOOP_OOB_ERR = "Error with number in for loop.\nCheck your for loops have valid numbers";
	private final static String LOOP_SYN_ERR = "Error with loop syntax (no loop keyword found)\nYou shouldn't see this, raise an issue on the github page please :)";
	private final static String LOOP_BOUND_ERR = "Error find loop bounds.\nCheck you have input valid bounds for all loops";
	private final static String COORD_OOB_ERR = "Error with the co-ordinates of a shape.\nCheck your shapes have coordinates in form (x,y)";
	private final static String PUT_SYN_ERR = "Error with syntax of Put statement.\nCheck your Put statements are all correct";
	private final static String NO_INIT_ERR = "No shape with that identifier was found.\nCheck you've initialised that shape.";
	private final static String RSZ_FAC_ERR = "No factor keyword found\nCheck you've correctly resized your shape";

	// maps a string to a tokenised representation of the text
	// pre: null != toTokenise
	// post: state = state0 /\ String -> tokens
	public static CanvasInit tokenise(String toTokenise) throws TokeniseException {
		final String lc = toTokenise.toLowerCase();
		final String fst = dropWhileNEQ(lc, "initialise"); // TODO - check this is fine
		final String[] lines = lineify(fst);
		final String[] words = wordify(lines[0]);

		switch (words[0]) {
			case "initialise":
				final Twople<Integer,Integer> size = sizeString(words[2]);
				final Text next = tokenise(Arrays.copyOfRange(lines, 1, lines.length),Optional.empty()).fst;
				return new CanvasInit(size,next);
			default:
				throw new TokeniseException(CANV_INIT_ERR);
		}
	}



	// when it calls itself for a loop, pos in loop required
	private static Twople<Text, Integer> tokenise(String[] lines, Optional<Map<String,Shape>> prevMap)
			throws TokeniseException {
		final Text head = new Text();
		final Map<String, Shape> idMap = mapify(prevMap);
		// stores mapping from identifiers to objects
		// only tokenise should add or remove from this (to keep state out of other methods)

		Optional<Text> cur = Optional.of(head);
		// current text being parsed
		int count = 0;

		while (count < lines.length) {
			final String[] line = wordify(lines[count]);
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
					final Twople<Loop,Integer> loopRes = loopify(lines,count,idMap);
					curAct = loopRes.fst;
					count = loopRes.snd;
					break;
				case "block":
					curAct = blockify(idMap,line);
					break;
				case "sequential":
					final Twople<Sequential,Integer> seqRes =
							sequentialify(idMap,line, Arrays.copyOfRange(lines,count+1,lines.length));
					curAct = seqRes.fst;
					count = seqRes.snd;
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

			final Text unwrappedText = cur.get();
			// if it's reached this point, it shouldn't be nothing (as it shouldn't be end of loop)

			unwrappedText.stmt = curAct;

			// move onto next line
			final Text nxt = new Text(curAct);
			cur.next = Optional.of(nxt);
			cur = Optional.of(nxt);
			count++;
		}

		return new Twople(head, count);
	}

	private static Move moveify(Map<String,Shape> idMap, String[] line) throws TokeniseException {
		final String ident = line[1];
		final Optional<Shape> maybeShape = getShape(idMap, ident);

		if(maybeShape.isPresent()) {
			final Shape shapeRef = maybeShape.get();
			final Twople<Integer,Integer> coord = coordinatify(line);
			return new Move(shapeRef, coord);
		}
		else
			throw new TokeniseException(NO_INIT_ERR);

	}

	private static Resize resizeify(Map<String,Shape> map, String[] line) throws TokeniseException {
		final String ident = line[1];
		final Optional<Shape> maybeShape
				= getShape(map,ident);

		if(maybeShape.isPresent()) {
			final Shape shapeRef = maybeShape.get();
			final double size = getResize(line);
			return new Resize(shapeRef, size);
		}
		else
			throw new TokeniseException(NO_INIT_ERR);
	}

	private static double getResize(String[] line) throws TokeniseException {
		boolean isNext = false;
		for(String word: line) {
			if(!isNext)
				switch(word) {
					case "factor":
						isNext = true;
						break;
					default:
						break;
				}
			else
				return Double.parseDouble(word);
		}

		throw new TokeniseException(RSZ_FAC_ERR);
	}

	private static Twople<Sequential,Integer> sequentialify(Map<String,Shape> map, String[] currentLine, String[] text) throws TokeniseException {
		final Shape[] allowedShapes = getShapeRefs(map, currentLine);
		final Twople<Text,Integer> inner = tokenise(text,Optional.of(map));
		final Sequential seq = new Sequential(allowedShapes,inner.fst);
		return new Twople(seq,inner.snd);
	}

	private static Block blockify(Map<String,Shape> map, String[] line) {
		final Shape[] shapes = getShapeRefs(map,line);
		return new Block(shapes);
	}

	// gets references of shapes
	// originally restrictify
	private static Shape[] getShapeRefs(Map<String,Shape> map, String[] line) {
		final Shape[] shapes;
		switch(line[0]) {
			case "_":
				shapes = getAllMappings(map);
				break;
			default: final
				String[] idents = Arrays.copyOfRange(line,1,line.length);
				shapes = getMappings(map, idents);
				break;
		}
		return shapes;
	}

	private static <K,V> V[] getAllMappings(Map<K,V> map) {
		return (V[]) map.values().toArray(new Object[0]);
		// TODO - see type erasure TODO
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
		final Twople<Integer,Integer> coords = coordinatify(line);
		return new Put(shapeRef,coords);
	}

	// maps line -> (id -> ShapeRef) st. line = "Put ":shapeRef:id:shapeInfo
	private static Twople<String,Shape> shapeify(String[] line) throws TokeniseException {
		final Shape shape;

		if("put".equals(line[0])) {
			switch(line[1]) {
				case "circle":
					shape = SLCircle.parseSize(line);
					break;
				default:
					throw new TokeniseException(PUT_SYN_ERR);
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

	private static Twople<Loop, Integer> loopify(String[] lines, int count, Map<String,Shape> map) throws TokeniseException {
		final String[] words = wordify(lines[count]);
		final Optional<Integer> numIter;

		switch(words[0]) {
			case("for"):
				numIter = Optional.of(loopIterify(words));
				break;
			case("loop"):
				numIter = Optional.empty();
				break;
			default:
				throw new TokeniseException(LOOP_SYN_ERR);
		}

		final Shape[] shapes = getShapeRefs(map,words);
		final Twople<Text,Integer> inner = tokenise(lines,Optional.of(map)); // TODO - figure out params for tokenise
		final Loop loop = new Loop(numIter,shapes,inner.fst);
		return new Twople<>(loop,inner.snd);
	}

	private static String[] wordify(String line){
		return line.split("\\* *");
	}
	private static String[] lineify(String text) {
		return text.split(newPara);
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
		
		while(toDrop.length() - eq.length() > count && eq.equals(toDrop.substring(count,count+eq.length())))
			count++;

		return toDrop.substring(count);
	}

	private static Map<String,Shape> mapify(Optional<Map<String,Shape>> prev) {
		return prev.orElse(new HashMap<>());
	}
}
