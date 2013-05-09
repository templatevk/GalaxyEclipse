package arch.galaxyeclipse.shared.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol.Packet;
import arch.galaxyeclipse.shared.util.ICallback;
import org.jboss.netty.channel.ChannelHandler;

public interface IChannelHandler extends ChannelHandler {
	void sendPacket(Packet packet);
	
	void disconnect(ICallback<Boolean> callback);
	
	boolean isConnected();
}
