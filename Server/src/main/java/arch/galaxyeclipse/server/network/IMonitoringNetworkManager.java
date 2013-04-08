package arch.galaxyeclipse.server.network;

/**
 * Provides means for monitoring connected clients.
 */
interface IMonitoringNetworkManager {
    void registerServerChannelHandler(IServerChannelHandler handler);

    void unregisterServerChannelHandler(IServerChannelHandler handler);
}
