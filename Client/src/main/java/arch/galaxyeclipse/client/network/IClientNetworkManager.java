package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.common.ICallback;
import arch.galaxyeclipse.shared.protocol.GeProtocol.Packet;

public interface IClientNetworkManager extends IPacketSubscribable<IServerPacketListener> {
	void sendPacket(Packet packet);
	
	void disconnect(ICallback<Boolean> callback);
	
	void connect(ICallback<Boolean> callback);
}
