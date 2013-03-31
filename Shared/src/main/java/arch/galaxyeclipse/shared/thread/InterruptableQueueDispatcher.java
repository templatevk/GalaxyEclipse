package arch.galaxyeclipse.shared.thread;

import java.util.*;

import org.apache.log4j.*;

/**
 * Thread processing the queue passed applying the passed command to the queue items.
 */
public class InterruptableQueueDispatcher<T> extends Thread {
	private static final Logger log = Logger.getLogger(InterruptableQueueDispatcher.class);
	
	private AbstractQueue<T> queue;
	private ICommand<T> command;
	private boolean yield;
	
	public InterruptableQueueDispatcher(AbstractQueue<T> queue) {
		this(queue, new StubDispatchCommand<T>(), false);
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
		log.debug("Starting " + this);
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
			log.error("Error on dispatcher thread", e);
		}
	}

	public void setCommand(ICommand<T> command) {
		this.command = command;
	}
}
