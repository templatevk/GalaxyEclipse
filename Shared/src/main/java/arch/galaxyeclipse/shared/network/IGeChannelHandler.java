package arch.galaxyeclipse.shared.network;

import arch.galaxyeclipse.shared.common.IGeCallback;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;
import org.jboss.netty.channel.ChannelHandler;

public interface IGeChannelHandler extends ChannelHandler {

    void sendPacket(GePacket packet);

    void disconnect(IGeCallback<Boolean> callback);

    boolean isConnected();
}
