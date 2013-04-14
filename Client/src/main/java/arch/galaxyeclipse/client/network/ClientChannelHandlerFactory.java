package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.network.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.util.*;

class ClientChannelHandlerFactory implements IClientChannelHandlerFactory {
	public ClientChannelHandlerFactory() {
		
	}
	
	@Override
	public IChannelHandler createHandler(ICommand<Packet> incomingPacketDispatcherCommand) {
		return new ClientChannelHandler(incomingPacketDispatcherCommand);
	}
}
