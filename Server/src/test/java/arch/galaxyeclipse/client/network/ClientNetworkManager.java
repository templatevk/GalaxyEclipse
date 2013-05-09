package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.SharedInfo;
import arch.galaxyeclipse.shared.thread.DelayedRunnableTask;
import arch.galaxyeclipse.shared.util.ICallback;
import arch.galaxyeclipse.shared.util.StubCallback;
import lombok.extern.slf4j.Slf4j;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

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
                    new DelayedRunnableTask(CONNECTION_TIMEOUT_MILLISECONDS, new Runnable() {
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