package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.network.*;
import arch.galaxyeclipse.shared.protocol.*;

/**
 *
 */
abstract class PacketHandlerDecorator extends StatefulPacketHandler
        implements IChannelAwarePacketHandler {

    private IChannelAwarePacketHandler decoratedPacketHandler;
    private IServerChannelHandler serverChannelHandler;

    PacketHandlerDecorator(IChannelAwarePacketHandler decoratedPacketHandler) {
        this.decoratedPacketHandler = decoratedPacketHandler;
        this.serverChannelHandler = decoratedPacketHandler.getServerChannelHandler();
    }

    @Override
    public IServerChannelHandler getServerChannelHandler() {
        return serverChannelHandler;
    }

    @Override
    public boolean handle(GeProtocol.Packet packet) {
        if (!handleImp(packet)) {
            return decoratedPacketHandler.handle(packet);
        }
        return true;
    }

    protected abstract boolean handleImp(GeProtocol.Packet packet);
}
