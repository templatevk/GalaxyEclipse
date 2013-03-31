package arch.galaxyeclipse.shared.network;

import org.apache.log4j.*;

import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.Packet;
import arch.galaxyeclipse.shared.util.*;

/**
 * Simply ignores all packets.
 */
public class StubPacketSender implements IPacketSender {
	private static final Logger log = Logger.getLogger(StubPacketSender.class);
	
	public StubPacketSender() {
		
	}

	@Override
	public void send(Packet packet) {
		log.debug(LogUtils.getObjectInfo(this) + " " + packet.getType());
	}
}
