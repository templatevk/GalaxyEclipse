package arch.galaxyeclipse.shared.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;

public interface IGePacketSender {

    void send(GePacket packet);
}
