package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;

/**
 *
 */
public interface IGeServerNetworkManager {
	// Binds the server socket to the address
    void startServer();
	
	void stopServer();

    void sendBroadcast(GePacket packet);
}
