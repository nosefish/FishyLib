package net.gmx.nosefish.fishylib.properties;


import java.util.Collection;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.World;
import net.canarymod.config.Configuration;
import net.canarymod.logger.Logman;
import net.canarymod.plugin.Plugin;
import net.visualillusionsent.utils.PropertiesFile;
import net.visualillusionsent.utils.UtilityException;

public class Properties {
	private Plugin plugin;
	private final Integer ALL = Integer.MIN_VALUE;
	private Logman logger;
	
	public Properties(Plugin plugin) {
		this.plugin = plugin;
		this.logger = plugin.getLogman();
	}
	
	/**
	 * If you can't guess what it does, go away.
	 * 
	 * @return true if debug mode is enabled, false otherwise.
	 */
	public boolean isDebug() {
		return getUniverseProperties().getBoolean("debug", false);
	}

	/**
	 * Gets a boolean value from the properties.
	 * 
	 * @param world
	 *            the <code>World</code> in which the event occurred. Must not
	 *            be null.
	 * @param key
	 *            the <Key> that identifies the property
	 * @return the value of the property, or false if the property is not
	 *         configured.
	 */
	public boolean getBoolean(World world, PropertyKey key) {
		if (key.getType() != ValueType.BOOLEAN) {
			throw new PropertyException("Key " + key.getPropertyName()
								+ " has type " + key.getType().toString()
								+ ", expected BOOLEAN.");
		}
		PropertiesFile propertiesFile = getWorldProperties(world);
		String keyString = key.getPropertyName();
		if (propertiesFile.containsKey(keyString)) {
			return propertiesFile.getBoolean(keyString);
		}
		// key not defined for world, use universal config
		propertiesFile = getUniverseProperties();
		if (propertiesFile.containsKey(keyString)) {
			return propertiesFile.getBoolean(keyString);
		}
		// not found
		logger.logWarning("Key not found in properties: " + keyString);
		return (Boolean)key.getDefault();
	}

	/**
	 * Gets a String value from the properties.
	 * 
	 * @param world
	 *            the <code>World</code> in which the event occurred. Must not
	 *            be null.
	 * @param key
	 *            the <Key> that identifies the property
	 * @return the value of the property, or the default value if the property is not
	 *         configured.
	 */
	public String getString(World world, PropertyKey key) {
		if (key.getType() != ValueType.STRING) {
			throw new PropertyException("Key " + key.getPropertyName()
								+ " has type " + key.getType().toString()
								+ ", expected STRING.");
		}
		PropertiesFile propertiesFile = getWorldProperties(world);
		String keyString = key.getPropertyName();
		if (propertiesFile.containsKey(keyString)) {
			return propertiesFile.getString(keyString);
		}
		// key not defined for world, use universal config
		propertiesFile = getUniverseProperties();
		keyString = key.getPropertyName();
		if (propertiesFile.containsKey(keyString)) {
			return propertiesFile.getString(keyString);
		}
		// not found
		logger.logWarning("Key not found in properties: " + keyString);
		return (String)key.getDefault();
	}

	/**
	 * Gets a boolean value from the properties.
	 * 
	 * @param world
	 *            the <code>World</code> in which the event occurred. Must not
	 *            be null.
	 * @param key
	 *            the <Key> that identifies the property
	 * @return the value of the property, or the default value if the property is not
	 *         configured.
	 */
	public double getDouble(World world, PropertyKey key) {
		if (key.getType() != ValueType.DOUBLE) {
			throw new PropertyException("Key " + key.getPropertyName()
								+ " has type " + key.getType().toString()
								+ ", expected DOUBLE.");
		}
		PropertiesFile propertiesFile = getWorldProperties(world);
		String keyString = key.getPropertyName();
		if (propertiesFile.containsKey(keyString)) {
			return propertiesFile.getDouble(keyString);
		}
		// key not defined for world, use universal config
		propertiesFile = getUniverseProperties();
		keyString = key.getPropertyName();
		if (propertiesFile.containsKey(keyString)) {
			return propertiesFile.getDouble(keyString);
		}
		// not found
		logger.logWarning("Key not found in properties: " + keyString);
		return (Double)key.getDefault();
	}

	/**
	 * Checks if a given value is contained in a property that is specified as a
	 * comma separated list of integers in the properties file.
	 * 
	 * @param world
	 *            the <code>World</code> in which the event occurred. Must not
	 *            be null.
	 * @param key
	 *            the <Key> that identifies the property
	 * @return the value of the property, or <code>false</code> if the property is not
	 *         configured.
	 */
	public boolean containsInteger(World world, PropertyKey key, int value) {
		if (key.getType() != ValueType.CSV_INT) {
			throw new PropertyException("Key " + key.getPropertyName()
								+ " has type " + key.getType().toString()
								+ ", expected CSV_INT.");
		}
		String keyString;
		PropertiesFile propertiesFile = getWorldProperties(world);
		keyString = key.getPropertyName();
		if (propertiesFile.containsKey(keyString)) {
			int[] valueArray = propertiesFile.getIntArray(keyString);
			return arrayContainsInt(valueArray, value);
		}
		// key not defined for world, use universal config
		propertiesFile = getUniverseProperties();
		keyString = key.getPropertyName();
		if (propertiesFile.containsKey(keyString)) {
			int[] valueArray = propertiesFile.getIntArray(keyString);
			return arrayContainsInt(valueArray, value);
		}
		// not found
		logger.logWarning("Key not found in properties: " + keyString);
		return false;
	}
	
	/**
	 * Checks if a player has a configured permission.
	 * 
	 * @param key
	 *            the <Key> that identifies the property
	 * @param player
	 *            the player that needs permission
	 * @return true if the player has permission, false if they haven't
	 */
	public boolean hasPermission(PropertyKey key, Player player) {
		return hasPermission(key, player, ALL );
	}
	
	/**
	 * Checks if a player has a configured permission associated with an integer
	 * value, for example permission to use an item.
	 * 
	 * @param key
	 *            the <Key> that identifies the property
	 * @param player
	 *            the player that needs permission
	 * @param value
	 *            the value to which the permission applies
	 * @return true if the player has permission, false if they haven't
	 */
	public boolean hasPermission(PropertyKey key, Player player, Integer value) {
		if (key.getType() != ValueType.CSV_PERMISSIONS) {
			throw new PropertyException("Key " + key.getPropertyName()
								+ " has type " + key.getType().toString()
								+ ", expected CSV_PERMISSIONS.");
		}
		// permissionskey=a.b.c,x.y
		// permissionskey/a.b.c=1,2,3,4,5
		World world = player.getWorld();
		String keyString = key.getPropertyName();
		PropertiesFile propertiesFile = getWorldProperties(world);
		if (propertiesFile.containsKey(keyString)) {
			return hasPermission(player, keyString, value, propertiesFile);
		}
		// key not defined for world, use universal config
		propertiesFile = getUniverseProperties();
		keyString = key.getPropertyName();
		if (propertiesFile.containsKey(keyString)) {
			return hasPermission(player, keyString, value, propertiesFile);
		}
		// not found
		logger.logWarning("Key not found in properties: " + keyString);
		return false;
	}
	
	// performs the actual permission check
	// assumes that the key exists
	private boolean hasPermission(Player player, String keyString, int value, PropertiesFile propertiesFile) {
		String[] permStrings = null;
		try {
			permStrings = propertiesFile.getStringArray(keyString);
		} catch (UtilityException e) {
			//something went wrong.
			logger.logStacktrace("UtilityException while getting permissions for key " + keyString, e);
			return false;
		}
		for (String permString : permStrings) {
			if (permString.equals("NOBODY")) {
				return false;
			}
			if (player.hasPermission(permString)) {
				// get values for this permission
				// permissionskey/per.miss.ion=1,2,3,4,5
				String keySlashPerm = keyString + "/" + permString;
				if (propertiesFile.containsKey(keySlashPerm)) {
					try {
						int[] permValues = propertiesFile.getIntArray(keySlashPerm);
						if (arrayContainsInt(permValues,value)) {
							// value found, player has permission
							return true;
						}
					} catch (UtilityException e) {
						// something went wrong
						logger.logStacktrace("UtilityException while getting permissions for key " + keyString, e);
						return false;
					}
				} else {
					// no list of values means permission for all values
					return true;
				}
			}
		}
		// player has no permission that gives him rights to the value
		return false;
	}
	
	
	public void addMissingKeys(Collection<PropertyKey> keys) {
		PropertiesFile universeProperties = getUniverseProperties();
		if (! universeProperties.containsKey("debug")) {
			universeProperties.setBoolean("debug", false);
		}
		for (PropertyKey k : keys) {
			if (k.getDefault() != null
					&& ! universeProperties.containsKey(k.getPropertyName())) {
				switch (k.getType()) {
				case BOOLEAN: 
					universeProperties.setBoolean(k.getPropertyName(), (Boolean)k.getDefault());
					break;
				case STRING:
					universeProperties.setString(k.getPropertyName(), (String)k.getDefault());
					break;
				case DOUBLE:
					universeProperties.setDouble(k.getPropertyName(), (Double)k.getDefault());
					break;
				case CSV_INT:
					universeProperties.setIntArray(k.getPropertyName(), (int[])k.getDefault());
					break;
				case CSV_PERMISSIONS:
					universeProperties.setStringArray(k.getPropertyName(), (String[])k.getDefault());
					break;
				}
			}
		}
		universeProperties.save();
	}

	
	// gets world specific properties file for this plugin
	private PropertiesFile getWorldProperties(World world) {
		return Configuration.getPluginConfig(plugin, world);
	}
	
	// gets general properties for this plugin
	private PropertiesFile getUniverseProperties() {
		return Configuration.getPluginConfig(plugin);
	}
	
	// check if an integer array contains a specific value
	// needed because Arrays.asList doesn't work with primitive types
	private boolean arrayContainsInt(int[] array, int value) {
		for (int i : array) {
			if (i == value) {
				return true;
			}
		}
		return false;
	}

}
