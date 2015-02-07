package edu.wpi.first.wpilibj.networktables;

import java.util.HashSet;
import java.util.Set;

/**
 * An adapter that is used to filter sub table change notifications and make the path relative to the NetworkTable
 * 
 * @author Mitchell
 *
 */
public class NetworkTableSubListenerAdapter implements ITableListener {
	
	private final ITableListener m_targetListener;
	private final NetworkTable m_targetSource;
	private final String m_prefix;
	
	private final Set<String> m_notifiedTables = new HashSet<String>();

	/**
	 * Create a new adapter
	 * @param prefix the prefix of the current table
	 * @param targetSource the source that events passed to the target listener will appear to come from
	 * @param targetListener the listener where events are forwarded to
	 */
	public NetworkTableSubListenerAdapter(String prefix, NetworkTable targetSource, ITableListener targetListener){
		m_prefix = prefix;
		m_targetSource = targetSource;
		m_targetListener = targetListener;
	}

	public void valueChanged(ITable source, String key, Object value, boolean isNew) {//TODO use string cache
            if(key.startsWith(m_prefix)){
                String relativeKey = key.substring(m_prefix.length() + 1);
                int endSubTable = -1;//TODO implement sub table listening better
                for(int i = 0; i < relativeKey.length(); ++i){
                    if(relativeKey.charAt(i) == NetworkTable.PATH_SEPARATOR){//is sub table
                        endSubTable = i;
                        break;
                    }
                }
                if(endSubTable != -1){
                    String subTableKey = relativeKey.substring(0, endSubTable);
                    if(!m_notifiedTables.contains(subTableKey)){
                        m_notifiedTables.add(subTableKey);
                        m_targetListener.valueChanged(m_targetSource, subTableKey, m_targetSource.getSubTable(subTableKey), true);
                    }
                }
            }
	}

}
