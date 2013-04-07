package arch.galaxyeclipse.server;

import org.apache.log4j.*;

import arch.galaxyeclipse.server.network.*;
import arch.galaxyeclipse.shared.*;
import arch.galaxyeclipse.shared.inject.*;

public class GalaxyEclipseServer {
    private static final Logger log = Logger.getLogger(GalaxyEclipseServer.class);

	private IServerNetworkManager serverNetworkManager;
	
	public GalaxyEclipseServer() {

	}

    // Configuration and dependency resolution
    public void preconfigure() {
        PropertyConfigurator.configure("log4j.properties");
        serverNetworkManager = SpringContextHolder.CONTEXT.getBean(IServerNetworkManager.class);
    }

    // Performing full startup of the application
	public void start() {
	    // Binding the server on the address
		serverNetworkManager.startServer(SharedInfo.HOST, SharedInfo.PORT);
	}

    // Performing full shutdown of the application
	public void stop() {
		serverNetworkManager.stopServer();
	}
	
	public static void main(String[] args) throws Exception {
        GalaxyEclipseServer server = new GalaxyEclipseServer();
        try {
            server.preconfigure();
            server.start();
        } catch (Exception e) {
            log.error("Error during server startup", e);
        }
	}
}