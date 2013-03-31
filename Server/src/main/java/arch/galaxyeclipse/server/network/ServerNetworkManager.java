package arch.galaxyeclipse.server.network;

import java.net.*;
import java.util.concurrent.*;

import org.apache.log4j.*;
import org.jboss.netty.bootstrap.*;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.*;

import arch.galaxyeclipse.shared.network.*;

public class ServerNetworkManager implements IServerNetworkManager {	
	private static final Logger log = Logger.getLogger(ServerNetworkManager.class);
	
	private AbstractProtobufChannelPipelineFactory channelPipelineFactory;
	private Channel serverChannel;
	private ServerBootstrap bootstrap;
	
	public ServerNetworkManager(AbstractProtobufChannelPipelineFactory channelPipelineFactory) {
		this.channelPipelineFactory = channelPipelineFactory;
	}
	
	@Override
	public void startServer(String host, int port) {
		if (bootstrap == null) { // Initialize server bootstrap
			bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
					Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
			bootstrap.setPipelineFactory(channelPipelineFactory);
			bootstrap.setOption("keepAlive", true);
			bootstrap.setOption("tcpNoDelay", true);
		}
		
		if (serverChannel != null && serverChannel.isBound()) { // Unbind the port if binded
			serverChannel.unbind();
		}
		log.info("Starting server on " + host + ":" + port);
		serverChannel = bootstrap.bind(new InetSocketAddress(host, port));
	}
	
	@Override
	public void stopServer() {
		if (serverChannel != null && serverChannel.isOpen()) {
			log.info("Stopping server");
			serverChannel.close();
			serverChannel.unbind();
		}
	}
}