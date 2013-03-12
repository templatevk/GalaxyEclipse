package arch.galaxyeclipse.client.network;

import java.util.*;
import java.util.concurrent.*;

import org.apache.log4j.*;
import org.jboss.netty.bootstrap.*;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.*;
import org.jboss.netty.handler.codec.protobuf.*;

import arch.galaxyeclipse.shared.protocol.*;

public class NetworkManager {	
	private static final NetworkManager INSTANCE = new NetworkManager();
	private static final Logger log = Logger.getLogger(NetworkManager.class);
	
	private Map<GalaxyEclipseProtocol.Packet.Type, List<IPacketListener>> listeners;
	private ClientBootstrap bootstrap;
	private ConcurrentLinkedQueue<GalaxyEclipseProtocol.Packet> packets;
	private PacketsDispatcher packetsDispatcher;
	
	private NetworkManager() {
		listeners = new HashMap<GalaxyEclipseProtocol.Packet.Type, List<IPacketListener>>();
		packets = new ConcurrentLinkedQueue<GalaxyEclipseProtocol.Packet>();
		packetsDispatcher = new PacketsDispatcher();
		
		bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		bootstrap.setOption("keepAlive", true);
		bootstrap.setOption("tcpNoDelay", true);
		
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				
				pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
				pipeline.addLast("protobufDecoder", new ProtobufDecoder(
						GalaxyEclipseProtocol.Packet.getDefaultInstance()));
				
				pipeline.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
				pipeline.addLast("protobufEncoder", new ProtobufEncoder());
				
				pipeline.addLast("clientHandler", new ClientChannelHandler());
				
				return pipeline;
			}
		});
	}
	
	public static NetworkManager getInstance() {
		return INSTANCE;
	}
	
	public void addListener(IPacketListener listener, 
			GalaxyEclipseProtocol.Packet.Type packetType) {
		List<IPacketListener> typeListeners = listeners.get(packetType);
		if (typeListeners == null) {
			typeListeners = new ArrayList<IPacketListener>();
			listeners.put(packetType, typeListeners);
		}
		typeListeners.add(listener);
	}
	
	public void removeListener(IPacketListener listener, 
			GalaxyEclipseProtocol.Packet.Type packetType) {
		List<IPacketListener> typeListeners = listeners.get(packetType);
		if (typeListeners == null) {
			return;
		}
		typeListeners.remove(listener);
	}
	
	private class ClientChannelHandler extends SimpleChannelHandler {
		@Override
		public void channelConnected(ChannelHandlerContext ctx,
				ChannelStateEvent e) throws Exception {
		
		}
		
		@Override
		public void channelDisconnected(ChannelHandlerContext ctx,
				ChannelStateEvent e) throws Exception {
		
		}
		
		@Override
		public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
				throws Exception {
		
		}
		
		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
				throws Exception {
		
		}
		
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
				throws Exception {
			log.error("Network error", e.getCause());
		}
	}
	
	private class PacketsDispatcher extends Thread {
		@Override
		public void run() {
			
		}
	}
}