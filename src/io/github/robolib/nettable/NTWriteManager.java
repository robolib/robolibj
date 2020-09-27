
package io.github.robolib.nettable;

import java.util.ArrayDeque;
import java.util.Queue;

import io.github.robolib.nettable.NTThread.NTRunnable;
import io.github.robolib.nettable.entry.NTEntryStore;
import io.github.robolib.nettable.entry.NTTableEntry;
import io.github.robolib.util.log.Logger;

/**
 * A write manager is a {@link IncomingEntryReceiver} that buffers transactions
 * and then and then dispatches them to a flushable transaction receiver that is
 * periodically offered all queued transaction and then flushed
 */
public class NTWriteManager implements NTRunnable {
    private final int SLEEP_TIME = 100;

    private final int QUEUE_SIZE = 500;

    private Object m_transactionsLock = new Object();
    private NTThread m_thread;
    private final NTEntryStore m_entryStore;

    private volatile Queue<NTTableEntry> m_incomingAssignmentQueue;
    private volatile Queue<NTTableEntry> m_incomingUpdateQueue;
    private volatile Queue<NTTableEntry> m_outgoingAssignmentQueue;
    private volatile Queue<NTTableEntry> m_outgoingUpdateQueue;

    private NTConnectionList m_receiver;
    private long m_lastWrite;

    private final long m_keepAliveDelay;

    /**
     * Create a new Write manager
     * 
     * @param receiver
     * @param threadManager
     * @param transactionPool
     * @param entryStore
     */
    public NTWriteManager(final NTConnectionList receiver, final NTEntryStore entryStore, long keepAliveDelay) {

        m_receiver = receiver;
        m_entryStore = entryStore;

        m_incomingAssignmentQueue = new ArrayDeque<NTTableEntry>(QUEUE_SIZE);
        m_incomingUpdateQueue = new ArrayDeque<NTTableEntry>(QUEUE_SIZE);
        m_outgoingAssignmentQueue = new ArrayDeque<NTTableEntry>(QUEUE_SIZE);
        m_outgoingUpdateQueue = new ArrayDeque<NTTableEntry>(QUEUE_SIZE);

        m_keepAliveDelay = keepAliveDelay;
    }

    /**
     * start the write thread
     */
    public void start() {
        if (m_thread != null)
            stop();
        m_lastWrite = System.currentTimeMillis();
        m_thread = NTThread.newBlockingPeriodicThread(this, "NT Write Manager Thread");
    }

    /**
     * stop the write thread
     */
    public void stop() {
        if (m_thread != null)
            m_thread.stop();
    }

    public void offerOutgoingAssignment(NTTableEntry entry) {
        if (entry.isDirty())
            return;
        entry.makeDirty();
        synchronized (m_transactionsLock) {
            m_incomingAssignmentQueue.add(entry);
            if (m_incomingAssignmentQueue.size() == QUEUE_SIZE) {
                try {
                    run();
                } catch (InterruptedException e) {
                }
                Logger.get(this).warn("Assignment queue overflowed.");
                Logger.get(this)
                        .warn("Decrease the rate at which you create new entries, or increase the write buffer size.");
            }
        }
    }

    public void offerOutgoingUpdate(NTTableEntry entry) {
        if (entry.isDirty())
            return;
        entry.makeDirty();
        synchronized (m_transactionsLock) {
            m_incomingUpdateQueue.add(entry);
            if (m_incomingUpdateQueue.size() == QUEUE_SIZE) {
                try {
                    run();
                } catch (InterruptedException e) {
                }
                Logger.get(this).warn("Update queue overflowed.");
                Logger.get(this)
                        .warn("Decrease the rate at which you update entries, or increase the write buffer size.");
            }
        }
    }

    /**
     * the periodic method that sends all buffered transactions
     * 
     * @throws InterruptedException
     */
    public void run() throws InterruptedException {
        synchronized (m_transactionsLock) {
            // swap the assignment and update queue
            Queue<NTTableEntry> tmp = m_incomingAssignmentQueue;
            m_incomingAssignmentQueue = m_outgoingAssignmentQueue;
            m_outgoingAssignmentQueue = tmp;

            tmp = m_incomingUpdateQueue;
            m_incomingUpdateQueue = m_outgoingUpdateQueue;
            m_outgoingUpdateQueue = tmp;
        }

        boolean wrote = false;

        if (m_outgoingAssignmentQueue.size() > 0) {
            synchronized (m_entryStore) {
                m_outgoingAssignmentQueue.forEach(entry -> {
                    entry.makeClean();
                    m_receiver.offerOutgoingAssignment(entry);
                });
            }
            m_outgoingAssignmentQueue.clear();
            wrote = true;
        }

        if (m_outgoingUpdateQueue.size() > 0) {
            synchronized (m_entryStore) {
                m_outgoingUpdateQueue.forEach(entry -> {
                    entry.makeClean();
                    m_receiver.offerOutgoingUpdate(entry);
                });
            }
            m_outgoingUpdateQueue.clear();
            wrote = true;
        }

        if (wrote) {
            m_receiver.flush();
            m_lastWrite = System.currentTimeMillis();
        } else if (System.currentTimeMillis() - m_lastWrite > m_keepAliveDelay) {
            m_receiver.ensureAlive();
        }

        Thread.sleep(SLEEP_TIME);
    }

}
