package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.shared.network.*;

public interface IServerChannelHandler extends IChannelHandler {
	void setPacketHandler(IPacketHandler packetHandler);
}
