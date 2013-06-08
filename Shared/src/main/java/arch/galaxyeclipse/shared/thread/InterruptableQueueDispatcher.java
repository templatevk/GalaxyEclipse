package arch.galaxyeclipse.shared.thread;

import arch.galaxyeclipse.shared.common.ICommand;
import arch.galaxyeclipse.shared.common.StubCommand;
import lombok.extern.slf4j.Slf4j;

import java.util.AbstractQueue;

/**
 * Thread processing the queue passed applying the passed command to the queue items.
 */
@Slf4j
public class InterruptableQueueDispatcher<T> extends AbstractGeRunnable implements GeRunnable {
	private AbstractQueue<T> queue;
	private ICommand<T> command;
	private boolean yieldOnQueueEmpty;
	
	public InterruptableQueueDispatcher(AbstractQueue<T> queue) {
		this(queue, new StubCommand<T>(), false);
	}
	
	public InterruptableQueueDispatcher(AbstractQueue<T> queue, ICommand<T> command) {
		this(queue, command, false);
	}
	
	public InterruptableQueueDispatcher(AbstractQueue<T> queue,
            ICommand<T> command, boolean yieldOnQueueEmpty) {
		this.queue = queue;
		this.command = command;
		this.yieldOnQueueEmpty = yieldOnQueueEmpty;
	}
	
	@Override
	public void run() {
        if (InterruptableQueueDispatcher.log.isTraceEnabled()) {
		    InterruptableQueueDispatcher.log.trace("Starting " + this);
        }

		try {
			T item;
			while (!Thread.interrupted()) {
				while (!queue.isEmpty()) {
					item = queue.poll();
					command.perform(item);
				}
				if (yieldOnQueueEmpty) {
					Thread.yield();
				}
			}
		} catch (Exception e) {
			InterruptableQueueDispatcher.log.error("Error on dispatcher thread", e);
		}
	}

	public void setCommand(ICommand<T> command) {
		this.command = command;
	}
}
