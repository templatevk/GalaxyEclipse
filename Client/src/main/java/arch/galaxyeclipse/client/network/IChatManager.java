package arch.galaxyeclipse.client.network;

import java.util.*;

public interface IChatManager {
    void sendMessage(String message);

    List<String> getChatHistory();
}
