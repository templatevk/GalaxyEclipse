package arch.galaxyeclipse.shared.network;

import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.Packet;

public interface IPacketSender {
	void send(Packet packet);
}
