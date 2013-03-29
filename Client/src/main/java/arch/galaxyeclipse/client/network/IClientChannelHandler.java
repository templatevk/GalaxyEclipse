package arch.galaxyeclipse.client.network;

import arch.galaxyeclipse.shared.network.*;
import arch.galaxyeclipse.shared.thread.*;

public interface IClientChannelHandler extends IChannelHandler {
	void disconnect(final ICallback<Boolean> callback);
	
	boolean isConnected();
}
