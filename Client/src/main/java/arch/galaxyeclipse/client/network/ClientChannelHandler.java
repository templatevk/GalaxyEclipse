package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.network.*;
import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.*;
import arch.galaxyeclipse.shared.thread.*;
import lombok.extern.slf4j.*;
import org.jboss.netty.channel.*;

@Slf4j
class ClientChannelHandler extends AbstractProtobufChannelHandler
		implements IChannelHandler {
    public ClientChannelHandler(ICommand<Packet> incomingPacketDispatcherCommand) {
		super(incomingPacketDispatcherCommand);
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