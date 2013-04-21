package arch.galaxyeclipse.shared;

/**
 * Represent common constants used in Client and Server modules.
 */
public class SharedInfo {
    public static final String HOST = "localhost";
	public static final int PORT = 3724;

    // Initial location location size is 1000.00 x 1000.00
    public static final float DYNAMIC_OBJECT_QUERY_RADIUS = 50;
    public static final float CLIENT_POINT_TO_LOCATION_POINT = 100;

    private SharedInfo() {

    }
}
