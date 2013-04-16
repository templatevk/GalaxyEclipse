package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.server.network.handler.*;
import arch.galaxyeclipse.shared.network.*;

/**
 * Represents server channel handler, state pattern applied.
 */
public interface IServerChannelHandler extends IChannelHandler {
	void setStatefulPacketHandler(IStatefulPacketHandler packetHandler);

    PlayerInfoHolder getPlayerInfoHolder();
}
