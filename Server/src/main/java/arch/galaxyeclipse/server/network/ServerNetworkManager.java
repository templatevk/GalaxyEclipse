package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.shared.network.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.util.*;
import lombok.extern.slf4j.*;
import org.jboss.netty.bootstrap.*;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.*;

import java.net.*;
import java.util.*;
import java.util.concurrent.*;

@Slf4j
class ServerNetworkManager implements IServerNetworkManager, IMonitoringNetworkManager {
    // All the connected clients
    private Set<IServerChannelHandler> serverChannelHandlers;

	private ProtobufChannelPipelineFactory channelPipelineFactory;
    private ServerBootstrap bootstrap;
    private Channel serverChannel;

	public ServerNetworkManager(ProtobufChannelPipelineFactory channelPipelineFactory) {
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

		serverChannel = bootstrap.bind(new InetSocketAddress(host, port));
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

        if (log.isDebugEnabled()) {
            log.debug(LogUtils.getObjectInfo(handler) + " registered, total handlers = "
                    + serverChannelHandlers.size());
        }
    }

    @Override
    public void unregisterServerChannelHandler(IServerChannelHandler handler) {
        serverChannelHandlers.remove(handler);

        if (log.isDebugEnabled()) {
            log.debug(LogUtils.getObjectInfo(handler) + " unregistered, total handlers = "
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