package net.gmx.nosefish.fishylib.worldmath;

public class FishyLocationDouble extends FishyLocation {
	private final FishyWorld world;
	private final FishyPointDouble point;
	
	public FishyLocationDouble(FishyWorld world, double x, double y, double z) {
		this.point = new FishyPointDouble(x, y, z);
		if (world != null) {
			this.world = world;
		} else {
			throw new NullPointerException("Parameter world was null.");
		}
	}
	
	public FishyLocationDouble(FishyWorld world, FishyPoint point) {
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
	public FishyPoint getPoint(){
		return this.point;
	}
	
	@Override
	public FishyLocationDouble addVector(FishyVector vector) {
		return new FishyLocationDouble(this.world, this.point.addVector(vector));
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		if (other.getClass() != FishyLocationDouble.class) {
			return false;
		}
		return this.equalsLocation((FishyLocationDouble)other);
	}
	
	@Override
	public int hashCode() {
		return this.point.hashCode() * 89 + this.getWorld().hashCode();
	}


	


}
