package arch.galaxyeclipse.shared.thread;

import arch.galaxyeclipse.shared.common.GeStubCommand;
import arch.galaxyeclipse.shared.common.IGeCommand;
import lombok.extern.slf4j.Slf4j;

import java.util.AbstractQueue;

/**
 * Thread processing the queue passed applying the passed command to the queue items.
 */
@Slf4j
public class GeInterruptableQueueDispatcher<T> extends GeAbstractRunnable implements GeRunnable {
	private AbstractQueue<T> queue;
	private IGeCommand<T> command;
	private boolean yieldOnQueueEmpty;
	
	public GeInterruptableQueueDispatcher(AbstractQueue<T> queue) {
		this(queue, new GeStubCommand<T>(), false);
	}
	
	public GeInterruptableQueueDispatcher(AbstractQueue<T> queue, IGeCommand<T> command) {
		this(queue, command, false);
	}
	
	public GeInterruptableQueueDispatcher(AbstractQueue<T> queue,
            IGeCommand<T> command, boolean yieldOnQueueEmpty) {
		this.queue = queue;
		this.command = command;
		this.yieldOnQueueEmpty = yieldOnQueueEmpty;
	}
	
	@Override
	public void run() {
        if (GeInterruptableQueueDispatcher.log.isTraceEnabled()) {
		    GeInterruptableQueueDispatcher.log.trace("Starting " + this);
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
			GeInterruptableQueueDispatcher.log.error("Error on dispatcher thread", e);
		}
	}

	public void setCommand(IGeCommand<T> command) {
		this.command = command;
	}
}
