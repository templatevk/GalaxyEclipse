package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.network.IGeServerChannelHandler;

/**
 *
 */
public class GePacketHandlerFactory {
    public GePacketHandlerFactory() {

    }

    public static IGeStatefulPacketHandler createStatefulPacketHandler(
            IGeServerChannelHandler serverChannelHandler) {
        return new GeUnauthenticatedPacketHandler(serverChannelHandler);
    }

    static IGeChannelAwarePacketHandler createStatefulPacketHandler(
            IGeServerChannelHandler serverChannelHandler, GeGameModeType gameModeType) {
        switch (gameModeType) {
            case FLIGHT:
                return new GeDynamicObjectsRequestHandler(
                        new GeShipStateRequestHandler(
                        new GeClientActionHandler(
                        new GeChatMessageHandler(
                        new GeFlightPacketHandler(serverChannelHandler)))));
        }
        return null;
    }
}
