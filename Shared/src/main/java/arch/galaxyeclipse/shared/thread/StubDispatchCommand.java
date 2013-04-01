package arch.galaxyeclipse.shared.thread;

/**
 * No action command.
 */
public class StubDispatchCommand<T> implements ICommand<T> {
	@Override
	public void perform(T t) {

	}
}
