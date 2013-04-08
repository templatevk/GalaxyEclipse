package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.shared.network.*;
import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.*;
import org.apache.log4j.*;
import org.jboss.netty.bootstrap.*;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.*;

import java.net.*;
import java.util.*;
import java.util.concurrent.*;

class ServerNetworkManager implements IServerNetworkManager, IMonitoringNetworkManager {
	private static final Logger log = Logger.getLogger(ServerNetworkManager.class);

    // All the connected clients
    private Set<IServerChannelHandler> serverChannelHandlers;

	private AbstractProtobufChannelPipelineFactory channelPipelineFactory;
    private ServerBootstrap bootstrap;
    private Channel serverChannel;

	public ServerNetworkManager(AbstractProtobufChannelPipelineFactory channelPipelineFactory) {
		this.channelPipelineFactory = channelPipelineFactory;
        serverChannelHandlers = new HashSet<IServerChannelHandler>();
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

    @Override
    public void registerServerChannelHandler(IServerChannelHandler handler) {
        serverChannelHandlers.add(handler);
    }

    @Override
    public void unregisterServerChannelHandler(IServerChannelHandler handler) {
        serverChannelHandlers.remove(handler);
    }

    @Override
    public void sendBroadcast(Packet packet) {
        for (IServerChannelHandler handler : serverChannelHandlers) {
            handler.sendPacket(packet);
        }
    }
}