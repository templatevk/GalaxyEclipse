package arch.galaxyeclipse.shared.thread;

import arch.galaxyeclipse.shared.context.ContextHolder;

/**
 *
 */
public class TaskRunnablePair<T extends Runnable> {
    private final GeExecutor geExecutor;
    private DelayedRunnableTask delayedRunnableTask;
    private T runnable;
    private long millisecondsDelay;
    private boolean repeat;
    private boolean sleepAfter;

    public TaskRunnablePair(long millisecondsDelay, T runnable,
            boolean repeat, boolean sleepAfter) {
        this.geExecutor = ContextHolder.getBean(GeExecutor.class);
        this.millisecondsDelay = millisecondsDelay;
        this.runnable = runnable;
        this.repeat = repeat;
        this.sleepAfter = sleepAfter;
    }

    public void start() {
        if (isAlive()) {
            delayedRunnableTask.interrupt();
        }
        delayedRunnableTask = new DelayedRunnableTask(millisecondsDelay,
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
