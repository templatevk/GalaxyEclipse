package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.*;
import arch.galaxyeclipse.shared.network.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.Packet.*;
import arch.galaxyeclipse.shared.thread.*;
import arch.galaxyeclipse.shared.util.*;
import lombok.extern.slf4j.*;
import org.jboss.netty.bootstrap.*;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.*;

import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Main class responsible for network communication using the GeProtocol.
 */
@Slf4j
class ClientNetworkManager implements IClientNetworkManager, ITestClientNetworkManager {
	// Sleep interval to wait for connection result
	private static final int CONNECTION_TIMEOUT_MILLISECONDS = 300;

	private IClientChannelHandlerFactory channelHandlerFactory;
	
	private IChannelHandler channelHandler;
	private ClientBootstrap bootstrap;
	private Map<Packet.Type, Set<IServerPacketListener>> listeners;
	
	public ClientNetworkManager(IClientChannelHandlerFactory channelHandlerFactory) {
		this.channelHandlerFactory = channelHandlerFactory;

        listeners = new HashMap<>();

        channelHandler = channelHandlerFactory.createHandler(new ICommand<Packet>() {
            @Override
            public void perform(Packet packet) {
                if (log.isDebugEnabled()) {
                    log.debug("Notifying listeners for " + packet.getType());
                }

                Set<IServerPacketListener> listenersForType = listeners.get(packet.getType());
                if (listenersForType != null) {
                    for (IServerPacketListener listener : listenersForType) {
                        listener.onPacketReceived(packet);
                    }
                }
            }
        });

        // Instantiating the client bootsrap
        bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		bootstrap.setPipelineFactory(new AbstractProtobufChannelPipelineFactory() {
			@Override
			protected void configureHandlers(ChannelPipeline pipeline) {
				pipeline.addLast("clientHanlder", channelHandler);
			}
		});	
		bootstrap.setOption("keepAlive", true);
		bootstrap.setOption("tcpNoDelay", true);
	}
	
	@Override
	public void connect(SocketAddress address, final ICallback<Boolean> callback) {
		// Trying to testConnect to the passed adress
        if (channelHandler.isConnected()) {
            channelHandler.disconnect(new StubCallback<Boolean>());
        }

		bootstrap.connect(address).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(final ChannelFuture future) throws Exception {
                new DelayedRunnableExecutor(CONNECTION_TIMEOUT_MILLISECONDS, new Runnable() {
                    @Override
                    public void run() {
                        callback.onOperationComplete(channelHandler.isConnected());
                    }
                }).start();
			}
		});
	}
	
	@Override
	public void disconnect(final ICallback<Boolean> callback) {
		if (channelHandler.isConnected()) {
			channelHandler.disconnect(callback);
		} else {
			callback.onOperationComplete(false);
		}
	}
	
	@Override
	public void addPacketListener(IServerPacketListener listener) {
		Set<IServerPacketListener> typeListeners = listeners.get(listener.getPacketTypes());
		if (typeListeners == null) {
			typeListeners = new HashSet<IServerPacketListener>();
			for (Packet.Type packetType : listener.getPacketTypes()) {
                if (log.isInfoEnabled()) {
				    log.info("Adding listener of type " + packetType.toString());
                }
				listeners.put(packetType, typeListeners);
			}
		}
		typeListeners.add(listener);
	}
	
	@Override
	public void removePacketListener(IServerPacketListener listener) {
        if (log.isInfoEnabled()) {
		    log.info("Removing listener " + listener + " of types");
        }

		for (Packet.Type packetType : listener.getPacketTypes()) {
			Set<IServerPacketListener> typeListeners = listeners.get(listener.getPacketTypes());
			if (typeListeners != null) {
                if (log.isInfoEnabled()) {
                    log.info(packetType.toString());
                }
				typeListeners.remove(listener);
			}
		}
	}
	
	@Override
	public void removeListenerForType(IServerPacketListener listener,
			Type packetType) {
        if (log.isInfoEnabled()) {
            log.info("Removing listener " + listener + " of type");
        }

		Set<IServerPacketListener> typeListeners = listeners.get(packetType);
		if (typeListeners != null) {
            if (log.isInfoEnabled()) {
                log.info(packetType.toString());
            }
			typeListeners.remove(listener);
		}	
	}
	
	@Override
	public void sendPacket(Packet packet) {
        if (log.isTraceEnabled()) {
            log.trace(LogUtils.getObjectInfo(this) + " sending packet " + packet.getType());
        }

		channelHandler.sendPacket(packet);
	}






    // Test methods

    @Override
    public void testConnect() {
        testConnect(false);
    }

    @Override
    public void testConnect(boolean wait) {
        testConnect(new InetSocketAddress(SharedInfo.HOST, SharedInfo.PORT), wait);
    }

    @Override
    public void testConnect(SocketAddress address, boolean wait) {
        testConnect(address, wait, new StubCallback<Boolean>());
    }

    @Override
    public void testConnect(SocketAddress address, final boolean wait,
            final ICallback<Boolean> callback) {
        // Trying to testConnect to the passed adress
        bootstrap.connect(address).addListener(new ChannelFutureListener() {
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
                            if (!channelHandler.isConnected()) {
                                channelHandler.disconnect(new StubCallback<Boolean>());
                            }
                            callback.onOperationComplete(channelHandler.isConnected());
                        }
                    }).start();
                } else {
                    callback.onOperationComplete(channelHandler.isConnected());
                }
            }
        });
    }
}