package net.gmx.nosefish.fishylib.worldmath;

public class FishyCuboidInt {
	protected final FishyWorld world;
	protected final FishyPointInt lowPoint;
	protected final FishyPointInt highPoint;
	
	public FishyCuboidInt(FishyWorld world, FishyPointInt point1, FishyPointInt point2) {
		this.world = world;
		int[] lowPoint = point1.toIntArray();
		int[] highPoint = point2.toIntArray();
		for (int i = 0; i < 3; i++) {
			int t;
			if (lowPoint[i] > highPoint[i]) {
				t = lowPoint[i];
				lowPoint[i] = highPoint[i];
				highPoint[i] = t;
			}
		}
		this.lowPoint = new FishyPointInt(lowPoint[0], lowPoint[1], lowPoint[2]);
		this.highPoint = new FishyPointInt(highPoint[0], highPoint[1], highPoint[2]);
	}

	/**
	 * @return the world
	 */
	public FishyWorld getWorld() {
		return world;
	}

	/**
	 * lowPoint.getIntX/Y/Z <= highPoint.getIntX/Y/Z
	 * 
	 * @return the lowPoint
	 */
	public FishyPointInt getLowPoint() {
		return lowPoint;
	}

	/**
	 * lowPoint.getIntX/Y/Z <= highPoint.getIntX/Y/Z
	 * 
	 * @return the highPoint
	 */
	public FishyPointInt getHighPoint() {
		return highPoint;
	}
	
	public int getVolume() {
		return (highPoint.getIntX() - lowPoint.getIntX() + 1)
		     * (highPoint.getIntY() - lowPoint.getIntY() + 1)
		     * (highPoint.getIntZ() - lowPoint.getIntZ() + 1);
	}
	
	public boolean containsLocation(FishyLocation location) {
		return (location.getWorld().equals(world) &&
				lowPoint.getIntX() <= location.getIntX() &&
				highPoint.getIntX() >= location.getIntX() &&
				lowPoint.getIntY() <= location.getIntZ() &&
				highPoint.getIntY() >= location.getIntZ() &&
				lowPoint.getIntZ() <= location.getIntZ() &&
				highPoint.getIntZ() >= location.getIntZ());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((highPoint == null) ? 0 : highPoint.hashCode());
		result = prime * result
				+ ((lowPoint == null) ? 0 : lowPoint.hashCode());
		result = prime * result + ((world == null) ? 0 : world.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FishyCuboidInt other = (FishyCuboidInt) obj;
		if (highPoint == null) {
			if (other.highPoint != null) {
				return false;
			}
		} else if (!highPoint.equals(other.highPoint)) {
			return false;
		}
		if (lowPoint == null) {
			if (other.lowPoint != null) {
				return false;
			}
		} else if (!lowPoint.equals(other.lowPoint)) {
			return false;
		}
		if (world == null) {
			if (other.world != null) {
				return false;
			}
		} else if (!world.equals(other.world)) {
			return false;
		}
		return true;
	}

	
}
