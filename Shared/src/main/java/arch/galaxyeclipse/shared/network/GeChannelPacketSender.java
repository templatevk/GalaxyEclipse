package arch.galaxyeclipse.shared.network;

import arch.galaxyeclipse.shared.common.GeLogUtils;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;
import lombok.extern.slf4j.Slf4j;
import org.jboss.netty.channel.Channel;

/**
 * Sends the packets through the channel passed.
 */
@Slf4j
public class GeChannelPacketSender implements IGePacketSender {

    private Channel channel;

    public GeChannelPacketSender(Channel channel) {
        super();
        this.channel = channel;
    }

    @Override
    public void send(GePacket packet) {
        if (GeChannelPacketSender.log.isDebugEnabled()) {
            GeChannelPacketSender.log.debug(GeLogUtils.getObjectInfo(this) + " " + packet.getType());
        }
        channel.write(packet);
    }
}
