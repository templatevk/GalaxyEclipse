package arch.galaxyeclipse.shared.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.util.*;
import lombok.extern.slf4j.*;
import org.jboss.netty.channel.*;

/**
 * Sends the packets through the channel passed.
 */
@Slf4j
public class ChannelPacketSender implements IPacketSender {
	private Channel channel;

	public ChannelPacketSender(Channel channel) {
		super();
		this.channel = channel;
	}
	
	@Override
	public void send(Packet packet) {
        if (log.isDebugEnabled()) {
		    log.debug(LogUtils.getObjectInfo(this) + " "  + packet.getType());
        }
		channel.write(packet);
	}
}
