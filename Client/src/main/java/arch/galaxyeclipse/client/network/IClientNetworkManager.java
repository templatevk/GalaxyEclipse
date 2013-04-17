package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.util.*;

import java.net.*;

public interface IClientNetworkManager {	
	void addPacketListener(IServerPacketListener listener);
	
	void removePacketListener(IServerPacketListener listener);
	
	void removeListenerForType(IServerPacketListener listener, Packet.Type packetType);
	
	void sendPacket(Packet packet);
	
	void disconnect(ICallback<Boolean> callback);
	
	void connect(SocketAddress address, ICallback<Boolean> callback);
}
