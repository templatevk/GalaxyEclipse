package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;

/**
 *
 */
public interface IGePacketProcessingListener extends IGeTypedPacketListener {
    void onProcessingComplete(GePacket packet);
}
