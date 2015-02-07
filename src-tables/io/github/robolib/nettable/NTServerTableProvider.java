package io.github.robolib.nettable;

import java.io.IOException;
import java.util.Hashtable;

/**
 * Provides a {@link NetworkTable} for a given {@link NTNode}
 */
public class NTServerTableProvider {
	private final NTServer m_node;
	private final Hashtable<String, NetworkTable> m_tables = new Hashtable<String, NetworkTable>();

	/**
	 * Create a new NetworkTableProvider for a given NetworkTableNode
	 * @param node the node that handles the actual network table 
	 * @throws IOException 
	 */
	public NTServerTableProvider() throws IOException{
		m_node = new NTServer();
	}
	
	public ITable getRootTable(){
		return getTable("");
	}

	/**
     * Get a table by name
     * @param name the name of the table
     * @return a Table with the given name
     */
	public ITable getTable(String key) {
		if (m_tables.containsKey(key)) {
			return (NetworkTable) m_tables.get(key);
		} else {
			NetworkTable table = new NetworkTable(key);
			m_tables.put(key, table);
			return table;
		}
	}

	/**
	 * @return the Network Table node that backs the Tables returned by this provider
	 */
	public NTServer getServer() {
		return m_node;
	}

	/**
	 * close the backing network table node
	 */
	public void close() {
		m_node.close();
	}
}
