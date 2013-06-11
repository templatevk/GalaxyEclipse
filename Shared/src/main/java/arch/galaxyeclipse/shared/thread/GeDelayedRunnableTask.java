package arch.galaxyeclipse.shared.thread;

import arch.galaxyeclipse.shared.common.GeLogUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a thread that will execute submitted runnable after the submitted 
 * milliseconds delay.
 */
@Slf4j
public class GeDelayedRunnableTask extends GeAbstractRunnable implements GeRunnable {
	private long millisecondsDelay;
    private boolean repeat;
    private boolean sleepAfter;

    private @Setter Runnable runnable;

    public GeDelayedRunnableTask(long millisecondsDelay, Runnable runnable) {
        this(millisecondsDelay, runnable, false, false);
    }

	public GeDelayedRunnableTask(long millisecondsDelay, Runnable runnable,
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
                } while (repeat && !Thread.interrupted());
            } else {
                do {
                    Thread.sleep(millisecondsDelay);
                    runnable.run();
                } while (repeat && !Thread.interrupted());
            }
		} catch (InterruptedException e) {

		} catch (Exception e) {
            GeDelayedRunnableTask.log.error(GeLogUtils.getObjectInfo(this) + " error", e);
        }
	}
}
