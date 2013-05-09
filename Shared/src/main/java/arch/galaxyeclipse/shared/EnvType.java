package arch.galaxyeclipse.shared;

import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.util.Properties;

/**
 *
 */
@Slf4j
public enum EnvType {
    DEV,
    DEV_UI,
    PROD;

    public static final EnvType CURRENT;

    private static final String PROPERTIES_PATH = "env.properties";

    static {
        EnvType currentEnvType;
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(PROPERTIES_PATH));

            currentEnvType = valueOf(properties.getProperty("env.type"));
        } catch (Exception e) {
            log.error("Error determining environment type ");
            currentEnvType = DEV;
        }
        CURRENT = currentEnvType;
    }
}
