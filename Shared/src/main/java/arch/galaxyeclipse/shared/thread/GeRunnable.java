package arch.galaxyeclipse.shared.thread;

/**
 *
 */
public interface GeRunnable extends Runnable {

    void start();

    void interrupt();

    boolean isAlive();
}
