package arch.galaxyeclipse.server.network;

/**
 * Provides means for monitoring connected clients.
 */
interface IGeMonitoringNetworkManager {
    void registerServerChannelHandler(IGeServerChannelHandler handler);

    void unregisterServerChannelHandler(IGeServerChannelHandler handler);
}
