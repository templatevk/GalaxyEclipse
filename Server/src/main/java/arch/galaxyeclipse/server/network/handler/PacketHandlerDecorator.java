package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.network.IServerChannelHandler;
import arch.galaxyeclipse.shared.protocol.GeProtocol;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
abstract class PacketHandlerDecorator extends StatefulPacketHandler
        implements IChannelAwarePacketHandler {

    private IChannelAwarePacketHandler decoratedPacketHandler;
    private IServerChannelHandler serverChannelHandler;

    PacketHandlerDecorator(IChannelAwarePacketHandler decoratedPacketHandler) {
        this.decoratedPacketHandler = decoratedPacketHandler;
        serverChannelHandler = decoratedPacketHandler.getServerChannelHandler();
    }

    @Override
    public IServerChannelHandler getServerChannelHandler() {
        return serverChannelHandler;
    }

    @Override
    public final boolean handle(GeProtocol.Packet packet) {
        if (!handleImp(packet)) {
            return decoratedPacketHandler.handle(packet);
        }
        if (log.isTraceEnabled()) {
            String username = getServerChannelHandler().getPlayerInfoHolder().getPlayer().getUsername();
            log.trace(username + " " + packet.getType());
        }
        return true;
    }

    @Override
    public final void onChannelClosed() {
        onChannelClosedImpl();
        decoratedPacketHandler.onChannelClosed();
    }

    protected void onChannelClosedImpl() {

    }

    protected abstract boolean handleImp(GeProtocol.Packet packet);
}
