package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;

import java.util.List;

/**
 *
 */
public interface IGeTypedPacketListener {
    List<GePacket.Type> getPacketTypes();
}
