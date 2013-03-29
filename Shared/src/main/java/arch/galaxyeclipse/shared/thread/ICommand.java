package arch.galaxyeclipse.shared.thread;

public interface ICommand<T> {
	void perform(T t);
}
