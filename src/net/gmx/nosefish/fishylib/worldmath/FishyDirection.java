package net.gmx.nosefish.fishylib.worldmath;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import net.canarymod.api.world.position.Direction;

public enum FishyDirection {
	NORTH(0.0),
	NORTH_NORTHEAST(22.5),
	NORTHEAST(45.0), 
	EAST_NORTHEAST(67.5),
	EAST(90.0),
	EAST_SOUTHEAST(112.5),
	SOUTHEAST(135.0),
	SOUTH_SOUTHEAST(157.5),
	SOUTH(180.0),
	SOUTH_SOUTHWEST(202.5),
	SOUTHWEST(225.0),
	WEST_SOUTHWEST(247.5),
	WEST(270.0),
	WEST_NORTHWEST(292.5),
	NORTHWEST(315.0),
	NORTH_NORTHWEST(337.5),
	UP(null),
	DOWN(null),
	ERROR(null);
	
	public static final Set<FishyDirection> cardinalDirections =
			Collections.unmodifiableSet(EnumSet.of(NORTH, EAST, SOUTH, WEST));
	public static final Set<FishyDirection> ordinalDirections =
			Collections.unmodifiableSet(EnumSet.of(NORTHEAST, SOUTHEAST, SOUTHWEST, NORTHWEST));
	
	// private fields
	private final Double degrees;
	private static final NavigableMap<Double, FishyDirection> index =
			new TreeMap<>();
	static {
		for (FishyDirection dir : FishyDirection.values()) {
			if (dir.degrees != null) {
				index.put(dir.degrees, dir);
			}
		}
	}
	
	private FishyDirection(Double degrees) {
		this.degrees = degrees;
	}
	
	// Canary uses the old direction system: 
	// Canary North = +X, real North is -Z, +X is East
	// Canary = Fishy - 90° 
	// TODO: check, I may be lying!
	public Direction toCanaryDirection() {
		Direction dir;
		try {
			dir = Direction.valueOf(nearestDirection(degrees - 90.0).toString());
		} catch (IllegalArgumentException e){
			dir = Direction.ERROR;
		}
		return dir;
	}
	
	public FishyDirection opposite() {
		switch(this) {
		case UP: return DOWN;
		case DOWN: return UP;
		case ERROR: return ERROR;
		default:
			double opposite = (this.degrees + 180.0);
			opposite = opposite < 360.0 ? opposite : opposite - 360.0;
			return index.get(opposite);
		}
	}
	
	public double toDegrees() {
		return this.degrees;
	}
	
	public double toRadians() {
		return Math.toRadians(this.degrees.doubleValue());
	}
	
	public FishyVectorDouble toUnitDoubleVector() {
		switch (this) {
		case UP: return new FishyVectorDouble(0.0, 1.0, 0.0);
		case DOWN: return new FishyVectorDouble(0.0, -1.0, 0.0);
		case ERROR: return null;
		// TODO: complete trivial cases, add constants to FishyVectorDouble
		default:
			double rad = this.toRadians();
			return new FishyVectorDouble(Math.sin(rad), 0.0, - Math.cos(rad));
		}
	}
	
	public FishyVectorInt toUnitIntVector() {
		switch (this) {
		case UP   : return FishyVectorInt.UNIT_Y;
		case DOWN : return FishyVectorInt.UNIT_MINUS_Y;
		case NORTH: return FishyVectorInt.UNIT_MINUS_Z;
		case EAST : return FishyVectorInt.UNIT_X;
		case SOUTH: return FishyVectorInt.UNIT_Z;
		case WEST : return FishyVectorInt.UNIT_MINUS_X;
		default: return null;
		}
	}
	
	public static double normalizeDegrees(double degrees) {
		double clamped = degrees % 360F;
		clamped = (clamped >= 0F) ? clamped : (360F - clamped);
		return clamped;
	}
	
	public static FishyDirection nearestDirection(double degrees) {
		// normalize to value range
		double clamped = normalizeDegrees(degrees);
		// clamped >= 0 => this is never null
		Map.Entry<Double, FishyDirection> floorEntry = index.floorEntry(clamped);
		// granularity is 22.5°
		if (clamped - floorEntry.getKey() >= 11.25) {
			// this will return null for clamped >= 337.5
			Map.Entry<Double, FishyDirection> ceilingEntry = index.ceilingEntry(clamped);
			return (ceilingEntry == null) ? NORTH : ceilingEntry.getValue();
		} else {
			return floorEntry.getValue();
		}		
	}
}
