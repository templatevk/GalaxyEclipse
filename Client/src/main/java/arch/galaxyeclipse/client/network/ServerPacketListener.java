package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.protocol.*;
import arch.galaxyeclipse.shared.util.*;
import lombok.extern.slf4j.*;

/**
 *
 */
@Slf4j
public abstract class ServerPacketListener implements IServerPacketListener {
    public ServerPacketListener() {

    }

    protected abstract void onPacketReceivedImpl(GeProtocol.Packet packet);

    @Override
    public void onPacketReceived(GeProtocol.Packet packet) {
        if (log.isInfoEnabled()) {
            log.info(LogUtils.getObjectInfo(this) + " received packet " + packet.getType());
        }

        onPacketReceivedImpl(packet);
    }
}
