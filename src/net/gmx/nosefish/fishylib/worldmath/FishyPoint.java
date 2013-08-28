package net.gmx.nosefish.fishylib.worldmath;

public abstract class FishyPoint implements IFishyPoint{

	public abstract int getIntX();
	public abstract int getIntY();
	public abstract int getIntZ();

	public abstract double getDoubleX();
	public abstract double getDoubleY();
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
