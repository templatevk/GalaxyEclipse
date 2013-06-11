package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.common.IGeCommand;
import arch.galaxyeclipse.shared.network.GeProtobufChannelHandler;
import arch.galaxyeclipse.shared.network.IGeChannelHandler;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;
import lombok.extern.slf4j.Slf4j;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;

@Slf4j
class GeClientChannelHandler extends GeProtobufChannelHandler
		implements IGeChannelHandler {
    private IGeCommand<GePacket> incomingPacketDispatcherCommand;

    public GeClientChannelHandler(IGeCommand<GePacket> incomingPacketDispatcherCommand) {
        this.incomingPacketDispatcherCommand = incomingPacketDispatcherCommand;
    }

    @Override
    protected IGeCommand<GePacket> getIncomingPacketDispatcherCommand() {
        return incomingPacketDispatcherCommand;
    }

    @Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
        super.channelConnected(ctx, e);
        if (GeClientChannelHandler.log.isInfoEnabled()) {
            GeClientChannelHandler.log.info("Client channel connected " + e.getChannel().hashCode());
        }
    }
	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		super.channelDisconnected(ctx, e);
        if (GeClientChannelHandler.log.isInfoEnabled()) {
            GeClientChannelHandler.log.info("Client channel disconnected " + e.getChannel().hashCode());
        }
    }
}