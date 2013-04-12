package arch.galaxyeclipse.server;

import org.apache.commons.codec.digest.*;
import org.junit.*;

/**
 * Base class for server tests.
 */
public abstract class AbstractJUnitServerTest {
    public static final String TEST_PLAYER_USERNAME = "test";
    public static final String TEST_PLAYER_PASSWORD_DECRYPTED = "test";
    public static final String TEST_PLAYER_PASSWORD_ENCRYPTED = DigestUtils.md5Hex("test");

    private static GalaxyEclipseServer server;

    @BeforeClass
    public static void startServer() {
        server = new GalaxyEclipseServer();
        server.preconfigure();
        server.start();
    }

    @AfterClass
    public static void shutdownServer() {
        server.stop();
    }
}