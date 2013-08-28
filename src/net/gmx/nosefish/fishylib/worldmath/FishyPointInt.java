package net.gmx.nosefish.fishylib.worldmath;

public class FishyPointInt extends FishyPoint{
	public static final FishyPointInt ORIGIN = new FishyPointInt(0, 0, 0);
	final FishyVectorInt coordinates;
	
	public FishyPointInt(int x, int y, int z) {
		this.coordinates = new FishyVectorInt(x, y, z);
	}
	
	public FishyPointInt(IFishyPoint point) {
		this.coordinates = new FishyVectorInt(
				point.getIntX(),
				point.getIntY(),
				point.getIntZ()
				);
	}
	
	private FishyPointInt(FishyVectorInt vector) {
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
		return FishyPointDouble.ORIGIN.addVector(this.coordinates.add(vector));
	}
	
	public FishyPointInt addIntVector(FishyVectorInt vector) {
		return new FishyPointInt(this.coordinates.addInt(vector));
	}
	
	public int[] toIntArray() {
		return coordinates.toIntArray();
	}
	
	public double[] toDoubleArray() {
		return coordinates.toDoubleArray();
	}
	
	public FishyVectorInt getIntVectorTo(FishyPointInt point) {
		return new FishyVectorInt(
				point.getIntX() - this.getIntX(),
				point.getIntY() - this.getIntY(),
				point.getIntZ() - this.getIntZ()
				);
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other.getClass() == FishyPointInt.class) {
			FishyPointInt oVec = (FishyPointInt) other;
			return this.getIntX() == oVec.getIntX()
			    && this.getIntY() == oVec.getIntY()
			    && this.getIntZ() == oVec.getIntZ();
		}
		return false;
	}
	
	
	@Override
	public int hashCode() {
		return coordinates.hashCode();
	}
}
