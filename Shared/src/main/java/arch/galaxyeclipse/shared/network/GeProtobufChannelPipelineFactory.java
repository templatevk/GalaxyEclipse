package arch.galaxyeclipse.shared.network;

import arch.galaxyeclipse.shared.protocol.GeProtocol.GePacket;
import lombok.extern.slf4j.Slf4j;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * Adds protobuf handlers and custom ones.
 */
@Slf4j
public abstract class GeProtobufChannelPipelineFactory implements ChannelPipelineFactory {
	protected abstract void configureHandlers(ChannelPipeline pipeline);
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();

		// Adding length and protobuf handlers
		pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
		pipeline.addLast("protobufDecoder", new ProtobufDecoder(GePacket.getDefaultInstance()));
		
		pipeline.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
		pipeline.addLast("protobufEncoder", new ProtobufEncoder());
		
		configureHandlers(pipeline);
        if (GeProtobufChannelPipelineFactory.log.isDebugEnabled()) {
		    GeProtobufChannelPipelineFactory.log.debug("Channel handlers configured");
        }
		
		return pipeline;
	}
}
