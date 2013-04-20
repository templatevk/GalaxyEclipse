package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.network.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.Packet.*;
import arch.galaxyeclipse.shared.thread.*;
import arch.galaxyeclipse.shared.util.*;
import com.google.common.collect.*;
import lombok.*;
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
class ClientNetworkManager implements IClientNetworkManager {
	// Sleep interval to wait for connection result
	protected static final int CONNECTION_TIMEOUT_MILLISECONDS = 300;

    @Getter(AccessLevel.PROTECTED)
	private IChannelHandler channelHandler;
    @Getter(AccessLevel.PROTECTED)
	private ClientBootstrap bootstrap;
	private SetMultimap<Packet.Type, IServerPacketListener> listeners;
	
	public ClientNetworkManager() {
        listeners = HashMultimap.create();

        channelHandler = ClientChannelHandlerFactory.createHandler(new ICommand<Packet>() {
            @Override
            public void perform(Packet packet) {
                if (log.isDebugEnabled()) {
                    log.debug("Notifying listeners for " + packet.getType());
                }

                for (IServerPacketListener listener : listeners.get(packet.getType())) {
                    listener.onPacketReceived(packet);
                }
            }
        });

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
        for (Packet.Type packetType : listener.getPacketTypes()) {
            if (log.isInfoEnabled()) {
                log.info("Adding listener of type " + packetType.toString());
            }
            listeners.put(packetType, listener);
        }
	}
	
	@Override
	public void removePacketListener(IServerPacketListener listener) {
        if (log.isInfoEnabled()) {
		    log.info("Removing listener " + listener + " of types");
        }

		for (Packet.Type packetType : listener.getPacketTypes()) {
            if (log.isInfoEnabled()) {
                log.info(packetType.toString());
            }
            removeListenerForType(listener, packetType);
		}
	}
	
	@Override
	public void removeListenerForType(IServerPacketListener listener,
			Type packetType) {
        if (log.isInfoEnabled()) {
            log.info("Removing listener " + listener + " of type");
        }

        listeners.get(packetType).remove(listener);
	}
	
	@Override
	public void sendPacket(Packet packet) {
        if (log.isTraceEnabled()) {
            log.trace(LogUtils.getObjectInfo(this) + " sending packet " + packet.getType());
        }

		channelHandler.sendPacket(packet);
	}
}