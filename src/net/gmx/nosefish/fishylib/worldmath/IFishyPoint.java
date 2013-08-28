package net.gmx.nosefish.fishylib.worldmath;

public interface IFishyPoint {
		public int getIntX();
		public int getIntY();
		public int getIntZ();
		
		public double getDoubleX();
		public double getDoubleY();
		public double getDoubleZ();
		
		public boolean equalsPoint(IFishyPoint other);
}
