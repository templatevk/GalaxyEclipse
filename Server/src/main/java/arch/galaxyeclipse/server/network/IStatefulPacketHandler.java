package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.*;

/**
 * Strategy for handling player packets.
 */
public interface IStatefulPacketHandler {
	void handle(Packet packet);

    void onChannelClosed();
}
