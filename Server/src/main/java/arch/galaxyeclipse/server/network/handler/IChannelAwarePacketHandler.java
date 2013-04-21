package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.network.*;

/**
 *
 */
interface IChannelAwarePacketHandler extends IStatefulPacketHandler {
    IServerChannelHandler getServerChannelHandler();
}
