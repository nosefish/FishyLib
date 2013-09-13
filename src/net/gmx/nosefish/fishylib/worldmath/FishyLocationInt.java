package net.gmx.nosefish.fishylib.worldmath;

import net.canarymod.api.world.position.Location;

public class FishyLocationInt extends FishyLocation{
	private final FishyWorld world;
	private final FishyPointInt point;
	
	public FishyLocationInt(Location canaryLocation) {
		point = new FishyPointInt(canaryLocation.getBlockX(),
				canaryLocation.getBlockY(),
				canaryLocation.getBlockZ());
		this.world = FishyWorld.factory(canaryLocation.getWorldName(), canaryLocation.getType());
	}
	
	public FishyLocationInt(FishyWorld world, int x, int y, int z) {
		point = new FishyPointInt(x, y, z);
		if (world != null) {
			this.world = world;
		} else {
			throw new NullPointerException("Parameter world was null.");
		}
	}
	
	public FishyLocationInt(FishyWorld world, FishyPoint point) {
		this(world,
				point.getIntX(),
				point.getIntY(),
				point.getIntZ());		
	}
	
	@Override
	public int getIntX() {
		return point.getIntX();
	}

	@Override
	public int getIntY() {
		return point.getIntY();
	}

	@Override
	public int getIntZ() {
		return point.getIntZ();
	}

	@Override
	public double getDoubleX() {
		return point.getDoubleX();
	}

	@Override
	public double getDoubleY() {
		return point.getDoubleY();
	}

	@Override
	public double getDoubleZ() {
		return point.getDoubleZ();
	}
	
	@Override
	public FishyWorld getWorld() {
		return this.world;
	}
	
	@Override
	public FishyPoint getPoint() {
		return this.point;
	}
	
	public FishyLocationInt addIntVector(FishyVectorInt vector) {
		return new FishyLocationInt(this.world, point.addIntVector(vector));
	}
	
	@Override
	public FishyLocationDouble addVector(FishyVector vector) {
		return new FishyLocationDouble(this.world, point.addVector(vector));
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		if (other.getClass() != FishyLocationInt.class) {
			return false;
		}
		return this.equalsLocation((FishyLocationInt)other);
	}
	
	@Override
	public int hashCode() {
		return this.point.hashCode() * 89 + this.getWorld().hashCode();
	}
}
