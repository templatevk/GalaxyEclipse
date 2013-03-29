package arch.galaxyeclipse.shared.network;

import org.jboss.netty.channel.*;

import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.Packet;
import arch.galaxyeclipse.shared.thread.*;

public interface IChannelHandler extends ChannelHandler {
	void sendPacket(Packet packet);
	
	void disconnect(final ICallback<Boolean> callback);	
	
	boolean isConnected();
}
