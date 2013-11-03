package net.gmx.nosefish.fishylib.worldmath;

import net.canarymod.api.world.Chunk;

/**
 * 
 * @author Stefan Steinheimer
 *
 */
public class FishyChunk extends FishyLocationInt {
	
	public FishyChunk(Chunk chunk) {
		this(FishyWorld.factory(chunk.getDimension()), chunk.getX(), chunk.getZ());
	}
	
	public FishyChunk(FishyWorld world, int chunkX, int chunkZ) {
		super(world,
				chunkToWorld(chunkX),
				0,
				chunkToWorld(chunkZ));
	}
	
	public FishyChunk(FishyWorld world, int worldX, int worldY, int worldZ) {
		super(world,
				chunkOriginOf(worldX),
				0,
				chunkOriginOf(worldZ));
	}
	
	public boolean contains(FishyLocationInt location) {
		return this.getWorld().equals(location.getWorld())
				&& worldToChunk(location.getIntX()) == this.getChunkX()
				&& worldToChunk(location.getIntZ()) == this.getChunkZ();
	}
	
	public int getChunkX() {
		return worldToChunk(this.getIntX());
	}
	
	public int getChunkZ() {
		return worldToChunk(this.getIntZ());
	}
	
	@Override
	public String toString(){
		return "FishyChunk <"
				+ this.getChunkX()
				+ ", "
				+ this.getChunkZ()
				+ "> in world "
				+ this.getWorld().toString();
	}
	
	/**
	 * Converts Minecraft chunk coordinates to world coordinates
	 * 
	 * @param i
	 * @return
	 */
	public static int chunkToWorld(int i) {
		return i << 4;
	}
	
	/**
	 * Converts world coordinates to Minecraft chunk coordinates
	 * 
	 * @param i
	 * @return
	 */
	public static int worldToChunk(int i) {
		return i >> 4;
	}
	
	
	/**
	 * Gets the origin of the chunk (in world coordinates) that
	 * contains the coordinate.
	 * 
	 * @param i
	 * @return
	 */
	public static int chunkOriginOf(int i) {
		return chunkToWorld(worldToChunk(i));
	}
	
	// TODO: why the factory method? convert to constructor
	public static FishyChunk getChunkContaining(FishyLocation location) {
		return new FishyChunk(location.getWorld(),
				location.getIntX(),
				location.getIntY(),
				location.getIntZ());
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		if (other.getClass() != FishyChunk.class) {
			return false;
		}
		return this.equalsLocation((FishyChunk)other);
	}
	
	@Override
	public int hashCode() {
		return this.getPoint().hashCode() * 89 + this.getWorld().hashCode();
	}
}
