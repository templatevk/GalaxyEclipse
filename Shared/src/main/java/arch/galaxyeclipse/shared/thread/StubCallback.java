package arch.galaxyeclipse.shared.thread;

/**
 * No action callback.
 */
public class StubCallback<T> implements ICallback<T> {
	public StubCallback() {
		
	}
	
	@Override
	public void onOperationComplete(T object) {
		
	}
}
