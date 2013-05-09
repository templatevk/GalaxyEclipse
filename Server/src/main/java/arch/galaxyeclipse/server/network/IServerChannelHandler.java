package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.server.data.PlayerInfoHolder;
import arch.galaxyeclipse.server.network.handler.IStatefulPacketHandler;
import arch.galaxyeclipse.shared.network.IChannelHandler;

/**
 * Represents server channel handler, state pattern applied.
 */
public interface IServerChannelHandler extends IChannelHandler {
	void setStatefulPacketHandler(IStatefulPacketHandler packetHandler);

    PlayerInfoHolder getPlayerInfoHolder();
}
