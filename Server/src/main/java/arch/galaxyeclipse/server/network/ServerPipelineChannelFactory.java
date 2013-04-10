package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.shared.network.*;
import org.jboss.netty.channel.*;

class ServerPipelineChannelFactory extends AbstractProtobufChannelPipelineFactory {
	@Override
	protected void configureHandlers(ChannelPipeline pipeline) {
		pipeline.addLast("serverHandler", new ServerChannelHandler());
	}
}
