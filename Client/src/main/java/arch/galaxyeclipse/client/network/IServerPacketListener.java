package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol.*;

import java.util.*;

public interface IServerPacketListener extends ITypedPacketListener {
	void onPacketReceived(Packet packet);
}
