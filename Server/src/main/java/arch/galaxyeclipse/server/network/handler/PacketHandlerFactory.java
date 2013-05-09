package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.network.IServerChannelHandler;

/**
 *
 */
public class PacketHandlerFactory {
    public PacketHandlerFactory() {

    }

    public static IStatefulPacketHandler createStatefulPacketHandler(
            IServerChannelHandler serverChannelHandler) {
        return new UnauthenticatedPacketHandler(serverChannelHandler);
    }

    static IChannelAwarePacketHandler createStatefulPacketHandler(
            IServerChannelHandler serverChannelHandler, GameModeType gameModeType) {
        switch (gameModeType) {
            case FLIGHT:
                return new DynamicObjectsRequestHandler(
                        new ShipStateRequestHandler(
                        new ClientActionHandler(
                        new ChatMessageHandler(
                        new FlightPacketHandler(serverChannelHandler)))));
        }
        return null;
    }
}
