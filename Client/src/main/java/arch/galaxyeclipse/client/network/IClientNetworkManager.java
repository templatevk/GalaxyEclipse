package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol.Packet;
import arch.galaxyeclipse.shared.common.ICallback;

import java.net.SocketAddress;

public interface IClientNetworkManager extends IPacketSubscribable<IServerPacketListener> {
	void sendPacket(Packet packet);
	
	void disconnect(ICallback<Boolean> callback);
	
	void connect(SocketAddress address, ICallback<Boolean> callback);
}
