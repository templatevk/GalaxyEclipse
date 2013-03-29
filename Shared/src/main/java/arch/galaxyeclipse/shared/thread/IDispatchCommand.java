package arch.galaxyeclipse.shared.thread;

public interface IDispatchCommand<T> {
	void perform(T t);
}
