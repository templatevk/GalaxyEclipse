package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.shared.network.GeProtobufChannelPipelineFactory;
import org.jboss.netty.channel.ChannelPipeline;

class GeServerPipelineChannelFactory extends GeProtobufChannelPipelineFactory {
	@Override
	protected void configureHandlers(ChannelPipeline pipeline) {
		pipeline.addLast("serverHandler", new GeServerChannelHandler());
	}
}
