package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.shared.protocol.GeProtocol.*;

/**
 * Strategy for handling player packets.
 */
public interface IStatefulPacketHandler {
	void handle(Packet packet);

    void onChannelClosed();
}
