package net.gmx.nosefish.fishylib.blocks;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import net.canarymod.api.world.blocks.BlockType;

public class StorageBlocks {
		static final SortedSet<Short> ids; 
		private static Short[] idArray = new Short[] {
			BlockType.Chest.getId(),
			BlockType.TrappedChest.getId(),
			BlockType.Dispenser.getId(),
			BlockType.Dropper.getId(),
			BlockType.Hopper.getId(),
			// version 1.6.2
		};
		static {
			TreeSet <Short> tmpSet = new TreeSet<Short>();
			Collections.addAll(tmpSet, idArray);
			ids = Collections.unmodifiableSortedSet(tmpSet);
			idArray = null;
		}

}
