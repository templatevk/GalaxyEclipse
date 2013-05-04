package arch.galaxyeclipse.shared.thread;

/**
 *
 */
public class TaskRunnablePair<T extends Runnable> {
    private DelayedRunnableTask delayedRunnableTask;
    private T runnable;
    private long millisecondsDelay;
    private boolean repeat;
    private boolean sleepAfter;

    public TaskRunnablePair() {
        this(0);
    }

    public TaskRunnablePair(long millisecondsDelay) {
        this(millisecondsDelay, null);
    }

    public TaskRunnablePair(long millisecondsDelay, T runnable) {
        this(millisecondsDelay, runnable, false, false);
    }

    public TaskRunnablePair(long millisecondsDelay, T runnable,
            boolean repeat, boolean sleepAfter) {
        this.millisecondsDelay = millisecondsDelay;
        this.runnable = runnable;
        this.repeat = repeat;
        this.sleepAfter = sleepAfter;
    }

    public void start() {
        if (delayedRunnableTask != null) {
            delayedRunnableTask.interrupt();
        }
        delayedRunnableTask = new DelayedRunnableTask(millisecondsDelay,
                runnable, repeat, sleepAfter);
        delayedRunnableTask.start();
    }

    public void stop() {
        if (delayedRunnableTask != null) {
            delayedRunnableTask.interrupt();
            delayedRunnableTask = null;
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
