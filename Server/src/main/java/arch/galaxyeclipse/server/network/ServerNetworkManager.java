package arch.galaxyeclipse.server.network;

import java.net.*;
import java.util.concurrent.*;

import org.apache.log4j.*;
import org.jboss.netty.bootstrap.*;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import arch.galaxyeclipse.shared.network.*;

@Component
public class ServerNetworkManager implements IServerNetworkManager {	
	private static final Logger log = Logger.getLogger(ServerNetworkManager.class);
	
	@Autowired
	private AbstractProtobufChannelPipelineFactory channelPipelineFactory;
	private Channel serverChannel;
	private ServerBootstrap bootstrap;
	
	public ServerNetworkManager() {
				
	}
	
	@Override
	public void startServer(String host, int port) {
		if (bootstrap == null) {
			bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
					Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
			bootstrap.setPipelineFactory(channelPipelineFactory);
			bootstrap.setOption("keepAlive", true);
			bootstrap.setOption("tcpNoDelay", true);
		}
		
		if (serverChannel != null && serverChannel.isBound()) {
			serverChannel.unbind();
		}
		serverChannel = bootstrap.bind(new InetSocketAddress(host, port));
		log.info("Starting server on " + host + ":" + port);
	}
	
	@Override
	public void stopServer() {
		if (serverChannel != null && serverChannel.isOpen()) {
			serverChannel.close();
			serverChannel.unbind();
			log.info("Stopping server");
		}
	}
}