package arch.galaxyeclipse.shared.common;

public class GeLogUtils {
	private GeLogUtils() {
		
	}
	
	public static String getObjectInfo(Object object) {
		// Represent info about the object using the log
		return object.getClass().getSimpleName() + "@" + object.hashCode();
	}
}
