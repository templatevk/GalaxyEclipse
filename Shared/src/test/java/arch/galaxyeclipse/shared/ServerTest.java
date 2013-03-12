package arch.galaxyeclipse.shared;

import java.net.*;
import java.util.concurrent.*;

import org.jboss.netty.bootstrap.*;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.*;
import org.jboss.netty.handler.codec.protobuf.*;

import arch.galaxyeclipse.shared.handler.*;
import arch.galaxyeclipse.shared.protocol.*;

public class ServerTest {
	public static void main(String[] args) throws Exception {
		ChannelFactory factory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		ServerBootstrap bootstrap = new ServerBootstrap(factory);

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() {
				ChannelPipeline pipeline = Channels.pipeline();
				
				pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
				pipeline.addLast("protobufDecoder", new ProtobufDecoder(
						GalaxyEclipseProtocol.Packet.getDefaultInstance()));
				
				pipeline.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
				pipeline.addLast("protobufEncoder", new ProtobufEncoder());
				
				pipeline.addLast("testHandler", new TestServerHandler());
				
				return pipeline;
			}
		});

		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);

		bootstrap.bind(new InetSocketAddress(SharedTestInfo.PORT));
	}
}
