package arch.galaxyeclipse.shared;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

/**
 *
 */
@Slf4j
public enum GeEnvType {
    DEV,
    PROD;

    public static final GeEnvType CURRENT;

    private static final String PROPERTIES_FILE = "env.properties";
    private static final String PROPERTIES_DIR = "env";

    static {
        GeEnvType currentEnvType;
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(new File(PROPERTIES_DIR, PROPERTIES_FILE)));

            currentEnvType = valueOf(properties.getProperty("env.type"));
        } catch (Exception e) {
            GeEnvType.log.error("Error determining environment type", e);
            currentEnvType = DEV;
        }
        CURRENT = currentEnvType;
    }
}
