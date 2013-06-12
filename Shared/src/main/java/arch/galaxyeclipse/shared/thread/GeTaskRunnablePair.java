package arch.galaxyeclipse.shared.thread;

import com.google.common.base.Preconditions;

/**
 *
 */
public class GeTaskRunnablePair<T extends Runnable> {

    private GeDelayedRunnableTask delayedRunnableTask;
    private T runnable;
    private long millisecondsDelay;
    private boolean repeat;
    private boolean sleepAfter;

    public GeTaskRunnablePair(long millisecondsDelay) {
        this(millisecondsDelay, null);
    }

    public GeTaskRunnablePair(long millisecondsDelay, T runnable) {
        this(millisecondsDelay, runnable, true, false);
    }

    public GeTaskRunnablePair(long millisecondsDelay, T runnable,
            boolean repeat, boolean sleepAfter) {

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
