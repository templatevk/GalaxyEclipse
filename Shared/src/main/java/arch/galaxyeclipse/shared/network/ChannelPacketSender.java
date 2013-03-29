package arch.galaxyeclipse.shared.network;

import org.apache.log4j.*;
import org.jboss.netty.channel.*;

import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.Packet;
import arch.galaxyeclipse.shared.util.*;

public class ChannelPacketSender implements IPacketSender {
	private static final Logger log = Logger.getLogger(StubPacketSender.class);
	
	private Channel channel;

	public ChannelPacketSender(Channel channel) {
		super();
		this.channel = channel;
	}
	
	@Override
	public void send(Packet packet) {
		log.debug(LogUtils.getObjectInfo(this) + " "  + packet.getType());
		channel.write(packet);
	}
}
