package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.Packet;

public interface IPacketHandler {
	void handle(Packet packet);
}
