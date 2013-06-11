package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.common.GeLogUtils;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
public abstract class GeServerPacketListener implements IGeServerPacketListener {
    public GeServerPacketListener() {

    }

    protected abstract void onPacketReceivedImpl(GePacket packet);

    @Override
    public void onPacketReceived(GePacket packet) {
        if (GeServerPacketListener.log.isDebugEnabled()) {
            GeServerPacketListener.log.debug(GeLogUtils.getObjectInfo(this) + " received packet " + packet.getType());
        }

        onPacketReceivedImpl(packet);
    }
}
