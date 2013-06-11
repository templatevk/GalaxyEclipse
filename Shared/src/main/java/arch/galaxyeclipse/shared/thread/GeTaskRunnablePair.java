package arch.galaxyeclipse.shared.thread;

import arch.galaxyeclipse.shared.context.GeContextHolder;
import com.google.common.base.Preconditions;

/**
 *
 */
public class GeTaskRunnablePair<T extends Runnable> {

    private GeDelayedRunnableTask delayedRunnableTask;
    private GeExecutor geExecutor;
    private T runnable;
    private long millisecondsDelay;
    private boolean repeat;
    private boolean sleepAfter;

    public GeTaskRunnablePair(long millisecondsDelay) {
        this(millisecondsDelay, null, true, false);
    }

    public GeTaskRunnablePair(long millisecondsDelay, T runnable,
            boolean repeat, boolean sleepAfter) {

        this.geExecutor = GeContextHolder.getBean(GeExecutor.class);
        this.millisecondsDelay = millisecondsDelay;
        this.runnable = runnable;
        this.repeat = repeat;
        this.sleepAfter = sleepAfter;
    }

    public void start() {
        if (isAlive()) {
            delayedRunnableTask.interrupt();
        }

        Preconditions.checkNotNull(runnable);

        delayedRunnableTask = new GeDelayedRunnableTask(millisecondsDelay,
                runnable, repeat, sleepAfter);
        delayedRunnableTask.start();
    }

    public void stop() {
        if (isAlive()) {
            delayedRunnableTask.interrupt();
        }
    }

    public void setRunnable(T runnable) {
        this.runnable = runnable;
        if (delayedRunnableTask != null) {
            delayedRunnableTask.setRunnable(runnable);
        }
    }

    public boolean isAlive() {
        return delayedRunnableTask != null && delayedRunnableTask.isAlive();
    }
}
