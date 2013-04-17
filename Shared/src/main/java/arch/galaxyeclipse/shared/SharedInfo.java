package arch.galaxyeclipse.shared;

/**
 * Represent common constants used in Client and Server modules.
 */
public class SharedInfo {
    public static final String HOST = "localhost";
	public static final int PORT = 3724;

    // Initial location location size is 1000.00 x 1000.00
    public static final double DYNAMIC_OBJECT_QUERY_RADIUS = 100;
    public static final double CLIENT_POINT_TO_LOCATION_POINT = 100;

    private SharedInfo() {

    }

    public static EnvType getEnvType() {
        String env = System.getProperty("env");
        if (env != null) {
            return EnvType.valueOf(env);
        }
        return EnvType.DEV;
    }
}
// TODO Hibernate versioning, pixel to position & location size mapping                6
