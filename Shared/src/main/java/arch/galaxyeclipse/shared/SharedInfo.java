package arch.galaxyeclipse.shared;

/**
 * Represent common constants used in Client and Server modules.
 */
public class SharedInfo {
    public static final String HOST = "localhost";
	public static final int PORT = 3724;

    // Initial location location size is 1000.00 x 1000.00
    public static final float DYNAMIC_OBJECT_QUERY_RADIUS = 50;
    public static final float LOCATION_TO_SCREEN_COORDS_COEF = 30;

    public static final float SHIP_MOVE_SPEED_TO_LOCATION_COORDS_COEF = 0.01f;

    public static final long CLIENT_ACTION_ROTATION_DELAY_MILLISECONDS = 10;
    public static final long CLIENT_ACTION_MOVE_DELAY_MILLISECONDS = 10;
    public static final long CLIENT_ACTION_SPEED_DELAY_MILLISECONDS = 100;

    private SharedInfo() {

    }
}
