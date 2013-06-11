package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.common.GeLogUtils;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 */
@Slf4j
public abstract class GeSubscribableServerPacketListener extends GeServerPacketListener
        implements IGeSubscribableServerPacketListener {

    private Multimap<GePacket.Type, IGePacketProcessingListener> processingListeners;

    protected GeSubscribableServerPacketListener() {
        processingListeners = HashMultimap.create();
    }

    @Override
    public void onPacketReceived(GePacket packet) {
        super.onPacketReceived(packet);

        Collection<IGePacketProcessingListener> callbacks =
                new ArrayList<>(processingListeners.get(packet.getType()));
        for (IGePacketProcessingListener processingListener : callbacks) {
            processingListener.onProcessingComplete(packet);
        }
    }

    @Override
    public void addPacketListener(IGePacketProcessingListener listener) {
        for (GePacket.Type packetType : listener.getPacketTypes()) {
            processingListeners.put(packetType, listener);

            if (GeSubscribableServerPacketListener.log.isDebugEnabled()) {
                GeSubscribableServerPacketListener.log.debug(GeLogUtils.getObjectInfo(listener)
                        + " subscribed for " + packetType.toString() + " processing listening");
            }
        }
    }

    public void removeListenerForType(IGePacketProcessingListener listener,
            GePacket.Type packetType) {
        processingListeners.get(packetType).remove(listener);

        if (GeSubscribableServerPacketListener.log.isDebugEnabled()) {
            GeSubscribableServerPacketListener.log.debug(GeLogUtils.getObjectInfo(listener)
                    + " unsubscribed from " + packetType.toString() + " processing listening");
        }
    }

    public void removePacketListener(IGePacketProcessingListener listener) {
        for (GePacket.Type packetType : listener.getPacketTypes()) {
            removeListenerForType(listener, packetType);
        }
    }
}
