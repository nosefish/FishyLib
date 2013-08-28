package net.gmx.nosefish.fishylib.worldmath;

/**
 * 3D vector.
 * 
 * @author Stefan Steinheimer
 *
 */
public abstract class FishyVector {

		public abstract int getIntX();
		public abstract int getIntY();
		public abstract int getIntZ();

		public abstract double getDoubleX();
		public abstract double getDoubleY();
		public abstract double getDoubleZ();
		
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

		public abstract FishyVector add(FishyVector other);
		public abstract FishyVector scalarMult(double scalar);
		
		public double lengthSquared() {
			return    this.getDoubleX() * this.getDoubleX()
					+ this.getDoubleY() * this.getDoubleY()
					+ this.getDoubleZ() * this.getDoubleZ();
		}
		
		public double length() {
			return Math.sqrt(this.lengthSquared());
		}
		
		public FishyVector normalized() {
			return this.scalarMult(1.0/this.length());
		}
		
		/**
		 * Relative to negative Z axis (NORTH), clockwise
		 * for ease of use with FishyDirection
		 * 
		 * @return
		 */
		public double getXZDirectionRadians() {
			return Math.atan2(this.getDoubleX(), (-this.getDoubleZ()));
		}
		
		/**
		 * Relative to negative Z axis (NORTH), clockwise
		 * for ease of use with FishyDirection
		 * 
		 * @return
		 */
		public double getXZDirectionDegrees() {
			return Math.toDegrees(getXZDirectionRadians());
		}
		
		/**
		 * Equality check. Unlike <code>equals</code>,
		 * <code>a.equalsVec(b)</code> does not imply that
		 * <code>a.hashCode() == b.hashCode()</code>.
		 * 
		 * @param other
		 * @return
		 *     <code>true</code> if the double representations of the
		 *     vectors are equal, <code>false</code> otherwise.
		 */
		public final boolean equalsVec(FishyVector other) {
			if (this == other) {
				return true;
			}
			if (other == null) {
				return false;
			}
			return (this.getDoubleX() == other.getDoubleX())
					&& this.getDoubleY() == other.getDoubleY()
					&& this.getDoubleZ() == other.getDoubleZ();
		}
		
		@Override
		public String toString() {
			return "("
					+ this.getDoubleX()
					+ ", "
					+ this.getDoubleY()
					+ ", "
					+ this.getDoubleZ()
					+ ")";
		}
}
