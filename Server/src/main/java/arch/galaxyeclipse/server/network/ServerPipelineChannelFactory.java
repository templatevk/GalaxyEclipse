package arch.galaxyeclipse.server.network;

import org.jboss.netty.channel.*;

import arch.galaxyeclipse.shared.network.*;

public class ServerPipelineChannelFactory extends AbstractProtobufChannelPipelineFactory {
	@Override
	protected void configureHandlers(ChannelPipeline pipeline) {
		pipeline.addLast("serverHandler", new ServerChannelHandler());
	}
}
