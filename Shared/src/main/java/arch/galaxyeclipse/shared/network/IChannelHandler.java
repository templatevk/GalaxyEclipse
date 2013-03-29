package arch.galaxyeclipse.shared.network;

import org.jboss.netty.channel.*;

import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.Packet;

public interface IChannelHandler extends ChannelHandler {
	void sendPacket(Packet packet);
}
