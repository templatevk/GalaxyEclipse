package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol;
import arch.galaxyeclipse.shared.common.LogUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 */
@Slf4j
public abstract class SubscribableServerPacketListener extends ServerPacketListener
        implements ISubscribableServerPacketListener {

    private Multimap<GeProtocol.Packet.Type, IPacketProcessingListener> processingListeners;

    protected SubscribableServerPacketListener() {
        processingListeners = HashMultimap.create();
    }

    @Override
    public void onPacketReceived(GeProtocol.Packet packet) {
        super.onPacketReceived(packet);

        Collection<IPacketProcessingListener> callbacks =
                new ArrayList<>(processingListeners.get(packet.getType()));
        for (IPacketProcessingListener processingListener : callbacks) {
            processingListener.onProcessingComplete(packet);
        }
    }

    @Override
    public void addPacketListener(IPacketProcessingListener listener) {
        for (GeProtocol.Packet.Type packetType : listener.getPacketTypes()) {
            processingListeners.put(packetType, listener);

            if (SubscribableServerPacketListener.log.isDebugEnabled()) {
                SubscribableServerPacketListener.log.debug(LogUtils.getObjectInfo(listener)
                        + " subscribed for " + packetType.toString() + " processing listening");
            }
        }
    }

    public void removeListenerForType(IPacketProcessingListener listener,
            GeProtocol.Packet.Type packetType) {
        processingListeners.get(packetType).remove(listener);

        if (SubscribableServerPacketListener.log.isDebugEnabled()) {
            SubscribableServerPacketListener.log.debug(LogUtils.getObjectInfo(listener)
                    + " unsubscribed from " + packetType.toString() + " processing listening");
        }
    }

    public void removePacketListener(IPacketProcessingListener listener) {
        for (GeProtocol.Packet.Type packetType : listener.getPacketTypes()) {
            removeListenerForType(listener, packetType);
        }
    }
}
