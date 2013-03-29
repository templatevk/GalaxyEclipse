package arch.galaxyeclipse.server.network;

import org.apache.log4j.*;

import arch.galaxyeclipse.server.authentication.*;
import arch.galaxyeclipse.shared.inject.*;
import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.AuthRequest;
import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.AuthResponse;
import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.Packet;
import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.Packet.Type;

public class UnauthenticatedPacketHandler implements IPacketHandler {
	private static final Logger log = Logger.getLogger(UnauthenticatedPacketHandler.class);
	
	private IServerChannelHandler channelHandler;
	private IClientAuthenticator authenticator;
	
	public UnauthenticatedPacketHandler(IServerChannelHandler channelHandler) {
		this.channelHandler = channelHandler;
		authenticator = SpringContextHolder.CONTEXT.getBean(IClientAuthenticator.class);
	}
	
	@Override
	public void handle(Packet packet) {
		if (packet.getType() == Type.AUTH_REQUEST) {
			AuthRequest request = packet.getAuthRequest();
			
			AuthenticationResult result = authenticator.authenticate(
					request.getUsername(), request.getPassword());
			if (result.isSuccess) {
				channelHandler.setPacketHandler(new AuthenticatedPacketHandler());
			} 
			log.debug("Authentication user = " + request.getUsername() 
					+ " pass = " + request.getPassword() + " result = " + result.isSuccess);
			
			AuthResponse authResponse = AuthResponse.newBuilder()
					.setIsSuccess(result.isSuccess).build();
			Packet response = Packet.newBuilder()
					.setType(Type.AUTH_RESPONSE)
					.setAuthResponse(authResponse).build();
			channelHandler.sendPacket(response);
		}		
	}
}
