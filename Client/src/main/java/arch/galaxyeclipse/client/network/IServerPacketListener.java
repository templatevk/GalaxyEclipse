package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol.*;

import java.util.*;

public interface IServerPacketListener {
	void onPacketReceived(Packet packet);

	// Packet types willing to listen for
	List<Packet.Type> getPacketTypes();
}
