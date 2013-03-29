package arch.galaxyeclipse.shared.network;

import org.apache.log4j.*;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.protobuf.*;

import arch.galaxyeclipse.shared.protocol.*;

public abstract class AbstractProtobufChannelPipelineFactory implements ChannelPipelineFactory {
	private static final Logger log = Logger.getLogger(AbstractProtobufChannelPipelineFactory.class);
	
	protected abstract void configureHandlers(ChannelPipeline pipeline);
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		
		pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
		pipeline.addLast("protobufDecoder", new ProtobufDecoder(
				GalaxyEclipseProtocol.Packet.getDefaultInstance()));
		
		pipeline.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
		pipeline.addLast("protobufEncoder", new ProtobufEncoder());
		
		configureHandlers(pipeline);
		log.info("Channel handlers configured");
		
		return pipeline;
	}
}
