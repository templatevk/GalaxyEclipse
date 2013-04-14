package arch.galaxyeclipse.shared.thread;

import arch.galaxyeclipse.shared.util.*;
import lombok.extern.slf4j.*;

import java.util.*;

/**
 * Thread processing the queue passed applying the passed command to the queue items.
 */
@Slf4j
public class InterruptableQueueDispatcher<T> extends Thread {
	private AbstractQueue<T> queue;
	private ICommand<T> command;
	private boolean yield;
	
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
		this.yield = yieldOnQueueEmpty;
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
				if (yield) {
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
