package arch.galaxyeclipse.shared.thread;

import arch.galaxyeclipse.shared.util.*;
import lombok.extern.slf4j.*;

/**
 * Represents a thread that will execute submitted runnable after the submitted 
 * milliseconds delay.
 */
@Slf4j
public class DelayedRunnableExecutor extends Thread {
	private int millisecondsDelay;
	private Runnable runnable;
	
	public DelayedRunnableExecutor(int millisecondsDelay, Runnable runnable) {
		this.millisecondsDelay = millisecondsDelay;
		this.runnable = runnable;
	}
	
	@Override
	public void run() {
		try { // Sleep for delay and execute the runnable
			Thread.sleep(millisecondsDelay);
			runnable.run();
		} catch (InterruptedException e) {
			log.error(LogUtils.getObjectInfo(this) + " error", e);
		}
	}
}
