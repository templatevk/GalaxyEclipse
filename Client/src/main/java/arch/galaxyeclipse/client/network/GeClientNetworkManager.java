package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.common.GeLogUtils;
import arch.galaxyeclipse.shared.common.GeStubCallback;
import arch.galaxyeclipse.shared.common.IGeCallback;
import arch.galaxyeclipse.shared.common.IGeCommand;
import arch.galaxyeclipse.shared.network.GeProtobufChannelPipelineFactory;
import arch.galaxyeclipse.shared.network.IGeChannelHandler;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;
import arch.galaxyeclipse.shared.thread.GeDelayedRunnableTask;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Set;
import java.util.concurrent.Executors;

/**
 * Main class responsible for network communication using the GeProtocol.
 */
@Slf4j
class GeClientNetworkManager implements IGeClientNetworkManager {
    private static final int CONNECTION_TIMEOUT_MILLISECONDS = 300;
    private static final String HOST_PROPERTY = "host";
    private static final String PORT_PROPERTY = "port";

    @Getter(AccessLevel.PROTECTED)
	private IGeChannelHandler channelHandler;
    @Getter(AccessLevel.PROTECTED)
	private ClientBootstrap bootstrap;
	private SetMultimap<GePacket.Type, IGeServerPacketListener> listeners;
    private SocketAddress hostAddress;
	
	public GeClientNetworkManager() {
        String host = System.getProperty(HOST_PROPERTY);
        String port = System.getProperty(PORT_PROPERTY);
        Preconditions.checkNotNull(host, "Network error, host property is not set");
        Preconditions.checkNotNull(port, "Network error, port property is not set");
        hostAddress = new InetSocketAddress(host, Integer.valueOf(port));

        listeners = HashMultimap.create();

        channelHandler = GeClientChannelHandlerFactory.createHandler(new IGeCommand<GePacket>() {
            @Override
            public void perform(GePacket packet) {
                if (GeClientNetworkManager.log.isDebugEnabled()) {
                    GeClientNetworkManager.log.debug("Notifying listeners for " + packet.getType());
                }

                Set<IGeServerPacketListener> packetListeners = listeners.get(packet.getType());
                if (packetListeners != null) {
                    for (IGeServerPacketListener listener : packetListeners) {
                        listener.onPacketReceived(packet);
                    }
                }
            }
        });

        bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		bootstrap.setPipelineFactory(new GeProtobufChannelPipelineFactory() {
			@Override
			protected void configureHandlers(ChannelPipeline pipeline) {
				pipeline.addLast("clientHanlder", channelHandler);
			}
		});
		bootstrap.setOption("keepAlive", true);
		bootstrap.setOption("tcpNoDelay", true);
	}
	
	@Override
	public void connect(final IGeCallback<Boolean> callback) {
        if (channelHandler.isConnected()) {
            channelHandler.disconnect(new GeStubCallback<Boolean>());
        }

		bootstrap.connect(hostAddress).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture future) throws Exception {
                new GeDelayedRunnableTask(CONNECTION_TIMEOUT_MILLISECONDS, new Runnable() {
                    @Override
                    public void run() {
                        callback.onOperationComplete(channelHandler.isConnected());
                    }
                }).start();
            }
        });
	}
	
	@Override
	public void disconnect(final IGeCallback<Boolean> callback) {
		if (channelHandler.isConnected()) {
			channelHandler.disconnect(callback);
		} else {
			callback.onOperationComplete(false);
		}
	}
	
	@Override
	public void addPacketListener(IGeServerPacketListener listener) {
        for (GePacket.Type packetType : listener.getPacketTypes()) {
            if (GeClientNetworkManager.log.isInfoEnabled()) {
                GeClientNetworkManager.log.info("Adding listener of type " + packetType.toString());
            }
            listeners.put(packetType, listener);
        }
	}
	
	@Override
	public void removePacketListener(IGeServerPacketListener listener) {
        for (GePacket.Type packetType : listener.getPacketTypes()) {
            removeListenerForType(listener, packetType);
		}
	}
	
	@Override
	public void removeListenerForType(IGeServerPacketListener listener,
            GePacket.Type packetType) {
        if (GeClientNetworkManager.log.isInfoEnabled()) {
            GeClientNetworkManager.log.info("Removing listener " + listener + " of type " + packetType);
        }

        listeners.get(packetType).remove(listener);
	}
	
	@Override
	public void sendPacket(GePacket packet) {
        if (GeClientNetworkManager.log.isTraceEnabled()) {
            GeClientNetworkManager.log.trace(GeLogUtils.getObjectInfo(this) + " sending packet " + packet.getType());
        }
        Preconditions.checkNotNull(packet, "`packet` can not be null");
		channelHandler.sendPacket(packet);
	}
}