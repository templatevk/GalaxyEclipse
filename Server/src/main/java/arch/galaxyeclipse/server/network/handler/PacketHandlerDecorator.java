package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.network.*;
import arch.galaxyeclipse.shared.protocol.*;
import lombok.extern.slf4j.*;

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
        if (log.isDebugEnabled()) {
            String username = getServerChannelHandler().getPlayerInfoHolder().getPlayer().getUsername();
            log.debug(username + " " + packet.getType());
        }
        return true;
    }

    @Override
    public void onChannelClosed() {
        onChannelClosedImpl();
        decoratedPacketHandler.onChannelClosed();
    }

    protected void onChannelClosedImpl() {

    }

    protected abstract boolean handleImp(GeProtocol.Packet packet);
}
