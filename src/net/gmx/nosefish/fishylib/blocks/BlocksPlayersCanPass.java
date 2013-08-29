package net.gmx.nosefish.fishylib.blocks;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;


public class BlocksPlayersCanPass {
	static final SortedSet<Integer> ids; 
	private static Integer[] idArray = new Integer[] {
		0, 6, 8, 9, 10, 11,
		27, 28, 30, 31,
		32, 37, 38, 39, 40,
		50, 51, 55, 59, 63,
		64, 65, 66, 68, 69, 70, 71, 72, 75, 76, 77,
		83, 90,
		96, 104, 105, 106,
		115, 119, 127, 131, 132, 141, 142, 143,
		147, 148, 157,
		171 // version 1.6.2
	};
	static {
		TreeSet <Integer> tmpSet = new TreeSet<Integer>();
		Collections.addAll(tmpSet, idArray);
		ids = Collections.unmodifiableSortedSet(tmpSet);
		idArray = null;
	}
}
