package arch.galaxyeclipse.shared;

/**
 * Represent common constants used in Client and Server modules.
 */
public class GeConstants {

    // Initial location location size is 30 000 x 30 000
    public static final int      RADIUS_DYNAMIC_OBJECT_QUERY        = 2500;

    // all values are in millis
    public static final long    DELAY_SHIP_STATE_REQUEST           = 50;
    public static final long    DELAY_DYNAMIC_OBJECTS_REQUEST      = 50;

    public static final long    DELAY_OBJECT_SPEED_UPDATE          = 50;
    public static final long    DELAY_OBJECT_POSITION_UPDATE       = 10;
    public static final long    DELAY_OBJECT_ROTATION_UPDATE       = 10;

    public static final long    DELAY_HP_REGEN                     = 300;
    public static final long    DELAY_ENERGY_REGEN                 = 100;

    public static final int     UNDEFINED_OBJECT_ID                 = -1;

    private GeConstants() {

    }
}
