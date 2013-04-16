package arch.galaxyeclipse.shared;

/**
 * Represent common constants used in Client and Server modules.
 */
public class SharedInfo {
    public static final String HOST = "localhost";
	public static final int PORT = 3724;

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
