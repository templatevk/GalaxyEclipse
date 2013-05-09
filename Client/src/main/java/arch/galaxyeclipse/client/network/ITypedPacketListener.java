package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol;

import java.util.List;

/**
 *
 */
public interface ITypedPacketListener {
    List<GeProtocol.Packet.Type> getPacketTypes();
}
