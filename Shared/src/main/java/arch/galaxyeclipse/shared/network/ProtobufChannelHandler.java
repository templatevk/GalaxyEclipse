package arch.galaxyeclipse.shared.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.thread.*;
import arch.galaxyeclipse.shared.util.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.jboss.netty.channel.*;

import java.util.concurrent.*;

/**
 * Base class for handling @see Channel data. Puts sent/received packets into queues
 * and processes the into seperate threads.
 */
@Slf4j
public abstract class ProtobufChannelHandler extends SimpleChannelHandler
		implements IChannelHandler {
	private ConcurrentLinkedQueue<Packet> incomingPackets;
	private InterruptableQueueDispatcher<Packet> incomingPacketDispatcher;
	private ConcurrentLinkedQueue<Packet> outgoingPackets;
	private InterruptableQueueDispatcher<Packet> outgoingPacketDispatcher;
    private ICommand<Packet> outgoingPacketDispatcherCommand;
    private volatile IPacketSender packetSender;

    @Getter(AccessLevel.PROTECTED)
    private Channel channel;

    protected ProtobufChannelHandler() {
        outgoingPacketDispatcherCommand = new ICommand<Packet>() {
            @Override
            public void perform(Packet packet) {
                if (ProtobufChannelHandler.log.isTraceEnabled()) {
                    ProtobufChannelHandler.log.trace("Sending packet from the outgoing queue " + packet.getType());
                }
                packetSender.send(packet);
            }
        };

        outgoingPackets = new ConcurrentLinkedQueue<>();
        incomingPackets = new ConcurrentLinkedQueue<>();

		// Drop packets because of no connection
		packetSender = new StubPacketSender();
	}

    protected abstract ICommand<Packet> getIncomingPacketDispatcherCommand();

    private void preparePacketDispatchers() {
        incomingPackets.clear();
        outgoingPackets.clear();

        outgoingPacketDispatcher = new InterruptableQueueDispatcher<>(
                outgoingPackets, outgoingPacketDispatcherCommand);
        incomingPacketDispatcher = new InterruptableQueueDispatcher<>(
                incomingPackets, getIncomingPacketDispatcherCommand());

        incomingPacketDispatcher.start();
        outgoingPacketDispatcher.start();
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
		channel = e.getChannel();

        packetSender = new ChannelPacketSender(channel);
        preparePacketDispatchers();
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
		    ChannelStateEvent e) throws Exception {
		packetSender = new StubPacketSender();

        outgoingPacketDispatcher.interrupt();
		incomingPacketDispatcher.interrupt();
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		// Get the packet and put to the received packets queue
		Packet packet = (Packet)e.getMessage();
        if (ProtobufChannelHandler.log.isTraceEnabled()) {
            ProtobufChannelHandler.log.trace(LogUtils.getObjectInfo(this) + " put packet "
			    	+ packet.getType() + " to the incoming queue");
        }
		incomingPackets.add(packet);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		ProtobufChannelHandler.log.error("Network error", e.getCause());
	}

	@Override
	public void sendPacket(Packet packet) {
        if (ProtobufChannelHandler.log.isTraceEnabled()) {
            ProtobufChannelHandler.log.trace(LogUtils.getObjectInfo(this) + " put packet "
				    + packet.getType() + " to the outgoing queue");
        }
		outgoingPackets.add(packet);
	}
}
