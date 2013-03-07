package arch.galaxyeclipse.shared;

import java.net.*;
import java.util.concurrent.*;

import org.jboss.netty.bootstrap.*;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.*;

import arch.galaxyeclipse.shared.handler.*;

public class ClientTest {
	public static void main(String args[]) {
		ChannelFactory factory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
	
		ClientBootstrap bootstrap = new ClientBootstrap(factory);
	
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() {
				return Channels.pipeline(new TestClientHandler());
			}
		});
	
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);
	
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(
				SharedTestInfo.HOST, SharedTestInfo.PORT));
		future.addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture future) throws Exception {
				
			}
		});
	}
}
