package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.network.*;

/**
 *
 */
public class PacketHandlerFactory {
    private PacketHandlerFactory() {

    }

    public static IStatefulPacketHandler createStatefulPacketHandler(
            IServerChannelHandler serverChannelHandler) {
        return new UnauthenticatedPacketHandler(serverChannelHandler);
    }
}
