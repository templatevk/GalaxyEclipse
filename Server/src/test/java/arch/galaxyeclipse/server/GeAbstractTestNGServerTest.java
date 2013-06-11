package arch.galaxyeclipse.server;

import org.apache.commons.codec.digest.DigestUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 * Base class for server tests.
 */
public abstract class GeAbstractTestNGServerTest {

    public static final String TEST_PLAYER_USERNAME = "test";
    public static final String TEST_PLAYER_PASSWORD_DECRYPTED = "test";
    public static final String TEST_PLAYER_PASSWORD_ENCRYPTED = DigestUtils.md5Hex("test");

    private static GeServer server;

    @BeforeClass
    public static void startServer() {
        System.setProperty("host", "localhost");
        System.setProperty("port", "7777");
        server = new GeServer();
        server.start();
    }

    @AfterClass
    public static void shutdownServer() {
        server.stop();
    }
}