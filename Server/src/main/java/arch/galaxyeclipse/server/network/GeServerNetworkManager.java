package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.shared.common.GeLogUtils;
import arch.galaxyeclipse.shared.network.GeProtobufChannelPipelineFactory;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;
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
class GeServerNetworkManager implements IGeServerNetworkManager, IGeMonitoringNetworkManager {

    private static final String PORT_PROPERTY = "port";

    // All the connected clients
    private Set<IGeServerChannelHandler> serverChannelHandlers;

    private GeProtobufChannelPipelineFactory channelPipelineFactory;
    private ServerBootstrap bootstrap;
    private Channel serverChannel;

    private String port;
    private SocketAddress hostAddress;

    public GeServerNetworkManager(GeProtobufChannelPipelineFactory channelPipelineFactory) {
        port = System.getProperty(PORT_PROPERTY);
        Preconditions.checkNotNull(port, "Network error, port property is not set");
        hostAddress = new InetSocketAddress("0.0.0.0", Integer.valueOf(port));

        this.channelPipelineFactory = channelPipelineFactory;
        serverChannelHandlers = new HashSet<>();
    }

    @Override
    public void startServer() {
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
        serverChannel = bootstrap.bind(hostAddress);

        if (GeServerNetworkManager.log.isInfoEnabled()) {
            GeServerNetworkManager.log.info("Starting server on port " + port);
            GeServerNetworkManager.log.info("Local address " + serverChannel.getLocalAddress());
            GeServerNetworkManager.log.info("Is bound " + serverChannel.isBound());
            GeServerNetworkManager.log.info("Is open " + serverChannel.isOpen());
        }
    }

    @Override
    public void stopServer() {
        if (serverChannel != null && serverChannel.isOpen()) {
            if (GeServerNetworkManager.log.isInfoEnabled()) {
                GeServerNetworkManager.log.info("Stopping server");
            }
            serverChannel.close();
            serverChannel.unbind();
        }
    }

    @Override
    public void registerServerChannelHandler(IGeServerChannelHandler handler) {
        serverChannelHandlers.add(handler);

        if (GeServerNetworkManager.log.isInfoEnabled()) {
            GeServerNetworkManager.log.info(GeLogUtils.getObjectInfo(handler) + " registered, total handlers = "
                    + serverChannelHandlers.size());
        }
    }

    @Override
    public void unregisterServerChannelHandler(IGeServerChannelHandler handler) {
        serverChannelHandlers.remove(handler);

        if (GeServerNetworkManager.log.isInfoEnabled()) {
            GeServerNetworkManager.log.info(GeLogUtils.getObjectInfo(handler) + " unregistered, total handlers = "
                    + serverChannelHandlers.size());
        }
    }

    @Override
    public void sendBroadcast(GePacket packet) {
        if (GeServerNetworkManager.log.isInfoEnabled()) {
            GeServerNetworkManager.log.info("Sending " + packet.getType() + " broadcast");
        }

        for (IGeServerChannelHandler handler : new ArrayList<>(serverChannelHandlers)) {
            handler.sendPacket(packet);
        }
    }
}