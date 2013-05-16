package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol.Packet;

/**
 *
 */
public interface IServerNetworkManager {
	// Binds the server socket to the address
    void startServer();
	
	void stopServer();

    void sendBroadcast(Packet packet);
}
