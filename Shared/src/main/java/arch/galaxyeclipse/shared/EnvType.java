package arch.galaxyeclipse.shared;

import lombok.extern.slf4j.*;

import java.io.*;
import java.util.*;

/**
 *
 */
@Slf4j
public enum EnvType {
    DEV,
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
