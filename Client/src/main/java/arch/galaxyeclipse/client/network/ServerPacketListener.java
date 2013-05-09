package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol;
import arch.galaxyeclipse.shared.protocol.GeProtocol.Packet;
import arch.galaxyeclipse.shared.util.LogUtils;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
public abstract class ServerPacketListener implements IServerPacketListener {
    public ServerPacketListener() {

    }

    protected abstract void onPacketReceivedImpl(GeProtocol.Packet packet);

    @Override
    public void onPacketReceived(Packet packet) {
        if (log.isDebugEnabled()) {
            log.debug(LogUtils.getObjectInfo(this) + " received packet " + packet.getType());
        }

        onPacketReceivedImpl(packet);
    }
}
