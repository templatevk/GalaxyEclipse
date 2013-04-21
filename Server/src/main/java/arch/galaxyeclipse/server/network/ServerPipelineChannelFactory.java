package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.shared.network.*;
import org.jboss.netty.channel.*;

class ServerPipelineChannelFactory extends ProtobufChannelPipelineFactory {
	@Override
	protected void configureHandlers(ChannelPipeline pipeline) {
		pipeline.addLast("serverHandler", new ServerChannelHandler());
	}
}
