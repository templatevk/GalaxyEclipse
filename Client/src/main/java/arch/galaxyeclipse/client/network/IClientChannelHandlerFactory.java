package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.network.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.util.*;

public interface IClientChannelHandlerFactory {
	IChannelHandler createHandler(ICommand<Packet> incomingPacketDispatcherCommand);
}
