package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.util.*;

import java.net.*;

public interface IClientNetworkManager extends IPacketSubscribable<IServerPacketListener> {
	void sendPacket(Packet packet);
	
	void disconnect(ICallback<Boolean> callback);
	
	void connect(SocketAddress address, ICallback<Boolean> callback);
}
