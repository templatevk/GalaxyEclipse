package arch.galaxyeclipse.shared.util;

public class LogUtils {
	private LogUtils() {
		
	}
	
	public static String getObjectInfo(Object object) {
		// Represent info about the object using the log
		return object.getClass().getSimpleName() + "@" + object.hashCode();
	}
}
