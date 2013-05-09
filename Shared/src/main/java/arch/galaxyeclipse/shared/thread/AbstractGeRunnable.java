package arch.galaxyeclipse.shared.thread;

import java.util.concurrent.Future;

/**
 *
 */
abstract class AbstractGeRunnable implements GeRunnable {
    private Future<?> future;

    protected AbstractGeRunnable() {

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
