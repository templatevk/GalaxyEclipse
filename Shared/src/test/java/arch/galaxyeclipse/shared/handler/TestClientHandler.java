package arch.galaxyeclipse.shared.handler;

import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.*;
import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.Packet.*;
import org.jboss.netty.channel.*;

import java.util.*;

public class TestClientHandler extends SimpleChannelHandler {
	@Override
	public void channelConnected(ChannelHandlerContext ctx, final ChannelStateEvent e)
			throws Exception {
		Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Packet.Builder builder = Packet.newBuilder();
				builder.setType(Type.AUTH_REQUEST);
				e.getChannel().write(builder.build());
			}
		}, 0, 2000);
	}
	
	
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		Packet packet = (Packet)e.getMessage();
		switch (packet.getType()) {
		case AUTH_RESPONSE:
			System.out.println("Client received AUTH_RESPONSE");
			break;
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		//e.getFuture().setFailure(e.getCause());
	}
}
