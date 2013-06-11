package arch.galaxyeclipse.shared.network;

import arch.galaxyeclipse.shared.common.GeLogUtils;
import arch.galaxyeclipse.shared.common.IGeCallback;
import arch.galaxyeclipse.shared.common.IGeCommand;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;
import arch.galaxyeclipse.shared.thread.GeInterruptableQueueDispatcher;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jboss.netty.channel.*;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Base class for handling @see Channel data. Puts sent/received GePackets into queues
 * and processes the into seperate threads.
 */
@Slf4j
public abstract class GeProtobufChannelHandler extends SimpleChannelHandler
        implements IGeChannelHandler {

    private ConcurrentLinkedQueue<GePacket> incomingGePackets;
    private GeInterruptableQueueDispatcher<GePacket> incomingGePacketDispatcher;
    private ConcurrentLinkedQueue<GePacket> outgoingGePackets;
    private GeInterruptableQueueDispatcher<GePacket> outgoingGePacketDispatcher;
    private IGeCommand<GePacket> outgoingGePacketDispatcherCommand;
    private volatile IGePacketSender GePacketSender;

    @Getter(AccessLevel.PROTECTED)
    private Channel channel;

    protected GeProtobufChannelHandler() {
        outgoingGePacketDispatcherCommand = new IGeCommand<GePacket>() {
            @Override
            public void perform(GePacket GePacket) {
                if (GeProtobufChannelHandler.log.isTraceEnabled()) {
                    GeProtobufChannelHandler.log.trace("Sending GePacket from the outgoing queue " + GePacket.getType());
                }
                GePacketSender.send(GePacket);
            }
        };

        outgoingGePackets = new ConcurrentLinkedQueue<>();
        incomingGePackets = new ConcurrentLinkedQueue<>();

        // Drop GePackets because of no connection
        GePacketSender = new GeStubPacketSender();
    }

    protected abstract IGeCommand<GePacket> getIncomingPacketDispatcherCommand();

    private void prepareGePacketDispatchers() {
        incomingGePackets.clear();
        outgoingGePackets.clear();

        outgoingGePacketDispatcher = new GeInterruptableQueueDispatcher<>(
                outgoingGePackets, outgoingGePacketDispatcherCommand);
        incomingGePacketDispatcher = new GeInterruptableQueueDispatcher<>(
                incomingGePackets, getIncomingPacketDispatcherCommand());

        incomingGePacketDispatcher.start();
        outgoingGePacketDispatcher.start();
    }

    @Override
    public void disconnect(final IGeCallback<Boolean> callback) {
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
        GePacketSender = new GeChannelPacketSender(channel);
        prepareGePacketDispatchers();
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx,
            ChannelStateEvent e) throws Exception {
        GePacketSender = new GeStubPacketSender();
        outgoingGePacketDispatcher.interrupt();
        incomingGePacketDispatcher.interrupt();
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
            throws Exception {
        // Get the GePacket and put to the received GePackets queue
        GePacket GePacket = (GePacket) e.getMessage();
        if (GeProtobufChannelHandler.log.isTraceEnabled()) {
            GeProtobufChannelHandler.log.trace(GeLogUtils.getObjectInfo(this) + " put GePacket "
                    + GePacket.getType() + " to the incoming queue");
        }
        incomingGePackets.add(GePacket);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
        GeProtobufChannelHandler.log.error("Network error", e.getCause());
    }

    @Override
    public void sendPacket(GePacket GePacket) {
        if (GeProtobufChannelHandler.log.isTraceEnabled()) {
            GeProtobufChannelHandler.log.trace(GeLogUtils.getObjectInfo(this) + " put GePacket "
                    + GePacket.getType() + " to the outgoing queue");
        }
        outgoingGePackets.add(GePacket);
    }
}
