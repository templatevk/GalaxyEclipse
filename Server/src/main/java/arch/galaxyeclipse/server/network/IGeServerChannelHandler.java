package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.server.data.GePlayerInfoHolder;
import arch.galaxyeclipse.server.network.handler.IGeStatefulPacketHandler;
import arch.galaxyeclipse.shared.network.IGeChannelHandler;

/**
 * Represents server channel handler, state pattern applied.
 */
public interface IGeServerChannelHandler extends IGeChannelHandler {
	void setStatefulPacketHandler(IGeStatefulPacketHandler packetHandler);

    GePlayerInfoHolder getPlayerInfoHolder();
}
