package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.protocol.*;

import java.util.*;

/**
 *
 */
public interface ITypedPacketListener {
    List<GeProtocol.Packet.Type> getPacketTypes();
}
