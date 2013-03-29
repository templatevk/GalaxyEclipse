package arch.galaxyeclipse.server.network;

import org.jboss.netty.channel.*;
import org.springframework.stereotype.*;

import arch.galaxyeclipse.shared.network.*;

@Component
public class ServerPipelineChannelFactory extends AbstractProtobufChannelPipelineFactory {
	@Override
	protected void configureHandlers(ChannelPipeline pipeline) {
		pipeline.addLast("serverHandler", new ServerChannelHandler());
	}
}
