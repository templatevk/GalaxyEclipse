package arch.galaxyeclipse.shared;

import arch.galaxyeclipse.shared.handler.*;
import arch.galaxyeclipse.shared.protocol.*;
import org.jboss.netty.bootstrap.*;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.*;
import org.jboss.netty.handler.codec.protobuf.*;

import java.net.*;
import java.util.concurrent.*;

public class ClientTest {
	public static void main(String args[]) {
		ChannelFactory factory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
	
		ClientBootstrap bootstrap = new ClientBootstrap(factory);
	
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() {
				ChannelPipeline pipeline = Channels.pipeline();
				
				pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
				pipeline.addLast("protobufDecoder", new ProtobufDecoder(
						GalaxyEclipseProtocol.Packet.getDefaultInstance()));
				
				pipeline.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
				pipeline.addLast("protobufEncoder", new ProtobufEncoder());
				
				pipeline.addLast("testHandler", new TestClientHandler());
				
				return pipeline;
			}
		});
	
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);
		bootstrap.setOption("connectTimeoutMillis", 5000);
		
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(
				SharedInfo.HOST, SharedInfo.PORT));
		future.addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture future) throws Exception {
				System.out.println(future.isSuccess());
			}
		});
	}
}
