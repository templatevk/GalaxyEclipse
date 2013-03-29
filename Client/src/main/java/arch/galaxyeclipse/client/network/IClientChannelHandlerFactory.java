package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.*;
import arch.galaxyeclipse.shared.thread.*;

public interface IClientChannelHandlerFactory {
	IClientChannelHandler createHandler(IDispatchCommand<Packet> incomingPacketDispatcherCommand);
}
