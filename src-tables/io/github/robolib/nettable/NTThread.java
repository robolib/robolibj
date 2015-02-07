package io.github.robolib.nettable;


/**
 * A simple thread manager that will run periodic threads in their own thread
 */
public class NTThread {
    
    /**
     * A runnable where the run method will be called periodically 
     */
    public static interface NTRunnable {
        /**
         * the method that will be called periodically on a thread
         * 
         * @throws InterruptedException thrown when the thread is supposed to be interrupted and stop (implementers should always let this exception fall through)
         */
        public void run() throws InterruptedException;
    }
    
	private final Thread m_thread;
	private boolean run = true;
	private NTThread(final NTRunnable r, String name){
		m_thread = new Thread(() -> {
		    try {
                while(run){
                    r.run();
                }
            } catch (InterruptedException e) {}
		}, name);
		m_thread.start();
	}
	public void stop() {
		run = false;
		m_thread.interrupt();
	}
	public boolean isRunning() {
		return m_thread.isAlive();
	}

	/**
     * @param r
     * @param name the name of the thread
     * @return a thread that will run the provided runnable repeatedly with the assumption that the runnable will block
     */
	public static final NTThread newBlockingPeriodicThread(final NTRunnable r, String name) {
		return new NTThread(r, name);
	}

}
