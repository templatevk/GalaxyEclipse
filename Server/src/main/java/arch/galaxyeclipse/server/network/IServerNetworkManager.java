package arch.galaxyeclipse.server.network;

/**
 *
 */
public interface IServerNetworkManager {
	// Binds the server socket to the address
    void startServer(String host, int port);
	
	void stopServer();
}
