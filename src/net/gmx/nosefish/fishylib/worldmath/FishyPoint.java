package net.gmx.nosefish.fishylib.worldmath;

public abstract class FishyPoint implements IFishyPoint{

    @Override
	public abstract int getIntX();
    @Override
	public abstract int getIntY();
    @Override
	public abstract int getIntZ();

    @Override
	public abstract double getDoubleX();
    @Override
	public abstract double getDoubleY();
    @Override
	public abstract double getDoubleZ();

	public abstract FishyPoint addVector(FishyVector vector);
	
	public int[] toIntArray() {
		return new int[]{
				   this.getIntX(),
				   this.getIntY(),
				   this.getIntZ()
				   };
	}

	public double[] toDoubleArray() {
		return new double[]{
				   this.getDoubleX(),
				   this.getDoubleY(),
				   this.getDoubleZ()
				   };
	}

	public FishyVector getVectorTo(IFishyPoint point) {
		return new FishyVectorDouble(
				point.getDoubleX() - this.getDoubleX(),
				point.getDoubleY() - this.getDoubleY(),
				point.getDoubleZ() - this.getDoubleZ());
	}
	

    @Override
	public final boolean equalsPoint(IFishyPoint other) {
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		return (this.getDoubleX() == other.getDoubleX()
		     && this.getDoubleY() == other.getDoubleY()
		     && this.getDoubleZ() == other.getDoubleZ() 
		);
	}
	
	@Override
	public String toString() {
		return "<"
		       + this.getIntX()
		       + ", "
		       + this.getIntY()
		       + ", "
		       + this.getIntZ()
		       + ">";
	}

}
