package arch.galaxyeclipse.server;

import arch.galaxyeclipse.server.data.*;
import arch.galaxyeclipse.server.network.*;
import arch.galaxyeclipse.shared.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.types.*;
import ch.qos.logback.classic.*;
import ch.qos.logback.core.util.*;
import lombok.extern.slf4j.*;
import org.slf4j.*;

@Slf4j
public class GalaxyEclipseServer {
    private IServerNetworkManager serverNetworkManager;
    private DictionaryTypesMapper dictionaryTypesMapper;
	
	public GalaxyEclipseServer() {

	}

    public void preconfigure() {
        StatusPrinter.print((LoggerContext) LoggerFactory.getILoggerFactory());
        ContextHolder.INSTANCE.getClass();

        serverNetworkManager = ContextHolder.getBean(IServerNetworkManager.class);
        dictionaryTypesMapper = ContextHolder.getBean(DictionaryTypesMapper.class);

        DictionaryTypesMapperHelper.fillAll(dictionaryTypesMapper);

        if (log.isInfoEnabled()) {
            log.info("Server initialized for " + EnvType.CURRENT.toString() + " environment");
        }
    }

	public void start() {
		serverNetworkManager.startServer(SharedInfo.HOST, SharedInfo.PORT);
	}

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