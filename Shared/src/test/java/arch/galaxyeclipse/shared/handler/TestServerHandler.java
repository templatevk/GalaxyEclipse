package arch.galaxyeclipse.shared.handler;

import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.*;
import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.Packet.*;
import org.jboss.netty.channel.*;

public class TestServerHandler extends SimpleChannelHandler {
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		Packet packet = (Packet)e.getMessage();
		switch (packet.getType()) {
		case AUTH_REQUEST:
			System.out.println("Server received AUTH_REQUEST");
			Packet.Builder builder = Packet.newBuilder();
			builder.setType(Type.AUTH_RESPONSE);
			e.getChannel().write(builder.build());
			break;
		}
	}
}
