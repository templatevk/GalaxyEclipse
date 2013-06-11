package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.network.IGeServerChannelHandler;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
abstract class GePacketHandlerDecorator extends GeStatefulPacketHandler
        implements IGeChannelAwarePacketHandler {

    private IGeChannelAwarePacketHandler decoratedPacketHandler;
    private IGeServerChannelHandler serverChannelHandler;

    GePacketHandlerDecorator(IGeChannelAwarePacketHandler decoratedPacketHandler) {
        this.decoratedPacketHandler = decoratedPacketHandler;
        serverChannelHandler = decoratedPacketHandler.getServerChannelHandler();
    }

    @Override
    public IGeServerChannelHandler getServerChannelHandler() {
        return serverChannelHandler;
    }

    @Override
    public final boolean handle(GePacket packet) {
        if (!handleImp(packet)) {
            return decoratedPacketHandler.handle(packet);
        }
        if (GePacketHandlerDecorator.log.isTraceEnabled()) {
            String username = getServerChannelHandler().getPlayerInfoHolder().getPlayer().getUsername();
            GePacketHandlerDecorator.log.trace(username + " " + packet.getType());
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

    protected abstract boolean handleImp(GePacket packet);
}
