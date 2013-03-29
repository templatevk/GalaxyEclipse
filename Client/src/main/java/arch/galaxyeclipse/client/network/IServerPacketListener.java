package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.Packet;

public interface IServerPacketListener {
	void onPacketReceived(Packet packet);
	Packet.Type getPacketType();
}
