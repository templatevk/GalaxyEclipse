package arch.galaxyeclipse.client.network;

import java.util.List;

public interface IGeChatManager {
    void sendMessage(String message);

    List<String> getChatHistory();
}
