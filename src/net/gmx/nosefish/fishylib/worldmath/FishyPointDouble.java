package net.gmx.nosefish.fishylib.worldmath;

public class FishyPointDouble extends FishyPoint {
	public static final FishyPointDouble ORIGIN = new FishyPointDouble(0, 0, 0);
	final FishyVectorDouble coordinates;
	
	public FishyPointDouble(double x, double y, double z) {
		this.coordinates = new FishyVectorDouble(x, y, z);
	}
	
	private FishyPointDouble(FishyVectorDouble vector) {
		this.coordinates = vector;
	}
	
	@Override
	public int getIntX() {
		return coordinates.getIntX();
	}
	
	@Override
	public int getIntY() {
		return coordinates.getIntY();
	}
	
	@Override
	public int getIntZ() {
		return coordinates.getIntZ();
	}
	
	@Override
	public double getDoubleX() {
		return coordinates.getDoubleX();
	}

	@Override
	public double getDoubleY() {
		return coordinates.getDoubleY();
	}

	@Override
	public double getDoubleZ() {
		return coordinates.getDoubleZ();
	}

	@Override
	public FishyPointDouble addVector(FishyVector vector) {
		return new FishyPointDouble(this.coordinates.add(vector));
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other.getClass() == FishyPointDouble.class) {
			FishyPointDouble oVec = (FishyPointDouble) other;
			return this.getDoubleX() == oVec.getDoubleX()
			    && this.getDoubleY() == oVec.getDoubleY()
			    && this.getDoubleZ() == oVec.getDoubleZ();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return coordinates.hashCode();
	}
}
