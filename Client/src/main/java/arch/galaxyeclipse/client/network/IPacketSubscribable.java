package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol;

/**
 *
 */
public interface IPacketSubscribable<T> {
    void addPacketListener(T listener);

    void removeListenerForType(T listener, GeProtocol.Packet.Type packetType);

    void removePacketListener(T listener);
}
