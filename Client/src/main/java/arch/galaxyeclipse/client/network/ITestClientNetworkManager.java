package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.util.*;

import java.net.*;

/**
 *
 */
public interface ITestClientNetworkManager extends IClientNetworkManager {
    void testConnect();

    void testConnect(boolean wait);

    void testConnect(SocketAddress address, boolean wait);

    void testConnect(SocketAddress address, boolean wait, ICallback<Boolean> callback);
}
