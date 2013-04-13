package arch.galaxyeclipse.shared.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol.*;

public interface IPacketSender {
	void send(Packet packet);
}
