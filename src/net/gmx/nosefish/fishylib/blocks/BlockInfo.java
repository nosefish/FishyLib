package net.gmx.nosefish.fishylib.blocks;

import java.util.EnumSet;
import java.util.Set;


import net.canarymod.api.world.World;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.blocks.BlockMaterial;
import net.canarymod.api.world.blocks.BlockType;
import net.gmx.nosefish.fishylib.worldmath.FishyDirection;
import net.gmx.nosefish.fishylib.worldmath.FishyLocationInt;
import net.gmx.nosefish.fishylib.worldmath.FishyVectorDouble;
import net.gmx.nosefish.fishylib.worldmath.FishyVectorInt;

public class BlockInfo {

			
	public static FishyDirection getSignPostDirection(int dataValue) {
		switch (dataValue) {
		case 0x0: return FishyDirection.SOUTH;
		case 0x1: return FishyDirection.SOUTH_SOUTHWEST; // south-southwest, missing in Canary Direction enum
		case 0x2: return FishyDirection.SOUTHWEST;
		case 0x3: return FishyDirection.WEST_SOUTHWEST; // west-southwest, missing in Canary Direction enum
		case 0x4: return FishyDirection.WEST;
		case 0x5: return FishyDirection.WEST_NORTHWEST; // west-northwest, missing in Canary Direction enum
		case 0x6: return FishyDirection.NORTHWEST;
		case 0x7: return FishyDirection.NORTH_NORTHWEST; // north-northwest, missing in Canary Direction enum
		case 0x8: return FishyDirection.NORTH;
		case 0x9: return FishyDirection.NORTH_NORTHEAST; // north-northeast, missing in Canary Direction enum
		case 0xA: return FishyDirection.NORTHEAST;
		case 0xB: return FishyDirection.EAST_NORTHEAST; // east-northeast, missing in Canary Direction enum
		case 0xC: return FishyDirection.EAST;
		case 0xD: return FishyDirection.EAST_SOUTHEAST; // east-southeast, missing in Canary Direction enum
		case 0xE: return FishyDirection.SOUTHEAST;
		case 0xF: return FishyDirection.SOUTH_SOUTHEAST; // south-southeast, missing in Canary Direction enum
		default: return FishyDirection.ERROR; // invalid datavalue for signpost
		}
	}
	
	public static FishyDirection getWallSignDirection(int dataValue) {
		switch (dataValue) {
		case 0x2: return FishyDirection.NORTH; // Facing north (for ladders and signs, attached to the north side of a block)
		case 0x3: return FishyDirection.SOUTH; // Facing south
		case 0x4: return FishyDirection.WEST; // Facing west
		case 0x5: return FishyDirection.EAST; //Facing east
		default: return FishyDirection.ERROR; // invalid datavalue for WallSign
		}
	}
	
	public static FishyDirection getSignDirection(int blockId, int dataValue) {
		if (blockId == BlockType.WallSign.getId()) {
			return getWallSignDirection(dataValue);
		}
		if (blockId == BlockType.SignPost.getId()) {
			return getSignPostDirection(dataValue);
		}
		return null;
	}
	
	public static int getRedstonePower(int blockId, int dataValue) {
		RedstonePowerSource powerType = RedstonePowerSource.fromId(blockId);
		if (powerType == null) {
			// not a redstone block
			return 0;
		}
		if (RedstonePowerSource.powerIsAlwaysOn.contains(powerType)) {
			return 15;
		}
		if (RedstonePowerSource.powerIsAlwaysOff.contains(powerType)) {
			return 0;
		}
		if (RedstonePowerSource.powerIsDataValue.contains(powerType)) {
			return dataValue;
		}
		if (RedstonePowerSource.powerOnOffInBit0x1.contains(powerType)) {
			return ((dataValue & 0x1) == 0 ? 0 : 15);
		}
		if (RedstonePowerSource.powerOnOffInBit0x8.contains(powerType)) {
			return ((dataValue & 0x8) == 0 ? 0 : 15);
		}
		return 0;
	}
	
	public static FishyDirection getLadderDirection(int dataValue) {
		return getWallSignDirection(dataValue);
	}
	
	public static FishyDirection getFurnaceDirection(int dataValue) {
		return getWallSignDirection(dataValue);
	}
	
	public static FishyDirection getChestDirection(int dataValue) {
		return getWallSignDirection(dataValue);
	}

	public static FishyDirection getRepeaterDirection(int dataValue) {
		int dirValue = dataValue & 0x3; // direction is in the lower 2 bits
		switch (dirValue) {
		case 0x0: return FishyDirection.NORTH; // Facing north
		case 0x1: return FishyDirection.EAST; // Facing east
		case 0x2: return FishyDirection.SOUTH; // Facing south
		case 0x3: return FishyDirection.WEST; // Facing west
		default: return FishyDirection.ERROR; // wtf?
		}
	}
	
	public static Set<FishyDirection> getRedstoneWireConnections(FishyLocationInt location) {
		World world = location.getWorld().getWorldIfLoaded();
		if (world == null) {
			return null;
		}
		if (! world.isChunkLoaded(
				location.getIntX(),
				location.getIntY(), 
				location.getIntZ())) {
			return null;
		}
		Block block = world.getBlockAt(
				location.getIntX(),
				location.getIntY(), 
				location.getIntZ());
		return getRedstoneWireConnections(block);
	}
	
	public static Set<FishyDirection> getRedstoneWireConnections(Block wire) {
		if (wire.getType().getId() != BlockType.RedstoneWire.getId()) {
			return null;
		}
		// rules to check: 
		// 1) 4 surrounding blocks on same level: if redstone connector block, add direction.
		// 2) 4 surrounding blocks on level above: if redstone wire && block directly above this not opaque, add direction
		// 3) 4 surrounding blocks on level below: if redstone wire && block directly above them not opaque, add direction
		// 4) if only 1 direction, add opposite direction
		// 5) if no direction, add all directions
		Set<FishyDirection> outSet = EnumSet.noneOf(FishyDirection.class);
		Block blockAboveWire = wire.getRelative(0, 1, 0);
		for (FishyDirection direction : FishyDirection.cardinalDirections) {
			Block toCheck = getBlockInDirection(wire, direction, 1);
			// check rule 1)
			if (isRedstoneConnector(toCheck.getTypeId(), toCheck.getData(), direction.opposite())) {
				//System.out.println(direction + " matches 1");
				outSet.add(direction); // matches 1)
				continue;
			}
			// check rule 2)
			BlockMaterial material = null;
			try {
				material = blockAboveWire.getBlockMaterial();
			} catch (NullPointerException e) {
				// TODO: workaround for Canary bug. Remove when fixed.
				// TODO: figure out Canary bug and report/fix
			}
			if (material == null || ! material.isOpaque()) {
				Block aboveToCheck = toCheck.getRelative(0, 1, 0);
				if (aboveToCheck.getTypeId() == BlockType.RedstoneWire.getId()) {
					//System.out.println(direction + " matches 2");
					outSet.add(direction); // matches 2)
					continue;
				}
			}
			// check rule 3)
			try {
				material = toCheck.getBlockMaterial();
			} catch (NullPointerException e) {
				//TODO: workaround for Canary bug. Remove when fixed.
			}
			if (material == null || ! material.isOpaque()) {
				Block belowToCheck = toCheck.getRelative(0, -1, 0);
				if (belowToCheck.getTypeId() == BlockType.RedstoneWire.getId()) {
					//System.out.println(direction + " matches 3");
					outSet.add(direction); // matches 3)
					//continue;
				}
			}
		}
		// check rule 4)
		if (outSet.size() == 1) {
			FishyDirection direction = (FishyDirection) (outSet.toArray())[0];
			//System.out.println(direction + " matches 4");
			outSet.add(direction.opposite()); // 4)
		}
		// check rule 5
		if (outSet.isEmpty()) {
			//System.out.println("matches 5 (no direction)");
			outSet = EnumSet.copyOf(FishyDirection.cardinalDirections);
		}
		return outSet;
	}
	
	public static boolean isRedstoneConnector(short blockTypeID, short blockData, FishyDirection direction) {
		RedstonePowerSource powerType = RedstonePowerSource.fromId(blockTypeID);
		if (powerType != null) {
			if (RedstonePowerSource.directionalRSBlocks.contains(powerType)) {
				// luckily, all directional redstone blocks work like repeaters
				return getRepeaterDirection(blockData).equals(direction);
			} else {
				// non-directional
				return true;
			}
		}
		return false;
	}
	
	public static boolean isDirectInput(FishyLocationInt inputLocation,
	                                               short blockTypeID,
	                                               short blockData,
	                                    FishyLocationInt target) {
		
		if (! inputLocation.getWorld().equals(target.getWorld())) {
			// Are you kidding? They're not even in the same world!
			//System.out.println("different world");
			return false;
		}
		FishyVectorInt in2target = new FishyVectorInt(inputLocation.getVectorTo(target));
		if (in2target.lengthSquared() != 1.0D) {
			// Not adjacent. What a waste of time.
			//System.out.println("not adjacent");
			return false;
		}
		FishyDirection connectionDirection = in2target.getCardinalDirection();
		if (connectionDirection == FishyDirection.ERROR) {
			// invalid direction
			//System.out.println("invalid direction");
			return false;
		}
		if (blockTypeID == BlockType.RedstoneWire.getId()) {
			//System.out.println("is redstone wire");
			Set<FishyDirection> directions = BlockInfo.getRedstoneWireConnections(inputLocation);
			return (directions == null) ? false : directions.contains(connectionDirection);
		}
		//System.out.println("not redstone wire");
		return BlockInfo.isRedstoneConnector(blockTypeID, blockData, connectionDirection);
	}
	
	public static boolean canPlayerPassThrough(int blockId) {
		return BlocksPlayersCanPass.ids.contains(new Integer(blockId));
	}
	
	public static boolean isStorageBlock(int blockId) {
		return StorageBlocks.ids.contains(new Integer(blockId));
	}
	
	
	// this doesn't belong here - doesn't provide information about a block.
	/**
	 * Make sure that the target chunk is loaded!
	 * 
	 * @param reference
	 * @param direction
	 * @param distance
	 * @return
	 */
	public static Block getBlockInDirection(Block reference, FishyDirection direction, int distance) {
		int[] dVec = {0, 0, 0};
		switch(direction) {
		case NORTH: 
			dVec[2] = -distance; // -Z
			break;
		case EAST:
			dVec[0] = distance; // +X
			break;
		case SOUTH:
			dVec[2] = distance; // +Z
			break;
		case WEST:
			dVec[0] = - distance; // -X
			break;
		case UP:
			dVec[1] = distance;
			break;
		case DOWN:
			dVec[1] = - distance;
			break;
		default:
			FishyVectorDouble directionVector = direction.toUnitDoubleVector().scalarMult(distance);
			dVec[0] = directionVector.getIntX();
			dVec[1] = directionVector.getIntY();
			dVec[2] = directionVector.getIntZ();
		}
		return reference.getRelative(dVec[0], dVec[1], dVec[2]);
	}
	
//	the x-axis indicates the player's distance east (positive) or west (negative) of the origin point—i.e., the longitude,
//	the z-axis indicates the player's distance south (positive) or north (negative) of the origin point—i.e., the latitude,
//	the y-axis indicates how high or low (from 0 to 255, with 64 being sea level) the player is—i.e., the elevation,
//	thereby forming a right-handed coordinate system (thumb≡x, index≡y, middle≡z), making it easy to remember which axis is which.
//	Additionally, the f number in the debug screen tells you which direction the player is currently facing. 0 equals due south. Thus:
//	0 = South
//	1 = West
//	2 = North
//	3 = East
}
