package arch.galaxyeclipse.client.network.sender;

import arch.galaxyeclipse.client.network.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.*;
import arch.galaxyeclipse.shared.util.*;
import lombok.*;
import lombok.extern.slf4j.*;

import java.util.*;

/**
 *
 */
@Slf4j
abstract class RepeatablePacketSender extends SubscribableServerPacketListener {
    @Getter(AccessLevel.PROTECTED)
    private IClientNetworkManager clientNetworkManager;
    @Delegate
    private ThreadWorker threadWorker;
    private GeProtocol.Packet.Type packetType;
    private int sleepMilliseconds;

    public RepeatablePacketSender(GeProtocol.Packet.Type packetType,
            int sleepMilliseconds) {

        this.packetType = packetType;
        this.sleepMilliseconds = sleepMilliseconds;

        threadWorker = new ThreadWorker();
        clientNetworkManager = ContextHolder.getBean(IClientNetworkManager.class);
    }

    @Override
    public List<GeProtocol.Packet.Type> getPacketTypes() {
        return Arrays.asList(packetType);
    }

    @Override
    protected void onPacketReceivedImpl(GeProtocol.Packet packet) {
        processPacket(packet);
        try {
            Thread.sleep(sleepMilliseconds);
        } catch (InterruptedException e) {
            RepeatablePacketSender.log.error("Error during sleeping", e);
        }
        sendRequest();
    }

    protected abstract void processPacket(GeProtocol.Packet packet);

    protected abstract void sendRequest();


    private class ThreadWorker extends Thread {
        private ThreadWorker() {

        }

        @Override
        public void run() {
            try {
                sendRequest();
                while (!Thread.interrupted()) {

                }
                clientNetworkManager.removePacketListener(RepeatablePacketSender.this);
            } catch (Exception e) {
                RepeatablePacketSender.log.error(LogUtils.getObjectInfo(this), e);
            }
        }
    }
}

