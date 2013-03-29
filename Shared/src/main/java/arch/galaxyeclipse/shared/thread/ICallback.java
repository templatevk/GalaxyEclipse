package arch.galaxyeclipse.shared.thread;

public interface ICallback<T> {
	void onOperationComplete(T object);
}
