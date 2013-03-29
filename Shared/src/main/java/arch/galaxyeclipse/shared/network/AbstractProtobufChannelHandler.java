package arch.galaxyeclipse.shared.network;

import java.util.concurrent.*;

import org.apache.log4j.*;
import org.jboss.netty.channel.*;

import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.Packet;
import arch.galaxyeclipse.shared.thread.*;

public class AbstractProtobufChannelHandler extends SimpleChannelHandler 
		implements IChannelHandler {
	private static final Logger log = Logger.getLogger(AbstractProtobufChannelHandler.class);
	
	private ConcurrentLinkedQueue<Packet> incomingPackets;
	private InterruptableQueueDispatcher<Packet> incomingPacketDispatcher;
	private ConcurrentLinkedQueue<Packet> outgoingPackets;
	private InterruptableQueueDispatcher<Packet> outgoingPacketDispatcher;
	
	public AbstractProtobufChannelHandler() {
		this(new StubDispatchCommand<Packet>());
	}
	
	public AbstractProtobufChannelHandler(IDispatchCommand<Packet> incomingPacketDispatcherCommand) {
		this(incomingPacketDispatcherCommand, null);
		outgoingPacketDispatcher.setCommand(new IDispatchCommand<Packet>() {
			@Override
			public void perform(Packet packet) {
				log.debug("Sending packet " + packet.getType());
				outgoingPackets.add(packet);
			}
		});
	}
	
	public AbstractProtobufChannelHandler(IDispatchCommand<Packet> incomingPacketDispatcherCommand,
			IDispatchCommand<Packet> outgoingPacketDispatcherCommand) {
		incomingPackets = new ConcurrentLinkedQueue<Packet>();	
		incomingPacketDispatcher = new InterruptableQueueDispatcher<Packet>(
				incomingPackets, incomingPacketDispatcherCommand);
		outgoingPackets = new ConcurrentLinkedQueue<Packet>();
		outgoingPacketDispatcher = new InterruptableQueueDispatcher<Packet>(
				outgoingPackets, outgoingPacketDispatcherCommand);
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
		incomingPackets.add((Packet)e.getMessage());
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		log.error("Network error", e.getCause());
	}
	
	@Override
	public void sendPacket(Packet packet) {
		outgoingPackets.add(packet);
	}
}
