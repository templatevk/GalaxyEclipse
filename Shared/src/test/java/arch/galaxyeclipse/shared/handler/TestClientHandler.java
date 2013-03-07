package arch.galaxyeclipse.shared.handler;

import java.util.*;

import org.jboss.netty.channel.*;

public class TestClientHandler extends SimpleChannelHandler {
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
		}, 2000);
	}
}
