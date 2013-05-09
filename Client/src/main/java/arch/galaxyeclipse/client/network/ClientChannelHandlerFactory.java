package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.network.IChannelHandler;
import arch.galaxyeclipse.shared.protocol.GeProtocol.Packet;
import arch.galaxyeclipse.shared.util.ICommand;

class ClientChannelHandlerFactory {
	private ClientChannelHandlerFactory() {
		
	}

	public static IChannelHandler createHandler(ICommand<Packet> incomingPacketDispatcherCommand) {
		return new ClientChannelHandler(incomingPacketDispatcherCommand);
	}
}
