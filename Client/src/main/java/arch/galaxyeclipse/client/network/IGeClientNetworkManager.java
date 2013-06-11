package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.common.IGeCallback;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;

public interface IGeClientNetworkManager extends IGePacketSubscribable<IGeServerPacketListener> {

    void sendPacket(GePacket packet);

    void disconnect(IGeCallback<Boolean> callback);

    void connect(IGeCallback<Boolean> callback);
}
