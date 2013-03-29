package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.network.*;
import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.Packet;
import arch.galaxyeclipse.shared.thread.*;

public interface IClientChannelHandlerFactory {
	IChannelHandler createHandler(ICommand<Packet> incomingPacketDispatcherCommand);
}
