package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.shared.network.ProtobufChannelPipelineFactory;
import org.jboss.netty.channel.ChannelPipeline;

class ServerPipelineChannelFactory extends ProtobufChannelPipelineFactory {
	@Override
	protected void configureHandlers(ChannelPipeline pipeline) {
		pipeline.addLast("serverHandler", new ServerChannelHandler());
	}
}
