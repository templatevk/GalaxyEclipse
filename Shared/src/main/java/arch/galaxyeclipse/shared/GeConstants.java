package arch.galaxyeclipse.shared;

/**
 * Represent common constants used in Client and Server modules.
 */
public class GeConstants {

    // Initial location location size is 30 000 x 30 000
    public static final int RADIUS_DYNAMIC_OBJECT_QUERY = 2500;

    public static final long CLIENT_ACTION_ROTATION_DELAY_MILLISECONDS = 10;
    public static final long CLIENT_ACTION_MOVE_DELAY_MILLISECONDS = 10;
    public static final long CLIENT_ACTION_SPEED_DELAY_MILLISECONDS = 50;

    public static final long CLIENT_REQUEST_DYNAMIC_OBJECTS_INTERVAL_MILLISECONDS = 50;
    public static final long CLIENT_REQUEST_SHIP_STATE_INTERVAL_MILLISECONDS = 50;

    public static final long DELAY_HP_REGEN = 300;
    public static final long DELAY_ENERGY_REGEN = 100;

    private GeConstants() {

    }
}
