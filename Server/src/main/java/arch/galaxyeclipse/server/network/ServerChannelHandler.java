package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.server.network.handler.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.network.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.util.*;
import lombok.extern.slf4j.*;
import org.jboss.netty.channel.*;

import static arch.galaxyeclipse.server.network.handler.PacketHandlerFactory.*;

@Slf4j
class ServerChannelHandler extends AbstractProtobufChannelHandler
		implements IServerChannelHandler {

    private PacketHandlerFactory packetHandlerFactory;
    private PlayerInfoHolder playerInfoHolder;

    private IMonitoringNetworkManager monitoringNetworkManager;
	private IStatefulPacketHandler statefulPacketHandler;
    private ICommand<Packet> incomingPacketDispatcherCommand;

    public ServerChannelHandler() {		
        monitoringNetworkManager = ContextHolder.INSTANCE.getBean(
                IMonitoringNetworkManager.class);
        playerInfoHolder = new PlayerInfoHolder();
        incomingPacketDispatcherCommand = new ICommand<Packet>() {
            @Override
            public void perform(Packet packet) {

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

		statefulPacketHandler = packetHandlerFactory.createStatefulPacketHandler(this);
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