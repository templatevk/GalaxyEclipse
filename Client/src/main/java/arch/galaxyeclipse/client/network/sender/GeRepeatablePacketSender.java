package arch.galaxyeclipse.client.network.sender;

import arch.galaxyeclipse.client.network.GeSubscribableServerPacketListener;
import arch.galaxyeclipse.client.network.IGeClientNetworkManager;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;
import arch.galaxyeclipse.shared.thread.GeDelayedRunnableTask;
import lombok.AccessLevel;
import lombok.Delegate;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
@Slf4j
abstract class GeRepeatablePacketSender extends GeSubscribableServerPacketListener
        implements Runnable {

    @Getter(AccessLevel.PROTECTED)
    private IGeClientNetworkManager clientNetworkManager;
    private GePacket.Type packetType;
    @Delegate
    private GeDelayedRunnableTask packetSendingTask;

    public GeRepeatablePacketSender(GePacket.Type packetType,
            long sleepMilliseconds) {
        this.packetType = packetType;
        clientNetworkManager = GeContextHolder.getBean(IGeClientNetworkManager.class);
        packetSendingTask = new GeDelayedRunnableTask(sleepMilliseconds,
                new Runnable() {
                    @Override
                    public void run() {
                        sendRequest();
                    }
                }, true, true);
    }

    @Override
    public List<GePacket.Type> getPacketTypes() {
        return Arrays.asList(packetType);
    }

    protected abstract void sendRequest();
}

