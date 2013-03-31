package arch.galaxyeclipse.server;

import org.apache.log4j.*;

import arch.galaxyeclipse.server.network.*;
import arch.galaxyeclipse.shared.*;
import arch.galaxyeclipse.shared.inject.*;

public class GalaxyEclipseServer {
	private IServerNetworkManager serverNetworkManager;
	
	public GalaxyEclipseServer() {
		PropertyConfigurator.configure("log4j.properties");
		serverNetworkManager = SpringContextHolder.CONTEXT.getBean(IServerNetworkManager.class);
	}
	
	public void start() { // Binding the server on the address
		serverNetworkManager.startServer(SharedInfo.HOST, SharedInfo.PORT);
	}
	
	public void stop() {
		serverNetworkManager.stopServer();
	}
	
	public static void main(String[] args) throws Exception {
		new GalaxyEclipseServer().start();
	}
}