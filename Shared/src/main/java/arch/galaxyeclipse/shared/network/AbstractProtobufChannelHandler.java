package arch.galaxyeclipse.shared.network;

import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.*;
import arch.galaxyeclipse.shared.thread.*;
import arch.galaxyeclipse.shared.util.*;
import org.apache.log4j.*;
import org.jboss.netty.channel.*;

import java.util.concurrent.*;

/**
 * Base class for handling @see Channel data. Puts sent/received packets into queues
 * and processes the into seperate threads.
 */
public abstract class AbstractProtobufChannelHandler extends SimpleChannelHandler
		implements IChannelHandler {
	private static final Logger log = Logger.getLogger(AbstractProtobufChannelHandler.class);

	// Queue holding the packets received
	private ConcurrentLinkedQueue<Packet> incomingPackets;
	// Thread for processing the received packets
	private InterruptibleQueueDispatcher<Packet> incomingPacketDispatcher;
	// Queue holding the packets sent
	private ConcurrentLinkedQueue<Packet> outgoingPackets;
	// Thread for processing the sent packets
	private InterruptibleQueueDispatcher<Packet> outgoingPacketDispatcher;
	private Channel channel;
	private volatile IPacketSender packetSender;

	protected AbstractProtobufChannelHandler(ICommand<Packet> incomingPacketDispatcherCommand) {
		// Initialize the queues and their processors
		incomingPackets = new ConcurrentLinkedQueue<Packet>();
		incomingPacketDispatcher = new InterruptibleQueueDispatcher<Packet>(
				incomingPackets, incomingPacketDispatcherCommand);
		outgoingPackets = new ConcurrentLinkedQueue<Packet>();
		outgoingPacketDispatcher = new InterruptibleQueueDispatcher<Packet>(
				outgoingPackets, new ICommand<Packet>() {
					@Override
					public void perform(Packet packet) {
						log.debug("Sending packet from the outgoing queue " + packet.getType());
						packetSender.send(packet);
					}
				});
		// Drop packets because of no connection
		packetSender = new StubPacketSender();
	}

	@Override
	public void disconnect(final ICallback<Boolean> callback) {
        if (isConnected()) {
            channel.disconnect().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    future.getChannel().close();
                    callback.onOperationComplete(future.isSuccess());
                }
		    });
        }
	}

	@Override
	public boolean isConnected() {
		return channel != null && channel.isConnected();
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
		    throws Exception {
		// Initializing the channel and passing it to packet sender
		channel = e.getChannel();
		packetSender = new ChannelPacketSender(channel);

		// Starting packet queues' processors
		getIncomingPacketDispatcher().start();
		getOutgoingPacketDispatcher().start();
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
		    ChannelStateEvent e) throws Exception {
		// Initializing sender dropping the packets
		packetSender = new StubPacketSender();

		// Stopping packet queues' processors
		getOutgoingPacketDispatcher().interrupt();
		getIncomingPacketDispatcher().interrupt();

		// Clearing packet queues
		getIncomingPackets().clear();
		getOutgoingPackets().clear();
	}

	protected ConcurrentLinkedQueue<Packet> getIncomingPackets() {
		return incomingPackets;
	}

	protected InterruptibleQueueDispatcher<Packet> getIncomingPacketDispatcher() {
		return incomingPacketDispatcher;
	}

	protected ConcurrentLinkedQueue<Packet> getOutgoingPackets() {
		return outgoingPackets;
	}

	protected InterruptibleQueueDispatcher<Packet> getOutgoingPacketDispatcher() {
		return outgoingPacketDispatcher;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		// Get the packet and put to the received packets queue
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
