package net.gmx.nosefish.fishylib.blocks;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import net.canarymod.api.world.blocks.BlockType;

public class StorageBlocks {
		static final SortedSet<Integer> ids; 
		private static final Integer[] idArray = new Integer[] {
			new Integer(BlockType.Chest.getId()),
			new Integer(BlockType.TrappedChest.getId()),
			new Integer(BlockType.Dispenser.getId()),
			new Integer(BlockType.Dropper.getId()),
			new Integer(BlockType.Hopper.getId()),
			// version 1.6.2
		};
		static {
			TreeSet <Integer> tmpSet = new TreeSet<>();
			Collections.addAll(tmpSet, idArray);
			ids = Collections.unmodifiableSortedSet(tmpSet);
		}

}
