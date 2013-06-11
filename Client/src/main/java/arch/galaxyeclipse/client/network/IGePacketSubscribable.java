package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;

/**
 *
 */
public interface IGePacketSubscribable<T> {
    void addPacketListener(T listener);

    void removeListenerForType(T listener, GePacket.Type packetType);

    void removePacketListener(T listener);
}
