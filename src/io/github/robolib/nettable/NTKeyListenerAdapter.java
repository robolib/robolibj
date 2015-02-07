package io.github.robolib.nettable;


/**
 * An adapter that is used to filter value change notifications for a specific key
 */
public class NTKeyListenerAdapter implements ITableListener {
    
    private final ITableListener m_targetListener;
    private final NetworkTable m_targetSource;
    private final String m_relativeKey;
    private final String m_fullKey;

    /**
     * Create a new adapter
     * @param relativeKey the name of the key relative to the table (this is what the listener will receiver as the key)
     * @param fullKey the full name of the key in the {@link NTNode}
     * @param targetSource the source that events passed to the target listener will appear to come from
     * @param targetListener the listener where events are forwarded to
     */
    public NTKeyListenerAdapter(String relativeKey, String fullKey, NetworkTable targetSource, ITableListener targetListener){
        m_relativeKey = relativeKey;
        m_fullKey = fullKey;
        m_targetSource = targetSource;
        m_targetListener = targetListener;
    }

    public void valueChanged(ITable source, String key, Object value, boolean isNew) {
        if(key.equals(m_fullKey)){
            m_targetListener.valueChanged(m_targetSource, m_relativeKey, value, isNew);
        }
    }

}
