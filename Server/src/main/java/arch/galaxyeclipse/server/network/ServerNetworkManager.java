package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.shared.network.*;
import org.apache.log4j.*;
import org.jboss.netty.bootstrap.*;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.*;

import java.net.*;
import java.util.concurrent.*;

class ServerNetworkManager implements IServerNetworkManager {
	private static final Logger log = Logger.getLogger(ServerNetworkManager.class);
	
	private AbstractProtobufChannelPipelineFactory channelPipelineFactory;
	private Channel serverChannel;
	private ServerBootstrap bootstrap;
	
	public ServerNetworkManager(AbstractProtobufChannelPipelineFactory channelPipelineFactory) {
		this.channelPipelineFactory = channelPipelineFactory;
	}
	
	@Override
	public void startServer(String host, int port) {
        // Initialize server bootstrap
        if (bootstrap == null) {
			bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
					Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
			bootstrap.setPipelineFactory(channelPipelineFactory);
			bootstrap.setOption("keepAlive", true);
			bootstrap.setOption("tcpNoDelay", true);
		}

        // Unbind the port if bound
		if (serverChannel != null && serverChannel.isBound()) {
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