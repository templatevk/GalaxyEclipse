package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.common.IGeCommand;
import arch.galaxyeclipse.shared.network.IGeChannelHandler;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;

class GeClientChannelHandlerFactory {

    private GeClientChannelHandlerFactory() {

    }

    public static IGeChannelHandler createHandler(IGeCommand<GePacket> incomingPacketDispatcherCommand) {
        return new GeClientChannelHandler(incomingPacketDispatcherCommand);
    }
}
