package arch.galaxyeclipse.shared.util;

public class LogUtils {
	private LogUtils() {
		
	}
	
	public static String getObjectInfo(Object object) {
		return object.getClass().getSimpleName() + "@" + object.hashCode();
	}
}
