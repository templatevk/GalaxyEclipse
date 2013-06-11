package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;

/**
 * Strategy for handling player packets.
 */
public interface IGeStatefulPacketHandler {

    boolean handle(GePacket packet);

    void onChannelClosed();
}
