package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.server.data.GePlayerInfoHolder;
import arch.galaxyeclipse.server.network.handler.GePacketHandlerFactory;
import arch.galaxyeclipse.server.network.handler.IGeStatefulPacketHandler;
import arch.galaxyeclipse.shared.common.GeLogUtils;
import arch.galaxyeclipse.shared.common.IGeCommand;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import arch.galaxyeclipse.shared.network.GeProtobufChannelHandler;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;
import lombok.extern.slf4j.Slf4j;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;

@Slf4j
class GeServerChannelHandler extends GeProtobufChannelHandler
		implements IGeServerChannelHandler {

    private GePacketHandlerFactory packetHandlerFactory;
    private GePlayerInfoHolder playerInfoHolder;

    private IGeMonitoringNetworkManager monitoringNetworkManager;
	private IGeStatefulPacketHandler statefulPacketHandler;
    private IGeCommand<GePacket> incomingPacketDispatcherCommand;

    public GeServerChannelHandler() {
        monitoringNetworkManager = GeContextHolder.getBean(
                IGeMonitoringNetworkManager.class);
        playerInfoHolder = new GePlayerInfoHolder();
        incomingPacketDispatcherCommand = new IGeCommand<GePacket>() {
            @Override
            public void perform(GePacket packet) {
                if (GeServerChannelHandler.log.isDebugEnabled()) {
                    GeServerChannelHandler.log.debug(GeLogUtils.getObjectInfo(this) + " " + packet.getType());
                }
                statefulPacketHandler.handle(packet);
            }
        };
	}

    @Override
    protected IGeCommand<GePacket> getIncomingPacketDispatcherCommand() {
        return incomingPacketDispatcherCommand;
    }

    @Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
        super.channelConnected(ctx, e);
        if (GeServerChannelHandler.log.isDebugEnabled()) {
            GeServerChannelHandler.log.debug("Server channel connected " + e.getChannel().hashCode());
        }

        monitoringNetworkManager.registerServerChannelHandler(this);

		statefulPacketHandler = GePacketHandlerFactory.createStatefulPacketHandler(this);
	}

    @Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
        super.channelDisconnected(ctx, e);
        if (GeServerChannelHandler.log.isDebugEnabled()) {
            GeServerChannelHandler.log.debug("Server channel disconnected");
        }

        monitoringNetworkManager.unregisterServerChannelHandler(this);

        statefulPacketHandler.onChannelClosed();
	}
	
	@Override
	public void setStatefulPacketHandler(IGeStatefulPacketHandler statefulPacketHandler) {
		this.statefulPacketHandler = statefulPacketHandler;

        if (GeServerChannelHandler.log.isDebugEnabled()) {
            GeServerChannelHandler.log.debug("Client packet handler changed to "
                    + GeLogUtils.getObjectInfo(statefulPacketHandler));
        }
    }

    @Override
    public GePlayerInfoHolder getPlayerInfoHolder() {
        return playerInfoHolder;
    }
}