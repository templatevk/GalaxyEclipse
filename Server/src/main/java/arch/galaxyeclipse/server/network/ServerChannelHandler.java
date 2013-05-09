package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.server.data.PlayerInfoHolder;
import arch.galaxyeclipse.server.network.handler.IStatefulPacketHandler;
import arch.galaxyeclipse.server.network.handler.PacketHandlerFactory;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.network.ProtobufChannelHandler;
import arch.galaxyeclipse.shared.protocol.GeProtocol.Packet;
import arch.galaxyeclipse.shared.util.ICommand;
import arch.galaxyeclipse.shared.util.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;

@Slf4j
class ServerChannelHandler extends ProtobufChannelHandler
		implements IServerChannelHandler {

    private PacketHandlerFactory packetHandlerFactory;
    private PlayerInfoHolder playerInfoHolder;

    private IMonitoringNetworkManager monitoringNetworkManager;
	private IStatefulPacketHandler statefulPacketHandler;
    private ICommand<Packet> incomingPacketDispatcherCommand;

    public ServerChannelHandler() {		
        monitoringNetworkManager = ContextHolder.getBean(
                IMonitoringNetworkManager.class);
        playerInfoHolder = new PlayerInfoHolder();
        incomingPacketDispatcherCommand = new ICommand<Packet>() {
            @Override
            public void perform(Packet packet) {
                if (log.isDebugEnabled()) {
                    log.debug(LogUtils.getObjectInfo(this) + " " + packet.getType());
                }
                statefulPacketHandler.handle(packet);
            }
        };
	}

    @Override
    protected ICommand<Packet> getIncomingPacketDispatcherCommand() {
        return incomingPacketDispatcherCommand;
    }

    @Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
        super.channelConnected(ctx, e);
        if (log.isDebugEnabled()) {
            log.debug("Server channel connected " + e.getChannel().hashCode());
        }

        monitoringNetworkManager.registerServerChannelHandler(this);

		statefulPacketHandler = PacketHandlerFactory.createStatefulPacketHandler(this);
	}

    @Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
        super.channelDisconnected(ctx, e);
        if (log.isDebugEnabled()) {
            log.debug("Server channel disconnected");
        }

        monitoringNetworkManager.unregisterServerChannelHandler(this);

        statefulPacketHandler.onChannelClosed();
	}
	
	@Override
	public void setStatefulPacketHandler(IStatefulPacketHandler statefulPacketHandler) {
		this.statefulPacketHandler = statefulPacketHandler;

        if (log.isDebugEnabled()) {
            log.debug("Client packet handler changed to "
                    + LogUtils.getObjectInfo(statefulPacketHandler));
        }
    }

    @Override
    public PlayerInfoHolder getPlayerInfoHolder() {
        return playerInfoHolder;
    }
}