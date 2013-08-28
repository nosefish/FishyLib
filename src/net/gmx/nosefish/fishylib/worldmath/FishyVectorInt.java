package net.gmx.nosefish.fishylib.worldmath;

import java.util.Arrays;

public class FishyVectorInt extends FishyVector {
	final int[] vec;
	
	public FishyVectorInt(int x, int y, int z) {
		this.vec = new int[] {x,y,z};
	}
	
	public FishyVectorInt(FishyVector vec) {
		this.vec = new int[] {
				       vec.getIntX(),
				       vec.getIntY(),
				       vec.getIntZ()
				       };
	}
	
	public int getIntX() {
		return vec[0];
	}
	
	public int getIntY() {
		return vec[1];
	}
	
	public int getIntZ() {
		return vec[2];
	}
	
	@Override
	public double getDoubleX() {
		return (double) vec[0];
	}

	@Override
	public double getDoubleY() {
		return (double) vec[1];
	}

	@Override
	public double getDoubleZ() {
		return (double) vec[2];
	}

	@Override
	public int[] toIntArray() {
		return Arrays.copyOf(vec, vec.length);
	}

	/**
	 * Adds the other vector as an int vector truncated to
	 * block coordinates.
	 * 
	 * @param other the vector to add
	 * @return the sum of the vectors
	 */
	public FishyVectorInt addInt(FishyVector other) {
		return new FishyVectorInt(
				   this.getIntX() + other.getIntX(),
				   this.getIntY() + other.getIntY(),
				   this.getIntZ() + other.getIntZ()
				   );
	}
	
	@Override
	public FishyVectorDouble add(FishyVector other) {
		return new FishyVectorDouble(
				   this.getDoubleX() + other.getDoubleX(),
				   this.getDoubleY() + other.getDoubleY(),
				   this.getDoubleZ() + other.getDoubleZ()
				   );
	}	
	
	public FishyVectorInt scalarIntMult(int scalar) {
		return new FishyVectorInt(
				   getIntX() * scalar,
				   getIntY() * scalar,
				   getIntZ() * scalar
				   );
	}
	
	@Override
	public FishyVectorDouble scalarMult(double scalar) {
		return new FishyVectorDouble(getDoubleX() * scalar,  getDoubleY() * scalar, getDoubleZ() * scalar);
	}

	@Override
	public double lengthSquared() {
		return    this.getIntX() * this.getIntX()
				+ this.getIntY() * this.getIntY()
				+ this.getIntZ() * this.getIntZ();
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(vec);
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other.getClass() == FishyVectorInt.class) {
			FishyVectorInt oVec = (FishyVectorInt) other;
			return     this.getIntX() == oVec.getIntX()
					&& this.getIntY() == oVec.getIntY()
					&& this.getIntZ() == oVec.getIntZ();
		}
		return false;
	}
	
	public FishyDirection getCardinalDirection() {
		if (getIntX() == 0 && getIntY() == 0 && getIntZ() < 0)
			return FishyDirection.NORTH;
		if (getIntX() == 0 && getIntY() == 0 && getIntZ() > 0)
			return FishyDirection.SOUTH;
		if (getIntX() > 0 && getIntY() == 0 && getIntZ() == 0)
			return FishyDirection.EAST;
		if (getIntX() < 0 && getIntY() == 0 && getIntZ() == 0)
			return FishyDirection.WEST;
		return FishyDirection.ERROR;
	}

	public FishyDirection getNearestFishyDirection() {
		return FishyDirection.nearestDirection(Math.toDegrees(getXZDirectionRadians()));
	}
}
