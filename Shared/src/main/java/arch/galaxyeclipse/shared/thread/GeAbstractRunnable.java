package arch.galaxyeclipse.shared.thread;

import java.util.concurrent.Future;

/**
 *
 */
public abstract class GeAbstractRunnable implements GeRunnable {

    private Future<?> future;

    protected GeAbstractRunnable() {

    }

    @Override
    public boolean isAlive() {
        return future != null && !future.isDone() && !future.isCancelled();
    }

    @Override
    public void interrupt() {
        if (future != null) {
            future.cancel(true);
        }
    }

    @Override
    public void start() {
        future = GeExecutor.getINSTANCE().submit(this);
    }
}
