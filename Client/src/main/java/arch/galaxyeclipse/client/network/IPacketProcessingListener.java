package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.protocol.*;

/**
 *
 */
public interface IPacketProcessingListener extends ITypedPacketListener {
    void onProcessingComplete(GeProtocol.Packet packet);
}
