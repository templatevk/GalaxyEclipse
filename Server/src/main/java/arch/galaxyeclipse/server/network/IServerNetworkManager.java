package arch.galaxyeclipse.server.network;

public interface IServerNetworkManager {
	void startServer(String host, int port);
	
	void stopServer();
}
