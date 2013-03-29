package arch.galaxyeclipse.client.network;

import org.apache.log4j.*;
import org.jboss.netty.channel.*;

import arch.galaxyeclipse.shared.network.*;
import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.Packet;
import arch.galaxyeclipse.shared.thread.*;

public class ClientChannelHandler extends AbstractProtobufChannelHandler 
		implements IChannelHandler {
	private static final Logger log = Logger.getLogger(ClientChannelHandler.class);
	
	public ClientChannelHandler(ICommand<Packet> incomingPacketDispatcherCommand) {
		super(incomingPacketDispatcherCommand);
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		log.info("Client channel connected " + e.getChannel().hashCode());
		super.channelConnected(ctx, e);
	}
	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		log.info("Client channel disconnected " + e.getChannel().hashCode());
		super.channelDisconnected(ctx, e);
	}
}