package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.types.DictionaryTypesMapper;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents client without gui.
 */
public class NoGuiGalaxyEclipseClient {
    @Getter
    private TestClientNetworkManager clientNetworkManager;
    @Getter
    private DictionaryTypesMapper dictionaryTypesMapper;

    public NoGuiGalaxyEclipseClient() {
        clientNetworkManager = new TestClientNetworkManager();
        dictionaryTypesMapper = new DictionaryTypesMapper();
    }

    public static void main(String args[]) {
        new NoGuiGalaxyEclipseClient();
    }

    public static List<NoGuiGalaxyEclipseClient> connectClients(int amount) {
        List<NoGuiGalaxyEclipseClient> result = new ArrayList<NoGuiGalaxyEclipseClient>();
        for (int i = 0; i < amount; i++) {
            NoGuiGalaxyEclipseClient client = new NoGuiGalaxyEclipseClient();
            client.clientNetworkManager.testConnect();
            result.add(client);
        }
        return result;
    }
}
