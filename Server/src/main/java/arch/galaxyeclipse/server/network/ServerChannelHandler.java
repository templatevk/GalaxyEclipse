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
		IDispatchCommand<Packet> dispatchCommand = new IDispatchCommand<Packet>() {
			@Override
			public void perform(Packet packet) {
				packetHandler.handle(packet);
			}
		};
		getIncomingPacketDispatcher().setCommand(dispatchCommand);
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		packetHandler = new UnauthenticatedPacketHandler(this);
		getIncomingPacketDispatcher().start();
		getOutgoingPacketDispatcher().start();
		log.info("Server channel connected");
	}
	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		log.info("Server channel disconnected");
	}	
	
	@Override
	public void setPacketHandler(IPacketHandler packetHandler) {
		this.packetHandler = packetHandler;
		log.debug("Client XXX packet handler changed to " 
				+ packetHandler.getClass().getSimpleName());
	}
}