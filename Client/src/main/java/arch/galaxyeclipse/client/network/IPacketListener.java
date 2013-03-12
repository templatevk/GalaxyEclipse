package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.protocol.*;

public interface IPacketListener {
	void onPacketReceived(GalaxyEclipseProtocol.Packet packet);
}
