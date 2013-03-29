package arch.galaxyeclipse.shared.thread;

import org.apache.log4j.*;

import arch.galaxyeclipse.shared.util.*;

public class DelayedRunnableExecutor extends Thread {
	private static final Logger log = Logger.getLogger(DelayedRunnableExecutor.class);
	
	private int millisecondsDelay;
	private Runnable runnable;
	
	public DelayedRunnableExecutor(int millisecondsDelay, Runnable runnable) {
		this.millisecondsDelay = millisecondsDelay;
		this.runnable = runnable;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(millisecondsDelay);
			runnable.run();
		} catch (InterruptedException e) {
			log.error(LogUtils.getObjectInfo(this) + " error", e);
		}
	}
}
