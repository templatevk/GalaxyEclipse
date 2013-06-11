package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;

public interface IGeServerPacketListener extends IGeTypedPacketListener {
	void onPacketReceived(GePacket packet);
}
