package arch.galaxyeclipse.server;

import java.net.*;
import java.util.concurrent.*;

import org.jboss.netty.bootstrap.*;
import org.jboss.netty.buffer.*;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.*;

import arch.galaxyeclipse.shared.*;

public class GalaxyEclipseServer {	
	public static void main(String[] args) throws Exception {
		ChannelFactory factory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		ServerBootstrap bootstrap = new ServerBootstrap(factory);

//		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
//			public ChannelPipeline getPipeline() {
//				//return Channels.pipeline();
//			}
//		});

		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);

		bootstrap.bind(new InetSocketAddress(SharedTestInfo.PORT));
	}
}