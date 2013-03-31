package arch.galaxyeclipse.server.network;

import org.apache.log4j.*;
import org.jboss.netty.channel.*;

import arch.galaxyeclipse.shared.network.*;
import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.Packet;
import arch.galaxyeclipse.shared.thread.*;

public class ServerChannelHandler extends AbstractProtobufChannelHandler 
		implements IServerChannelHandler {
	private static final Logger log = Logger.getLogger(ServerChannelHandler.class);
	
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
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		log.debug("Server channel connected " + e.getChannel().hashCode());
		super.channelConnected(ctx, e);

		packetHandler = new UnauthenticatedPacketHandler(this);
	}
	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		log.debug("Server channel disconnected");
		super.channelDisconnected(ctx, e);
	}	
	
	@Override
	public void setPacketHandler(IPacketHandler packetHandler) {
		log.debug("Client XXX packet handler changed to " + packetHandler);
		this.packetHandler = packetHandler;
	}
}