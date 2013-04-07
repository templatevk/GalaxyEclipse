package arch.galaxyeclipse.server;

import org.junit.*;

/**
 *
 */
public abstract class AbstractServerTest {
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