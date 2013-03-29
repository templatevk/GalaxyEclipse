package arch.galaxyeclipse.shared.network;

import java.util.concurrent.*;

import org.apache.log4j.*;
import org.jboss.netty.channel.*;

import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.Packet;
import arch.galaxyeclipse.shared.thread.*;
import arch.galaxyeclipse.shared.util.*;

public abstract class AbstractProtobufChannelHandler extends SimpleChannelHandler 
		implements IChannelHandler {
	private static final Logger log = Logger.getLogger(AbstractProtobufChannelHandler.class);
	
	private ConcurrentLinkedQueue<Packet> incomingPackets;
	private InterruptableQueueDispatcher<Packet> incomingPacketDispatcher;
	private ConcurrentLinkedQueue<Packet> outgoingPackets;
	private InterruptableQueueDispatcher<Packet> outgoingPacketDispatcher;
	private Channel channel;
	private volatile IPacketSender packetSender;
	
	protected AbstractProtobufChannelHandler(ICommand<Packet> incomingPacketDispatcherCommand) {
		incomingPackets = new ConcurrentLinkedQueue<Packet>();	
		incomingPacketDispatcher = new InterruptableQueueDispatcher<Packet>(
				incomingPackets, incomingPacketDispatcherCommand);
		outgoingPackets = new ConcurrentLinkedQueue<Packet>();
		outgoingPacketDispatcher = new InterruptableQueueDispatcher<Packet>(
				outgoingPackets, new ICommand<Packet>() {
					@Override
					public void perform(Packet packet) {
						log.debug("Sending packet from the outgoing queue " + packet.getType());
						packetSender.send(packet);
					}
				});
		packetSender = new StubPacketSender();
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
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
		throws Exception {
		channel = e.getChannel();
		packetSender = new ChannelPacketSender(channel);
		getIncomingPacketDispatcher().start();
		getOutgoingPacketDispatcher().start();
	}
	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
		ChannelStateEvent e) throws Exception {
		packetSender = new StubPacketSender();
		getOutgoingPacketDispatcher().interrupt();
		getIncomingPacketDispatcher().interrupt();
		getIncomingPackets().clear();
		getOutgoingPackets().clear();
	}
	
	protected ConcurrentLinkedQueue<Packet> getIncomingPackets() {
		return incomingPackets;
	}
	
	protected InterruptableQueueDispatcher<Packet> getIncomingPacketDispatcher() {
		return incomingPacketDispatcher;
	}
	
	protected ConcurrentLinkedQueue<Packet> getOutgoingPackets() {
		return outgoingPackets;
	}
	
	protected InterruptableQueueDispatcher<Packet> getOutgoingPacketDispatcher() {
		return outgoingPacketDispatcher;
	}	
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		Packet packet = (Packet)e.getMessage();
		log.debug(LogUtils.getObjectInfo(this) + " put packet " 
				+ packet.getType() + " to the incoming queue");
		incomingPackets.add(packet);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		log.error("Network error", e.getCause());
	}
	
	@Override
	public void sendPacket(Packet packet) {
		log.debug(LogUtils.getObjectInfo(this) + " put packet " 
				+ packet.getType() + " to the outgoing queue");
		outgoingPackets.add(packet);
	}

	protected Channel getChannel() {
		return channel;
	}
}
