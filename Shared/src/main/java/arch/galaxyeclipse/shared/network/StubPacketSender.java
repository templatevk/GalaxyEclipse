package arch.galaxyeclipse.shared.network;

import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.*;
import arch.galaxyeclipse.shared.util.*;
import lombok.extern.slf4j.*;

/**
 * Simply ignores all packets.
 */
@Slf4j
public class StubPacketSender implements IPacketSender {
    public StubPacketSender() {
		
	}

	@Override
	public void send(Packet packet) {
        if (log.isDebugEnabled()) {
		    log.debug(LogUtils.getObjectInfo(this) + " " + packet.getType());
        }
	}
}
