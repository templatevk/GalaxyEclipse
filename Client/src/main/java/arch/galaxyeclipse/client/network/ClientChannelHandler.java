package arch.galaxyeclipse.client.network;

import org.apache.log4j.*;
import org.jboss.netty.channel.*;

import arch.galaxyeclipse.shared.network.*;
import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.Packet;
import arch.galaxyeclipse.shared.thread.*;

public class ClientChannelHandler extends AbstractProtobufChannelHandler 
		implements IClientChannelHandler {	
	private static final Logger log = Logger.getLogger(ClientChannelHandler.class);
	
	private Channel channel;
	
	public ClientChannelHandler(IDispatchCommand<Packet> incomingPacketDispatcherCommand) {
		super(incomingPacketDispatcherCommand);
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
		throws Exception {
		channel = e.getChannel();
		getIncomingPackets().clear();
		getIncomingPacketDispatcher().start();
		getOutgoingPackets().clear();
		getOutgoingPacketDispatcher().start();
		log.info("Client channel connected");
	}
	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
		ChannelStateEvent e) throws Exception {
		getOutgoingPacketDispatcher().interrupt();
		log.info("Client channel disconnected");
	}
	
	@Override
	public void disconnect(final ICallback<Boolean> callback) {
		channel.disconnect().addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				future.getChannel().close();
				callback.onOperationComplete(future.isSuccess());
			}
		});
	}
	
	@Override
	public boolean isConnected() {
		return channel != null && channel.isConnected();
	}
}