package arch.galaxyeclipse.shared.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.util.*;
import org.jboss.netty.channel.*;

public interface IChannelHandler extends ChannelHandler {
	void sendPacket(Packet packet);
	
	void disconnect(ICallback<Boolean> callback);
	
	boolean isConnected();
}
