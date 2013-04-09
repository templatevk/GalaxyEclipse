package arch.galaxyeclipse.server;

import arch.galaxyeclipse.client.*;
import arch.galaxyeclipse.client.network.*;
import arch.galaxyeclipse.server.network.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.*;
import org.apache.log4j.*;
import org.junit.*;

import java.util.*;

import static arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.*;

/**
 *
 */
@Ignore
public class BroadcastClientTest extends AbstractServerTest {
    private static final Logger log = Logger.getLogger(BroadcastClientTest.class);

    private static final int CLIENTS_NUMBER = 100;
    private static final int WAIT_DELAY = 8000;

    private IServerNetworkManager serverNetworkManager;

    @Before
    public void initDependencies() {
        serverNetworkManager = ContextHolder.INSTANCE.getBean(IServerNetworkManager.class);
    }

    @Test
    public void testBroadcastMessage() throws InterruptedException {
        List<NoGuiGalaxyEclipseClient> clients = NoGuiGalaxyEclipseClient
                .connectClients(CLIENTS_NUMBER);
        IServerPacketListener listener = new IServerPacketListener() {
            @Override
            public void onPacketReceived(Packet packet) {
                log.info("Packet received");
            }

            @Override
            public List<GalaxyEclipseProtocol.Packet.Type> getPacketTypes() {
                return Arrays.asList(Packet.Type.CHAT_RECEIVE_MESSAGE);
            }
        };
        for (NoGuiGalaxyEclipseClient client : clients) {
            client.getClientNetworkManager().addListener(listener);
        }

        Thread.sleep(WAIT_DELAY);
        serverNetworkManager.sendBroadcast(Packet.newBuilder()
                .setType(Packet.Type.CHAT_RECEIVE_MESSAGE).setChatReceiveMessage(
                        ChatReceiveMessage.newBuilder()
                                .setMessage("Hello Client")
                                .setSender("Server")
                                .buildPartial())
                .build());
        Thread.sleep(WAIT_DELAY);
    }
}
