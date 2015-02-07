package io.github.robolib.nettable;

import java.util.NoSuchElementException;


/**
 * A table whose values can be read and written to
 */
public interface ITable {

	/**
	 * @param key the key to search for
	 * @return true if the table as a value assigned to the given key
	 */
	public boolean containsKey(String key);

	/**
	 * @param key the key to search for
	 * @return true if there is a subtable with the key which contains at least one key/subtable of its own
	 */
	public boolean containsSubTable(String key);
	
    /**
     * @param key the name of the table relative to this one
     * @return a sub table relative to this one
     */
    public ITable getSubTable(String key);
	
    
	/**
	 * Gets the value associated with a key as an object
	 * @param key the key of the value to look up
	 * @return the value associated with the given key
	 * @throws NoSuchElementException if there is no value associated with the given key
	 */
	public Object getValue(String key) throws NoSuchElementException;
	/**
	 * Put a value in the table
	 * @param key the key to be assigned to
	 * @param value the value that will be assigned
	 * @throws IllegalArgumentException when the value is not supported by the table
	 */
	public void putValue(String key, Object value) throws IllegalArgumentException;
        
    public void retrieveValue(String key, Object externalValue);

	/**
	 * Put a number in the table
	 * @param key the key to be assigned to
	 * @param value the value that will be assigned
	 */
	public void putNumber(String key, double value);
	/**
	 * @param key the key to look up
	 * @return the value associated with the given key 
	 * @throws NoSuchElementException if there is no value associated with the given key
	 */
	public double getNumber(String key) throws NoSuchElementException;
	/**
	 * @param key the key to look up
	 * @param defaultValue the value to be returned if no value is found
	 * @return the value associated with the given key or the given default value if there is no value associated with the key
	 */
	public double getNumber(String key, double defaultValue);

	/**
	 * Put a string in the table
	 * @param key the key to be assigned to
	 * @param value the value that will be assigned
	 */
	public void putString(String key, String value);
	/**
	 * @param key the key to look up
	 * @return the value associated with the given key 
	 * @throws NoSuchElementException if there is no value associated with the given key
	 */
	public String getString(String key) throws NoSuchElementException;
	/**
	 * @param key the key to look up
	 * @param defaultValue the value to be returned if no value is found
	 * @return the value associated with the given key or the given default value if there is no value associated with the key
	 */
	public String getString(String key, String defaultValue);

	/**
	 * Put a boolean in the table
	 * @param key the key to be assigned to
	 * @param value the value that will be assigned
	 */
	public void putBoolean(String key, boolean value);
	/**
	 * @param key the key to look up
	 * @return the value associated with the given key 
	 * @throws NoSuchElementException if there is no value associated with the given key
	 */
	public boolean getBoolean(String key) throws NoSuchElementException;
	/**
	 * @param key the key to look up
	 * @param defaultValue the value to be returned if no value is found
	 * @return the value associated with the given key or the given default value if there is no value associated with the key
	 */
	public boolean getBoolean(String key, boolean defaultValue);

	/**
	 * Add a listener for changes to the table
	 * @param listener the listener to add
	 */
	public void addTableListener(ITableListener listener);
	/**
	 * Add a listener for changes to the table
	 * @param listener the listener to add
	 * @param immediateNotify if true then this listener will be notified of all current entries (marked as new)
	 */
	public void addTableListener(ITableListener listener, boolean immediateNotify);
	
	/**
	 * Add a listener for changes to a specific key the table
	 * @param key the key to listen for
	 * @param listener the listener to add
	 * @param immediateNotify if true then this listener will be notified of all current entries (marked as new)
	 */
	public void addTableListener(String key, ITableListener listener, boolean immediateNotify);
	/**
	 * This will immediately notify the listener of all current sub tables
	 * @param listener
	 */
	public void addSubTableListener(final ITableListener listener);
	/**
	 * Remove a listener from receiving table events
	 * @param listener the listener to be removed
	 */
	public void removeTableListener(ITableListener listener);
}
