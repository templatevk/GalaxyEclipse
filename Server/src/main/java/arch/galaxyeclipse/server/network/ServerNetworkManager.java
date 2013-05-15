package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.shared.common.LogUtils;
import arch.galaxyeclipse.shared.network.ProtobufChannelPipelineFactory;
import arch.galaxyeclipse.shared.protocol.GeProtocol.Packet;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

@Slf4j
class ServerNetworkManager implements IServerNetworkManager, IMonitoringNetworkManager {
    private static final String HOST_PROPERTY = "host";
    private static final String PORT_PROPERTY = "port";

    // All the connected clients
    private Set<IServerChannelHandler> serverChannelHandlers;

	private ProtobufChannelPipelineFactory channelPipelineFactory;
    private ServerBootstrap bootstrap;
    private Channel serverChannel;
    private SocketAddress hostAddress;

    public ServerNetworkManager(ProtobufChannelPipelineFactory channelPipelineFactory) {
        String host = System.getProperty(HOST_PROPERTY);
        String port = System.getProperty(PORT_PROPERTY);
        Preconditions.checkNotNull(host, "Network error, host property is not set");
        Preconditions.checkNotNull(port, "Network error, port property is not set");
        hostAddress = new InetSocketAddress(host, Integer.valueOf(port));

        this.channelPipelineFactory = channelPipelineFactory;
        serverChannelHandlers = new HashSet<>();
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

        if (log.isInfoEnabled()) {
		    log.info("Starting server on " + host + ":" + port);
        }

		serverChannel = bootstrap.bind(hostAddress);
	}
	
	@Override
	public void stopServer() {
		if (serverChannel != null && serverChannel.isOpen()) {
            if (log.isInfoEnabled()) {
                log.info("Stopping server");
            }
			serverChannel.close();
			serverChannel.unbind();
		}
	}

    @Override
    public void registerServerChannelHandler(IServerChannelHandler handler) {
        serverChannelHandlers.add(handler);

        if (log.isInfoEnabled()) {
            log.info(LogUtils.getObjectInfo(handler) + " registered, total handlers = "
                    + serverChannelHandlers.size());
        }
    }

    @Override
    public void unregisterServerChannelHandler(IServerChannelHandler handler) {
        serverChannelHandlers.remove(handler);

        if (log.isInfoEnabled()) {
            log.info(LogUtils.getObjectInfo(handler) + " unregistered, total handlers = "
                    + serverChannelHandlers.size());
        }
    }

    @Override
    public void sendBroadcast(Packet packet) {
        if (log.isInfoEnabled()) {
            log.info("Sending " + packet.getType() + " broadcast");
        }

        for (IServerChannelHandler handler : new ArrayList<>(serverChannelHandlers)) {
            handler.sendPacket(packet);
        }
    }
}