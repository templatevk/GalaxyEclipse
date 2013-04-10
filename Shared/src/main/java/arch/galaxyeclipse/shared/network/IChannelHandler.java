package arch.galaxyeclipse.shared.network;

import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.*;
import arch.galaxyeclipse.shared.thread.*;
import org.jboss.netty.channel.*;

public interface IChannelHandler extends ChannelHandler {
	void sendPacket(Packet packet);
	
	void disconnect(final ICallback<Boolean> callback);	
	
	boolean isConnected();
}
