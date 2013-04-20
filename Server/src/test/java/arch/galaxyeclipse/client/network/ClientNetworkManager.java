package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.*;
import arch.galaxyeclipse.shared.network.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.Packet.*;
import arch.galaxyeclipse.shared.thread.*;
import arch.galaxyeclipse.shared.util.*;
import com.google.common.collect.*;
import lombok.extern.slf4j.*;
import org.jboss.netty.bootstrap.*;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.*;

import java.net.*;
import java.util.concurrent.*;

/**
 * Main class responsible for network communication using the GeProtocol.
 */
@Slf4j
class TestClientNetworkManager extends ClientNetworkManager implements IClientNetworkManager {
	public TestClientNetworkManager() {

	}

    public void testConnect() {
        testConnect(false);
    }

    public void testConnect(boolean wait) {
        testConnect(new InetSocketAddress(SharedInfo.HOST, SharedInfo.PORT), wait);
    }

    public void testConnect(SocketAddress address, boolean wait) {
        testConnect(address, wait, new StubCallback<Boolean>());
    }

    public void testConnect(SocketAddress address, final boolean wait,
            final ICallback<Boolean> callback) {
        // Trying to testConnect to the passed adress
        getBootstrap().connect(address).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture future) throws Exception {
                if (wait) {
                    if (log.isInfoEnabled()) {
                        log.info("Waiting " + CONNECTION_TIMEOUT_MILLISECONDS
                                + " milliseconds for connection");
                    }

                    // Waiting in the separate thread and notifying the caller trough the callback
                    new DelayedRunnableExecutor(CONNECTION_TIMEOUT_MILLISECONDS, new Runnable() {
                        @Override
                        public void run() {
                            if (!getChannelHandler().isConnected()) {
                                getChannelHandler().disconnect(new StubCallback<Boolean>());
                            }
                            callback.onOperationComplete(getChannelHandler().isConnected());
                        }
                    }).start();
                } else {
                    callback.onOperationComplete(getChannelHandler().isConnected());
                }
            }
        });
    }
}