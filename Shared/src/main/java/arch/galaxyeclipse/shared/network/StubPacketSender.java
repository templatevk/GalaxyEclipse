package arch.galaxyeclipse.shared.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol.Packet;
import arch.galaxyeclipse.shared.common.LogUtils;
import lombok.extern.slf4j.Slf4j;

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
