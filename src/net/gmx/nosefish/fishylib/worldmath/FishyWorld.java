package net.gmx.nosefish.fishylib.worldmath;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.canarymod.Canary;
import net.canarymod.api.world.DimensionType;
import net.canarymod.api.world.World;
import net.canarymod.api.world.WorldManager;

/**
 * Canary.getServer().getWorld(String) is slow, as are the
 * other methods for getting worlds from names in WorldManager.
 * Use this instead.
 * <p> 
 * All world names must be of the form worldname_DIMENSION. Names
 * without the _DIMENSION part are seen as referring to the
 * NORMAL dimension, but this should be avoided entirely to reduce
 * the potential for bugs.
 * <p>
 * Use the static <code>makeworldname</code> method (threadsafe) 
 * to generate a name from a world name and a <code>DimensionType</code>.
 * <p>
 * As usual, world access through the cache must be performed in the server thread.
 * <p>
 * Worlds are weakly referenced, but entries are only actively removed from the cache if
 * they are found to have been garbage collected in an accessor method call.
 * 
 * @author Stefan Steinheimer (nosefish)
 *
 */
public class FishyWorld {
	private static final Map<String, WeakReference<World>> cache = new HashMap<>(3);
	private static final ConcurrentMap<String, FishyWorld> fishyWorldPool = new ConcurrentHashMap<>(3, 0.9F, 1);
	
	private final String worldName;
	private final DimensionType dimensionType;
	
	private FishyWorld(String name, DimensionType dim) {
		this.worldName = name;
		this.dimensionType = dim;
	}
	
	public World getWorldIfLoaded() {
		return getLoadedWorld(this.worldName);
	}
	
	public World getAndLoad() {
		return getWorld(this.worldName);
	}
	
	public DimensionType getType() {
		return this.dimensionType;
	}
	
	public String getWorldName() {
		return this.worldName;
	}
	
	@Override
	public int hashCode() {
		return this.worldName.hashCode();
	}
	
	@Override
	public String toString() {
		return this.worldName;
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other instanceof FishyWorld) {
			return ((FishyWorld) other).worldName.equals(this.worldName);
		}
		return false;
	}
	
	public static FishyWorld factory(World world) {
		return factory(world.getName(), world.getType());
	}
	
	public static FishyWorld factory(String worldName, DimensionType dim) {
		String name = makeWorldName(worldName, dim);
		FishyWorld toReturn = fishyWorldPool.get(name);
		if (toReturn == null) {
			FishyWorld newWorld = new FishyWorld(name, dim);
			FishyWorld oldWorld = fishyWorldPool.putIfAbsent(name, newWorld);
			toReturn = oldWorld != null ? oldWorld : newWorld;
		}
		return toReturn;
	}
	
	
	/**
	 * Gets the world for the name. Does not load unloaded worlds.
	 * 
	 * @param name the world name
	 * @return the matching <code>World</code>, or null if no world with that name is loaded
	 */
	public static World getLoadedWorld(String name) {
		World returnWorld = null;
		WorldManager worldMan = Canary.getServer().getWorldManager();
		if (worldMan.worldIsLoaded(name)) {
			// it's loaded
			WeakReference<World> wRef = cache.get(name);
			if (wRef != null) {
				// found in cache, we're probably done, just one more thing..
				returnWorld = wRef.get();
				if (returnWorld == null) {
					// existed previously, but has been garbage collected
					cache.remove(name);
					returnWorld = Canary.getServer().getWorld(name);
					if (returnWorld != null) {
						// but it exists again (it's loaded, after all...)
						cache.put(name, new WeakReference<>(returnWorld));
					}
				} 
			} else {
				// not in cache
				returnWorld = Canary.getServer().getWorld(name);
				if (returnWorld != null) {
					// but it exists
					cache.put(name, new WeakReference<>(returnWorld));
				}
			}
		}
		return returnWorld;
	}
	
	/**
	 * Gets the world for the name. Loads the world if it
	 * is not loaded, just like <code>Canary.getServer().getWorld(String)</code>.
	 * 
	 * @param name the world name
	 * @return the matching <code>World</code>, or null if no world with that name exists
	 */
	public static World getWorld(String name) {
		World returnWorld;
		WeakReference<World> wRef = cache.get(name);
		if (wRef != null) {
			// found in cache, is the value still valid?
			returnWorld = wRef.get();
			if (returnWorld != null) {
				// it's in there, but is it loaded?
				WorldManager worldMan = Canary.getServer().getWorldManager();
				if (! worldMan.worldIsLoaded(name)) {
					// not loaded, let's change that
					returnWorld = Canary.getServer().getWorld(name);
				}
			} else {
				// existed previously, but has been garbage collected
				cache.remove(name);
				returnWorld = Canary.getServer().getWorld(name);
				if (returnWorld != null) {
					// but it exists again
					cache.put(name, new WeakReference<>(returnWorld));
				}
			}
		} else {
			// not in cache
			returnWorld = Canary.getServer().getWorld(name);
			if (returnWorld != null) {
				// but it exists
				cache.put(name, new WeakReference<>(returnWorld));
			}
		}
		return returnWorld;
	}
	
	/**
	 * Generates a world name to use with the <code>WorldNameCache</code>
	 * from a world name string and a <code>DimensionType</code>.
	 * <p>
	 * Canary/Minecraft classes usually use these two parts to refer
	 * to a world, but for FishySign's purposes it is more adequate
	 * to use the long format. You also get matching long names
	 * from <code>World.getFqName</code>.
	 * 
	 * @param world the name of the world
	 * @param dimension dimension type of the world
	 * @return the properly formatted world name
	 */
	public static String makeWorldName(String world, DimensionType dimension) {
		return world + "_" + dimension.getName();
	}

	/**
	 * <b>Not thread-safe! Only call from server thread, or be prepared for CMEs</b>
	 * 
	 * @param loc
	 * @return
	 */
	public static boolean isBlockLoaded(FishyLocationInt loc) {
		World world = loc.getWorld().getWorldIfLoaded();
		if (world == null) {
			return false;
		}
		if (! world.isChunkLoaded(loc.getIntX(), loc.getIntY(), loc.getIntZ())) {
			return false;
		}
		return true;
	}
}
