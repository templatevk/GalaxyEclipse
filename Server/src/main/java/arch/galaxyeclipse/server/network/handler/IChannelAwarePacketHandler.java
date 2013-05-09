package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.network.IServerChannelHandler;

/**
 *
 */
interface IChannelAwarePacketHandler extends IStatefulPacketHandler {
    IServerChannelHandler getServerChannelHandler();
}
