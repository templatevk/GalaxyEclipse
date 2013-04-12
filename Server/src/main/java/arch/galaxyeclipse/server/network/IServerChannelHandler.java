package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.shared.network.*;

/**
 * Represents server channel handler, state pattern applied.
 */
interface IServerChannelHandler extends IChannelHandler {
	void setStatefulPacketHandler(IStatefulPacketHandler packetHandler);
}
