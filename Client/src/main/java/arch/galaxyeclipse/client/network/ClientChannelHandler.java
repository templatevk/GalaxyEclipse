package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.network.IChannelHandler;
import arch.galaxyeclipse.shared.network.ProtobufChannelHandler;
import arch.galaxyeclipse.shared.protocol.GeProtocol.Packet;
import arch.galaxyeclipse.shared.util.ICommand;
import lombok.extern.slf4j.Slf4j;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;

@Slf4j
class ClientChannelHandler extends ProtobufChannelHandler
		implements IChannelHandler {
    private ICommand<Packet> incomingPacketDispatcherCommand;

    public ClientChannelHandler(ICommand<Packet> incomingPacketDispatcherCommand) {
        this.incomingPacketDispatcherCommand = incomingPacketDispatcherCommand;
    }

    @Override
    protected ICommand<Packet> getIncomingPacketDispatcherCommand() {
        return incomingPacketDispatcherCommand;
    }

    @Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
        super.channelConnected(ctx, e);
        if (log.isInfoEnabled()) {
            log.info("Client channel connected " + e.getChannel().hashCode());
        }
    }
	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		super.channelDisconnected(ctx, e);
        if (log.isInfoEnabled()) {
            log.info("Client channel disconnected " + e.getChannel().hashCode());
        }
    }
}