package arch.galaxyeclipse.client.network.sender;

import arch.galaxyeclipse.client.network.IClientNetworkManager;
import arch.galaxyeclipse.client.network.SubscribableServerPacketListener;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.Packet;
import arch.galaxyeclipse.shared.thread.DelayedRunnableTask;
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
abstract class RepeatablePacketSender extends SubscribableServerPacketListener
        implements Runnable {

    @Getter(AccessLevel.PROTECTED)
    private IClientNetworkManager clientNetworkManager;
    private Packet.Type packetType;
    @Delegate
    private DelayedRunnableTask packetSendingTask;

    public RepeatablePacketSender(Packet.Type packetType,
            long sleepMilliseconds) {
        this.packetType = packetType;
        clientNetworkManager = ContextHolder.getBean(IClientNetworkManager.class);
        packetSendingTask = new DelayedRunnableTask(sleepMilliseconds,
                new Runnable() {
                    @Override
                    public void run() {
                        sendRequest();
                    }
                }, true, true);
    }

    @Override
    public List<Packet.Type> getPacketTypes() {
        return Arrays.asList(packetType);
    }

    protected abstract void sendRequest();
}

