package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.network.*;
import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.*;
import arch.galaxyeclipse.shared.thread.*;
import org.apache.log4j.*;
import org.jboss.netty.channel.*;

class ServerChannelHandler extends AbstractProtobufChannelHandler
		implements IServerChannelHandler {
	private static final Logger log = Logger.getLogger(ServerChannelHandler.class);

    private IMonitoringNetworkManager monitoringNetworkManager;
	private IPacketHandler packetHandler;
	
	public ServerChannelHandler() {		
		super(new StubDispatchCommand<Packet>());
		// Delegate incoming packets to the current packet handler
		getIncomingPacketDispatcher().setCommand(new ICommand<Packet>() {
			@Override
			public void perform(Packet packet) {
				packetHandler.handle(packet);
			}
		});
        monitoringNetworkManager = ContextHolder.INSTANCE.getBean(
                IMonitoringNetworkManager.class);
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
        super.channelConnected(ctx, e);
        log.debug("Server channel connected " + e.getChannel().hashCode());
        monitoringNetworkManager.registerServerChannelHandler(this);

		packetHandler = new UnauthenticatedPacketHandler(this);
	}

    @Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
        super.channelDisconnected(ctx, e);
        log.debug("Server channel disconnected");
        monitoringNetworkManager.unregisterServerChannelHandler(this);
	}	
	
	@Override
	public void setPacketHandler(IPacketHandler packetHandler) {
		log.debug("Client XXX packet handler changed to " + packetHandler);
		this.packetHandler = packetHandler;
	}
}