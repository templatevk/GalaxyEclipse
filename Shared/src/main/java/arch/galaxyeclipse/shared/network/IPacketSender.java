package arch.galaxyeclipse.shared.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol.Packet;

public interface IPacketSender {
	void send(Packet packet);
}
