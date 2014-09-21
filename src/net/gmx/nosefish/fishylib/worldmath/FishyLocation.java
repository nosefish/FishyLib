package net.gmx.nosefish.fishylib.worldmath;

public abstract class FishyLocation extends FishyPoint implements IFishyLocation {

    /**
     *
     * @return
     */
    @Override
	public abstract FishyWorld getWorld();
	public abstract FishyPoint getPoint();
	
	@Override
	public boolean equalsLocation(IFishyLocation location) {
		if (this == location) {
			return true;
		}
		if (! super.equalsPoint(location)) {
			return false;
		}
		return location.getWorld().equals(this.getWorld());
	}
	
	@Override
	public String toString(){
		return "["
				+this.getWorld().toString()
				+": "
				+ super.toString()
				+ "]"; 
	}
}
