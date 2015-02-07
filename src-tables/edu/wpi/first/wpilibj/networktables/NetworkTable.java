package edu.wpi.first.wpilibj.networktables;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import edu.wpi.first.wpilibj.networktables.server.NTServer;

/**
 *
 */
public class NetworkTable implements ITable {
    
    
    
	/**
	 * The path separator for sub-tables and keys
	 */
	public static final char PATH_SEPARATOR = '/';
	
	private static NTServerTableProvider m_staticProvider = null;
	
	private synchronized static void checkInit(){
		if(m_staticProvider != null)
			throw new IllegalStateException("Network tables has already been initialized");
	}
	/**
	 * @throws IOException
	 */
	public synchronized static void initialize() throws IOException {
		checkInit();
		m_staticProvider = new NTServerTableProvider();
	}
        
	/**
	 * set the table provider for static network tables methods
	 * This must be called before initalize or getTable
	 */
	public synchronized static void setTableProvider(NTServerTableProvider provider) {
		checkInit();
		m_staticProvider = provider;
	}
	
	/**
	 * Gets the table with the specified key. If the table does not exist, a new table will be created.<br>
	 * This will automatically initialize network tables if it has not been already
	 * 
	 * @param key
	 *            the key name
	 * @return the network table requested
	 */
	public synchronized static ITable getTable(String key) {
		if(m_staticProvider == null)
			try {
				initialize();
			} catch (IOException e) {
				throw new RuntimeException("NetworkTable could not be initialized: " + e + ": " + e.getMessage());
			}
		return m_staticProvider.getTable(PATH_SEPARATOR + key);
	} 
	
	private final String m_path;
	private final EntryCache m_entryCache;
	private final NetworkTableKeyCache m_absoluteKeyCache;
	private final NTServerTableProvider m_provider;
	private final NTServer m_node;
	
	static class NetworkTableKeyCache{
        private final Hashtable<String, String> m_strCache = new Hashtable<String, String>();
        private final String m_strCachePath;

        public NetworkTableKeyCache(String path) {
            m_strCachePath = path;
        }
        
        /**
         * @param input
         * @return the value for a given input
         */
        public String get(final String input){
            String cachedValue = m_strCache.get(input);
            if(cachedValue == null)
                m_strCache.put(input, cachedValue = (m_strCachePath + PATH_SEPARATOR + input));
            return cachedValue;
        }
    }
    
    private class EntryCache {
        private final Hashtable<String, NTTableEntry> m_cache = new Hashtable<String, NTTableEntry>();
        
        public NTTableEntry get(final String key){
            NTTableEntry cachedValue = m_cache.get(key);
            if(cachedValue == null){
                cachedValue = m_node.getEntryStore().getEntry(m_absoluteKeyCache.get(key));
                if(cachedValue != null)
                    m_cache.put(key, cachedValue);
            }
            return cachedValue;
        }
    }

	NetworkTable(String path, NTServerTableProvider provider) {
		m_path = path;
		m_entryCache = new EntryCache();
		m_absoluteKeyCache = new NetworkTableKeyCache(path);
		m_provider = provider;
		m_node = provider.getNode();
	}
	public String toString(){
		return "NetworkTable: " + m_path;
	}
	
	public boolean isConnected() {
		return true;
	}

	public boolean isServer() {
		return true;
	}

	public void addTableListener(ITableListener listener) {
		addTableListener(listener, false);
	}

	private final Hashtable<ITableListener, List<ITableListener>> m_listenerMap =
	        new Hashtable<ITableListener, List<ITableListener>>();
	
	public void addTableListener(ITableListener listener, boolean immediateNotify) {
	    List<ITableListener> adapters = m_listenerMap.get(listener);
            if(adapters == null){
                    adapters = new ArrayList<ITableListener>();
                    m_listenerMap.put(listener, adapters);
            }
            NetworkTableListenerAdapter adapter = new NetworkTableListenerAdapter(m_path + PATH_SEPARATOR, this, listener);
            adapters.add(adapter);
            m_node.addTableListener(adapter, immediateNotify);
	}
	
	public void addTableListener(String key, ITableListener listener, boolean immediateNotify) {
	    List<ITableListener> adapters = m_listenerMap.get(listener);
		if(adapters == null){
		    adapters = new ArrayList<ITableListener>();
			m_listenerMap.put(listener, adapters);
		}
		NetworkTableKeyListenerAdapter adapter = new NetworkTableKeyListenerAdapter(key, m_absoluteKeyCache.get(key), this, listener);
		adapters.add(adapter);
		m_node.addTableListener(adapter, immediateNotify);
	}
	
	public void addSubTableListener(final ITableListener listener) {
	    List<ITableListener> adapters = m_listenerMap.get(listener);
		if(adapters == null){
			adapters = new ArrayList<ITableListener>();
			m_listenerMap.put(listener, adapters);
		}
		NetworkTableSubListenerAdapter adapter = new NetworkTableSubListenerAdapter(m_path, this, listener);
		adapters.add(adapter);
		m_node.addTableListener(adapter, true);
	}

	public void removeTableListener(ITableListener listener) {
	    List<ITableListener> adapters = m_listenerMap.get(listener);
		if(adapters != null){
			for(int i = 0; i < adapters.size(); ++i)
				m_node.removeTableListener((ITableListener) adapters.get(i));
			adapters.clear();
		}
	}
	
	@SuppressWarnings("unused")
    private synchronized NTTableEntry getEntry(String key){
		return m_entryCache.get(key);
	}

	/**
	 * Returns the table at the specified key. If there is no table at the
	 * specified key, it will create a new table
	 * 
	 * @param key
	 *            the key name
	 * @return the networktable to be returned
	 */
	public synchronized ITable getSubTable(String key) {
		return m_provider.getTable(m_absoluteKeyCache.get(key));
	}

	/**
	 * Checks the table and tells if it contains the specified key
	 * 
	 * @param key
	 *            the key to be checked
	 */
	public boolean containsKey(String key) {
            return m_node.containsKey(m_absoluteKeyCache.get(key));
	}
        
	public boolean containsSubTable(String key){
            String subtablePrefix = m_absoluteKeyCache.get(key)+PATH_SEPARATOR;
            if(m_node.getEntryStore().keys().stream().anyMatch(k -> k.startsWith(subtablePrefix)))
                return true;
            
            return false;
        }

	/**
	 * Maps the specified key to the specified value in this table. The key can
	 * not be null. The value can be retrieved by calling the get method with a
	 * key that is equal to the original key.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public void putNumber(String key, double value) {
		putValue(key, new Double(value));//TODO cache doubles
	}

	/**
	 * Returns the key that the name maps to.
	 * 
	 * @param key
	 *            the key name
	 * @return the key
	 * @throws TableKeyNotDefinedException
	 *             if the specified key is null
	 */
	public double getNumber(String key) throws TableKeyNotDefinedException {
		return m_node.getDouble(m_absoluteKeyCache.get(key));
	}

	/**
	 * Returns the key that the name maps to. If the key is null, it will return
	 * the default value
	 * 
	 * @param key
	 *            the key name
	 * @param defaultValue
	 *            the default value if the key is null
	 * @return the key
	 */
	public double getNumber(String key, double defaultValue) {
		try {
			return m_node.getDouble(m_absoluteKeyCache.get(key));
		} catch (TableKeyNotDefinedException e) {
			return defaultValue;
		}
	}

	/**
	 * Maps the specified key to the specified value in this table. The key can
	 * not be null. The value can be retrieved by calling the get method with a
	 * key that is equal to the original key.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public void putString(String key, String value) {
		putValue(key, value);
	}

	/**
	 * Returns the key that the name maps to.
	 * 
	 * @param key
	 *            the key name
	 * @return the key
	 * @throws TableKeyNotDefinedException
	 *             if the specified key is null
	 */
	public String getString(String key) throws TableKeyNotDefinedException {
		return m_node.getString(m_absoluteKeyCache.get(key));
	}

	/**
	 * Returns the key that the name maps to. If the key is null, it will return
	 * the default value
	 * 
	 * @param key
	 *            the key name
	 * @param defaultValue
	 *            the default value if the key is null
	 * @return the key
	 */
	public String getString(String key, String defaultValue) {
		try {
			return m_node.getString(m_absoluteKeyCache.get(key));
		} catch (TableKeyNotDefinedException e) {
			return defaultValue;
		}
	}

	/**
	 * Maps the specified key to the specified value in this table. The key can
	 * not be null. The value can be retrieved by calling the get method with a
	 * key that is equal to the original key.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public void putBoolean(String key, boolean value) {
		putValue(key, value?Boolean.TRUE:Boolean.FALSE);
	}

	/**
	 * Returns the key that the name maps to.
	 * 
	 * @param key
	 *            the key name
	 * @return the key
	 * @throws TableKeyNotDefinedException
	 *             if the specified key is null
	 */
	public boolean getBoolean(String key) throws TableKeyNotDefinedException {
		return m_node.getBoolean(m_absoluteKeyCache.get(key));
	}

	/**
	 * Returns the key that the name maps to. If the key is null, it will return
	 * the default value
	 * 
	 * @param key
	 *            the key name
	 * @param defaultValue
	 *            the default value if the key is null
	 * @return the key
	 */
	public boolean getBoolean(String key, boolean defaultValue) {
		try {
			return m_node.getBoolean(m_absoluteKeyCache.get(key));
		} catch (TableKeyNotDefinedException e) {
			return defaultValue;
		}
	}
	

    public void retrieveValue(String key, Object externalValue) {
        m_node.retrieveValue(m_absoluteKeyCache.get(key), externalValue);
    }
        
	/**
	 * Maps the specified key to the specified value in this table. The key can
	 * not be null. The value can be retrieved by calling the get method with a
	 * key that is equal to the original key.
	 * 
	 * @param key the key name
	 * @param value the value to be put
	 */
	public void putValue(String key, Object value){
		NTTableEntry entry = m_entryCache.get(key);
		if(entry!=null)
			m_node.putValue(entry, value);
		else
			m_node.putValue(m_absoluteKeyCache.get(key), value);
	}
	
	/**
	 * Returns the key that the name maps to.
	 * NOTE: If the value is a double, it will return a Double object,
	 * not a primitive.  To get the primitive, use getDouble
	 * 
	 * @param key
	 *            the key name
	 * @return the key
	 * @throws TableKeyNotDefinedException
	 *             if the specified key is null
	 */
	public Object getValue(String key) throws TableKeyNotDefinedException {
		return m_node.getValue(m_absoluteKeyCache.get(key));
	}
	
	/**
	 * Returns the key that the name maps to. If the key is null, it will return
	 * the default value
	 * NOTE: If the value is a double, it will return a Double object,
	 * not a primitive.  To get the primitive, use getDouble
	 * 
	 * @param key
	 *            the key name
	 * @param defaultValue
	 *            the default value if the key is null
	 * @return the key
	 */
	public Object getValue(String key, Object defaultValue) {
		try {
			return m_node.getValue(m_absoluteKeyCache.get(key));
		} catch(TableKeyNotDefinedException e){
			return defaultValue;
		}
	}
}
