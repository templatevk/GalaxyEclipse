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
    private Multimap<Packet.Type, ICallback<Packet>> processingListeners;

    public ServerPacketListener() {
        processingListeners = HashMultimap.create();
    }

    protected abstract void onPacketReceivedImpl(GeProtocol.Packet packet);

    @Override
    public void onPacketReceived(Packet packet) {
        if (log.isInfoEnabled()) {
            log.info(LogUtils.getObjectInfo(this) + " received packet " + packet.getType());
        }

        onPacketReceivedImpl(packet);

        Collection<ICallback<Packet>> callbacks = processingListeners.removeAll(packet.getType());
        for (ICallback<Packet> processingListener : callbacks) {
            processingListener.onOperationComplete(packet);
        }
    }

    public void subscribeOnce(ICallback<Packet> callback, Packet.Type packetType) {
        processingListeners.put(packetType, callback);
    }
}
