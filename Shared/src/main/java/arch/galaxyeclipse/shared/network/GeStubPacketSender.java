package arch.galaxyeclipse.shared.network;

import arch.galaxyeclipse.shared.common.GeLogUtils;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;
import lombok.extern.slf4j.Slf4j;

/**
 * Simply ignores all packets.
 */
@Slf4j
public class GeStubPacketSender implements IGePacketSender {
    public GeStubPacketSender() {
		
	}

	@Override
	public void send(GePacket packet) {
        if (GeStubPacketSender.log.isDebugEnabled()) {
		    GeStubPacketSender.log.debug(GeLogUtils.getObjectInfo(this) + " " + packet.getType());
        }
	}
}
