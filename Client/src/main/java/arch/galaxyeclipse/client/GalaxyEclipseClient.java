package arch.galaxyeclipse.client;

import java.net.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;

import org.jboss.netty.bootstrap.*;
import org.jboss.netty.buffer.*;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.*;

import arch.galaxyeclipse.client.window.*;
import arch.galaxyeclipse.shared.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

public class GalaxyEclipseClient  {		
	public static void main(String[] args) throws Exception {
		ClientWindow.getInstance();
	}
	
	private void initNetwork() {
		ChannelFactory factory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		ClientBootstrap bootstrap = new ClientBootstrap(factory);

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() {
				return Channels.pipeline();
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