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

    @Mocked StubServerPacketListener listener;
    @Test
    public void testBroadcastMessage() throws InterruptedException {
        new MockUp<StubServerPacketListener>() {
            @Mock
            public void onPacketReceived(Packet packet) {
                if (BroadcastClientTest.log.isInfoEnabled()) {
                    BroadcastClientTest.log.info("Packet received");
                }
            }

            @Mock
            public List<GalaxyEclipseProtocol.Packet.Type> getPacketTypes() {
                return Arrays.asList(Packet.Type.CHAT_RECEIVE_MESSAGE);
            }
        };

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
}
