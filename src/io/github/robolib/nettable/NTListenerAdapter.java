package io.github.robolib.nettable;

/**
 * An adapter that is used to filter value change notifications and make the
 * path relative to the NetworkTable
 */
public class NTListenerAdapter implements ITableListener {

    private final ITableListener m_targetListener;
    private final ITable m_targetSource;
    private final String m_prefix;

    /**
     * Create a new adapter
     * 
     * @param prefix         the prefix that will be filtered/removed from the
     *                       beginning of the key
     * @param targetSource   the source that events passed to the target listener
     *                       will appear to come from
     * @param targetListener the listener where events are forwarded to
     */
    public NTListenerAdapter(String prefix, ITable targetSource, ITableListener targetListener) {
        m_prefix = prefix;
        m_targetSource = targetSource;
        m_targetListener = targetListener;
    }

    public void valueChanged(ITable source, String key, Object value, boolean isNew) {// TODO use string cache
        if (key.startsWith(m_prefix)) {
            String relativeKey = key.substring(m_prefix.length());
            if (contains(relativeKey, NetworkTable.PATH_SEPARATOR))
                return;
            m_targetListener.valueChanged(m_targetSource, relativeKey, value, isNew);
        }
    }

    private static boolean contains(String source, char target) {
        for (int i = 0; i < source.length(); ++i)
            if (source.charAt(i) == target)
                return true;
        return false;
    }

}
