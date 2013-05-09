package arch.galaxyeclipse.shared.common;

public interface ICommand<T> {
	void perform(T argument);
}
