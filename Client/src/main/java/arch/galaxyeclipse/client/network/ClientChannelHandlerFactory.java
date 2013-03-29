package arch.galaxyeclipse.client.network;

import org.springframework.stereotype.*;

import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.*;
import arch.galaxyeclipse.shared.thread.*;

@Component
public class ClientChannelHandlerFactory implements IClientChannelHandlerFactory {
	public ClientChannelHandlerFactory() {
		
	}
	
	@Override
	public IClientChannelHandler createHandler(
			IDispatchCommand<Packet> incomingPacketDispatcherCommand) {
		return new ClientChannelHandler(incomingPacketDispatcherCommand);
	}
}
