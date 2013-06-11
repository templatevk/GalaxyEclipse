package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.network.IGeServerChannelHandler;

/**
 *
 */
interface IGeChannelAwarePacketHandler extends IGeStatefulPacketHandler {

    IGeServerChannelHandler getServerChannelHandler();
}
