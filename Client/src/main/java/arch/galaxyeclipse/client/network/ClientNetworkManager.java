package arch.galaxyeclipse.client.network;

import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import org.apache.log4j.*;
import org.jboss.netty.bootstrap.*;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import arch.galaxyeclipse.shared.network.*;
import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.Packet;
import arch.galaxyeclipse.shared.thread.*;

@Component
public class ClientNetworkManager implements IClientNetworkManager {
	private static final Logger log = Logger.getLogger(ClientNetworkManager.class);

	@Autowired
	private IClientChannelHandlerFactory channelHandlerFactory;
	
	private IClientChannelHandler channelHandler;
	private ClientBootstrap bootstrap;
	private Map<Packet.Type, List<IServerPacketListener>> listeners;
	
	public ClientNetworkManager() {			
		listeners = new HashMap<Packet.Type, List<IServerPacketListener>>();
		bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		bootstrap.setOption("keepAlive", true);
		bootstrap.setOption("tcpNoDelay", true);
		
		bootstrap.setPipelineFactory(new AbstractProtobufChannelPipelineFactory() {
			@Override
			protected void configureHandlers(ChannelPipeline pipeline) {
				pipeline.addLast("clientHanlder", channelHandler);
			}
		});	
	}
	
	@Override
	public void connect(SocketAddress address, final ICallback<Boolean> callback) {
		if (channelHandler == null) {
			channelHandler = channelHandlerFactory.createHandler(new IDispatchCommand<Packet>() {
				@Override
				public void perform(Packet packet) {
					log.debug("Notifying listeners for " + packet.getType());
					for (IServerPacketListener listener : listeners.get(packet.getType())) {
						listener.onPacketReceived(packet);
					}
				}
			});
		}
		
		bootstrap.connect(address).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				callback.onOperationComplete(future.isSuccess());
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
	
	public void addListener(IServerPacketListener listener) {
		List<IServerPacketListener> typeListeners = listeners.get(listener.getPacketType());
		if (typeListeners == null) {
			typeListeners = new ArrayList<IServerPacketListener>();
			listeners.put(listener.getPacketType(), typeListeners);
		}
		typeListeners.add(listener);
		log.info("Adding listener of type " + listener.getPacketType().toString());
	}
	
	public void removeListener(IServerPacketListener listener) {
		List<IServerPacketListener> typeListeners = listeners.get(listener.getPacketType());
		if (typeListeners != null) {
			typeListeners.remove(listener);
			log.info("Removing listener of type " + listener.getPacketType().toString());
		}
	}
	
	@Override
	public void sendPacket(Packet packet) {
		channelHandler.sendPacket(packet);
	}
}