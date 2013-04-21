package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.network.*;

/**
 *
 */
public class PacketHandlerFactory {
    public PacketHandlerFactory() {

    }

    public IStatefulPacketHandler createStatefulPacketHandler(
            IServerChannelHandler serverChannelHandler) {
        return new UnauthenticatedPacketHandler(serverChannelHandler);
    }

    IChannelAwarePacketHandler createStatefulPacketHandler(
            IServerChannelHandler serverChannelHandler, GameModeType gameModeType) {
        switch (gameModeType) {
            case FLIGHT:
                return new DynamicObjectsRequestHandler(new ShipStateRequestHandler(
                        new FlightPacketHandler(serverChannelHandler)));
        }
        return null;
    }
}
