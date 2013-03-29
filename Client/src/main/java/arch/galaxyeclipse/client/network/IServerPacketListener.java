package arch.galaxyeclipse.client.network;

import java.util.*;

import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.Packet;

public interface IServerPacketListener {
	void onPacketReceived(Packet packet);
	
	List<Packet.Type> getPacketTypes();
}
