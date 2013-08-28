package net.gmx.nosefish.fishylib.worldmath;

public interface IFishyLocation extends IFishyPoint{
	public FishyWorld getWorld();
	public boolean equalsLocation(IFishyLocation location);
}
