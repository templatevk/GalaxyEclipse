package arch.galaxyeclipse.shared.thread;

import java.util.*;

import org.apache.log4j.*;

/**
 * Поток обрабатывающий сообщения из очереди.
 */
public class InterruptableQueueDispatcher<T> extends Thread {
	private static final Logger log = Logger.getLogger(InterruptableQueueDispatcher.class);
	
	private AbstractQueue<T> queue;
	private IDispatchCommand<T> command;
	private boolean yield;
	
	public InterruptableQueueDispatcher(AbstractQueue<T> queue) {
		this(queue, new StubDispatchCommand<T>(), false);
	}
	
	public InterruptableQueueDispatcher(AbstractQueue<T> queue, IDispatchCommand<T> command) {
		this(queue, command, false);
	}
	
	public InterruptableQueueDispatcher(AbstractQueue<T> queue, 
			IDispatchCommand<T> command, boolean yieldOnQueueEmpty) {
		this.queue = queue;
		this.command = command;
		this.yield = yieldOnQueueEmpty;
	}
	
	@Override
	public void run() {
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

	public void setCommand(IDispatchCommand<T> command) {
		this.command = command;
	}
}
