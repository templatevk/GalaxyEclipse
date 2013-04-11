package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.client.network.*;
import arch.galaxyeclipse.server.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.*;
import lombok.extern.slf4j.*;
import mockit.*;
import org.junit.*;

import java.util.*;

import static arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.*;

/**
 *
 */
@Slf4j
public class BroadcastClientTest extends AbstractServerTest {
    private static final int CLIENTS_NUMBER = 10;
    private static final int WAIT_DELAY = 4000;

    private IServerNetworkManager serverNetworkManager;

    @Before
    public void initDependencies() {
        serverNetworkManager = ContextHolder.INSTANCE.getBean(IServerNetworkManager.class);
    }

    @Test
    public void testBroadcastMessage() throws InterruptedException {
        new ServerPacketListener();

        final StubPacketListener listener = new StubPacketListener();
        List<NoGuiGalaxyEclipseClient> clients = NoGuiGalaxyEclipseClient
                .connectClients(CLIENTS_NUMBER);
        for (NoGuiGalaxyEclipseClient client : clients) {
            client.getClientNetworkManager().addListener(listener);
        }

        Thread.sleep(WAIT_DELAY);
        BroadcastClientTest.log.info("Performing broadcast");
        serverNetworkManager.sendBroadcast(Packet.newBuilder()
                .setType(Packet.Type.CHAT_RECEIVE_MESSAGE).setChatReceiveMessage(
                        ChatReceiveMessage.newBuilder()
                                .setMessage("Hello Client")
                                .setSender("Server")
                                .buildPartial())
                .build());
        Thread.sleep(WAIT_DELAY);

        new Verifications() {{
            listener.onPacketReceived(Packet.getDefaultInstance()); times = CLIENTS_NUMBER;
        }};
    }

    private static class StubPacketListener implements IServerPacketListener {
        @Override
        public void onPacketReceived(Packet packet) {

        }

        @Override
        public List<Packet.Type> getPacketTypes() {
            return null;
        }
    }

    private static class ServerPacketListener extends MockUp<IServerPacketListener>
            implements IServerPacketListener {
        {
            log.info("Initializing mock");
        }

        @Override
        public void onPacketReceived(Packet packet) {
            log.info("Mock listener received " + packet.getType());
        }

        @Override
        public List<Packet.Type> getPacketTypes() {
            return Arrays.asList(Packet.Type.CHAT_RECEIVE_MESSAGE);
        }
    }
}
