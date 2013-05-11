package arch.galaxyeclipse.shared.common;

/**
 * No action command.
 */
public class StubCommand<T> implements ICommand<T> {
	@Override
	public void perform(T argument) {

	}
}
