package arch.galaxyeclipse.shared.thread;

import arch.galaxyeclipse.shared.util.LogUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a thread that will execute submitted runnable after the submitted 
 * milliseconds delay.
 */
@Slf4j
public class DelayedRunnableTask extends Thread {
	private long millisecondsDelay;
    private boolean repeat;
    private boolean sleepAfter;

    private @Setter Runnable runnable;

    public DelayedRunnableTask(long millisecondsDelay, Runnable runnable) {
        this(millisecondsDelay, runnable, false, false);
    }

	public DelayedRunnableTask(long millisecondsDelay, Runnable runnable,
            boolean repeat, boolean sleepAfter) {
		this.millisecondsDelay = millisecondsDelay;
		this.runnable = runnable;
        this.repeat = repeat;
        this.sleepAfter = sleepAfter;
    }
	
	@Override
	public void run() {
		try {
            if (sleepAfter) {
                do {
                    runnable.run();
                    Thread.sleep(millisecondsDelay);
                } while (repeat);
            } else {
                do {
                    Thread.sleep(millisecondsDelay);
                    runnable.run();
                } while (repeat);
            }
		} catch (InterruptedException e) {

		} catch (Exception e) {
            DelayedRunnableTask.log.error(LogUtils.getObjectInfo(this) + " error", e);
        }
	}
}
