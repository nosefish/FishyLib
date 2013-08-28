package net.gmx.nosefish.fishylib.properties;

/**
 * Intended to be implemented by an enum
 * 
 * @author Stefan Steinheimer (nosefish)
 *
 */
public interface PropertyKey {
	
	/**
	 * Gets the name of the property key that is used in the properties file
	 * 
	 * @return property key as it appears in the file
	 */
	public String getPropertyName();

	/**
	 * Gets the <code>ValueType</code> of this property. Used to determine how
	 * to load this property
	 * 
	 * @return the <code>ValueType</code> associated with this property
	 */
	public ValueType getType();
	
	/**
	 * Gets the default value of this property. This value will be applied if
	 * the property is missing from the properties file and will be written back
	 * to the file.
	 * 
	 * @return the default value
	 */
	public Object getDefault();
}
