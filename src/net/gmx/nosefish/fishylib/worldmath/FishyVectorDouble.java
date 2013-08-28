package net.gmx.nosefish.fishylib.worldmath;

import java.util.Arrays;

import net.canarymod.ToolBox;

public class FishyVectorDouble extends FishyVector{
	final double[] vec;
	
	public FishyVectorDouble(double x, double y, double z) {
		this.vec = new double[] {x, y, z};
	}
	
	public FishyVectorDouble(FishyVector vec) {
		this.vec = new double[] {
				       vec.getDoubleX(),
					   vec.getDoubleY(),
					   vec.getDoubleZ()
					   };
	}
	
	@Override
	public int getIntX() {
		return ToolBox.floorToBlock(vec[0]);
	}
	
	@Override
	public int getIntY() {
		return ToolBox.floorToBlock(vec[1]);
	}
	
	@Override
	public int getIntZ() {
		return ToolBox.floorToBlock(vec[2]);
	}
	
	@Override
	public double getDoubleX() {
		return vec[0];
	}
	
	@Override
	public double getDoubleY() {
		return vec[1];
	}
	
	@Override
	public double getDoubleZ() {
		return vec[2];
	}
	


	@Override
	public double[] toDoubleArray() {
		return Arrays.copyOf(vec, vec.length);
	}
	
	@Override
	public FishyVectorDouble add(FishyVector other) {
		return new FishyVectorDouble(
				this.getDoubleX() + other.getDoubleX(),
				this.getDoubleY() + other.getDoubleY(),
				this.getDoubleZ() + other.getDoubleZ()
				);
	}
	
	public FishyVectorDouble scalarMult(int scalar) {
		return this.scalarMult((double)scalar);
	}
	
	public FishyVectorDouble scalarMult(double scalar) {
		return new FishyVectorDouble(
				scalar * this.getDoubleX(),
				scalar * this.getDoubleY(),
				scalar * this.getDoubleZ()
				);
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(vec);
	}

	public boolean equals (Object other) {
		if (this == other) {
			return true;
		}
		if (other.getClass() == FishyVectorDouble.class) {
			FishyVectorDouble oVec = (FishyVectorDouble) other;
			return     this.getDoubleX() == oVec.getDoubleX()
					&& this.getDoubleY() == oVec.getDoubleY()
					&& this.getDoubleZ() == oVec.getDoubleZ();
		}
		return false;
	}




	

}

