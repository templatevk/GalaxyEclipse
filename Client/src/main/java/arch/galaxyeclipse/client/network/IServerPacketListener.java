package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol.Packet;

public interface IServerPacketListener extends ITypedPacketListener {
	void onPacketReceived(Packet packet);
}
