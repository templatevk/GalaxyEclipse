package arch.galaxyeclipse.shared;

/**
 * Represent common constants used in Client and Server modules.
 */
public class GeConstants {

    // Initial location location size is 10 000 x 10 000
    public static final int RADIUS_DYNAMIC_OBJECT_QUERY = 1000;

    public static final long CLIENT_ACTION_ROTATION_DELAY_MILLISECONDS = 10;
    public static final long CLIENT_ACTION_MOVE_DELAY_MILLISECONDS = 10;
    public static final long CLIENT_ACTION_SPEED_DELAY_MILLISECONDS = 50;

    public static final long CLIENT_REQUEST_DYNAMIC_OBJECTS_INTERVAL_MILLISECONDS = 50;
    public static final long CLIENT_REQUEST_SHIP_STATE_INTERVAL_MILLISECONDS = 50;

    private GeConstants() {

    }
}
