package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.server.authentication.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.*;
import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.Packet.*;
import org.apache.log4j.*;

/**
 * Processes the messages of unauthenticated players.
 */
class UnauthenticatedPacketHandler extends AbstractStubPacketHandler {
	private static final Logger log = Logger.getLogger(UnauthenticatedPacketHandler.class);
	
	private IServerChannelHandler channelHandler;
	private IClientAuthenticator authenticator;
	
	public UnauthenticatedPacketHandler(IServerChannelHandler channelHandler) {
		this.channelHandler = channelHandler;

		authenticator = ContextHolder.INSTANCE.getBean(IClientAuthenticator.class);
	}
	
	@Override
	public void handle(Packet packet) {
		// Currently handles only authentication request packets
		if (packet.getType() == Type.AUTH_REQUEST) { 
			AuthRequest request = packet.getAuthRequest();
			
			// Authentication the player
			AuthenticationResult result = authenticator.authenticate(
					request.getUsername(), request.getPassword());
			if (result.isSuccess()) {
				channelHandler.setPacketHandler(new FlightModePacketHandler(
                        channelHandler, result.getPlayer()));
			} 
			log.debug("Authentication user = " + request.getUsername() 
					+ " pass = " + request.getPassword() + " result = " + result.isSuccess());
			
			// Sending response as authentication result
			AuthResponse authResponse = AuthResponse.newBuilder()
					.setIsSuccess(result.isSuccess()).build();
			Packet response = Packet.newBuilder()
					.setType(Type.AUTH_RESPONSE)
					.setAuthResponse(authResponse).build();
			channelHandler.sendPacket(response);
		}		
	}
}
