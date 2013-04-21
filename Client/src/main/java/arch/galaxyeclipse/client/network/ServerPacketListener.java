package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.protocol.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.util.*;
import com.google.common.collect.*;
import lombok.extern.slf4j.*;

import java.util.*;

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
