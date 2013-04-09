package arch.galaxyeclipse.server;

import arch.galaxyeclipse.server.data.util.*;
import arch.galaxyeclipse.server.network.*;
import arch.galaxyeclipse.shared.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.types.*;
import org.apache.log4j.*;

public class GalaxyEclipseServer {
    private static final Logger log = Logger.getLogger(GalaxyEclipseServer.class);

	private IServerNetworkManager serverNetworkManager;
    private IDictionaryTypesMapperHelper dictionaryTypesMapperHelper;
    private DictionaryTypesMapper dictionaryTypesMapper;
	
	public GalaxyEclipseServer() {

	}

    // Configuration and dependency resolution
    public void preconfigure() {
        // Initializing the system
        PropertyConfigurator.configure("log4j.properties");
        ContextHolder.INSTANCE.getClass();

        // Resolving dependencies
        serverNetworkManager = ContextHolder.INSTANCE.getBean(IServerNetworkManager.class);
        dictionaryTypesMapperHelper = ContextHolder.INSTANCE.getBean(
                IDictionaryTypesMapperHelper.class);
        dictionaryTypesMapper = ContextHolder.INSTANCE.getBean(DictionaryTypesMapper.class);

        // Dependencies initialization
        dictionaryTypesMapperHelper.fillAll(dictionaryTypesMapper);
    }

    // Performing full startup of the application
	public void start() {
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