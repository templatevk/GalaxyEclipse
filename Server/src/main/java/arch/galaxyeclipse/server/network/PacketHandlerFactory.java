package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.server.network.*;

/**
 *
 */
public class PacketHandlerFactory {
    public PacketHandlerFactory() {
    }

    public IStatefulPacketHandler createStatefulPacketHandler(
            IServerChannelHandler serverChannelHandler,
            StatefulPacketHandlerType statefulPacketHandlerType) {

        switch (statefulPacketHandlerType) {
            case UNAUTHENTICATED_CLIENT_HANDLER:
                return new UnauthenticatedPacketHandler(serverChannelHandler);
        }
        return null;
    }

    static enum StatelessPacketHandlerType {
        STATIC_RESOURCES_HANDLER;
    }

    static enum StatefulPacketHandlerType {
        UNAUTHENTICATED_CLIENT_HANDLER;
    }
}
