package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol.*;

/**
 *
 */
public interface IServerNetworkManager {
	// Binds the server socket to the address
    void startServer(String host, int port);
	
	void stopServer();

    void sendBroadcast(Packet packet);
}
