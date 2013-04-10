package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.*;
import arch.galaxyeclipse.shared.thread.*;

import java.net.*;

public interface IClientNetworkManager {	
	void addListener(IServerPacketListener listener);
	
	void removeListener(IServerPacketListener listener);
	
	void removeListenerForType(IServerPacketListener listener, Packet.Type packetType);
	
	void sendPacket(Packet packet);
	
	void disconnect(ICallback<Boolean> callback);
	
	void connect(SocketAddress address, ICallback<Boolean> callback);
}
