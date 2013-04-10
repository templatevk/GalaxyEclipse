package arch.galaxyeclipse.shared.network;

import arch.galaxyeclipse.shared.protocol.*;
import lombok.extern.slf4j.*;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.protobuf.*;

/**
 * Adds protobuf handlers and custom ones.
 */
@Slf4j
public abstract class AbstractProtobufChannelPipelineFactory implements ChannelPipelineFactory {
	protected abstract void configureHandlers(ChannelPipeline pipeline);
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();

		// Adding length and protobuf handlers
		pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
		pipeline.addLast("protobufDecoder", new ProtobufDecoder(
				GalaxyEclipseProtocol.Packet.getDefaultInstance()));
		
		pipeline.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
		pipeline.addLast("protobufEncoder", new ProtobufEncoder());
		
		configureHandlers(pipeline);
        if (log.isInfoEnabled()) {
		    log.info("Channel handlers configured");
        }
		
		return pipeline;
	}
}
