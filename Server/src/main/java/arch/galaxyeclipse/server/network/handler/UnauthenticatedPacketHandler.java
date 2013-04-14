package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.authentication.*;
import arch.galaxyeclipse.server.data.*;
import arch.galaxyeclipse.server.data.model.*;
import arch.galaxyeclipse.server.network.*;
import arch.galaxyeclipse.server.protocol.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.Packet.*;
import arch.galaxyeclipse.shared.types.*;
import lombok.extern.slf4j.*;
import org.hibernate.*;

/**
 * Processes the messages of unauthenticated players.
 */
@Slf4j
class UnauthenticatedPacketHandler extends AbstractStubPacketHandler {
    private DictionaryTypesMapper dictionaryTypesMapper;
    private IServerChannelHandler channelHandler;
	private IClientAuthenticator authenticator;
    private GeProtocolMessageFactory protocolMessageFactory;

	public UnauthenticatedPacketHandler(IServerChannelHandler channelHandler) {
		this.channelHandler = channelHandler;

        dictionaryTypesMapper = ContextHolder.INSTANCE.getBean(DictionaryTypesMapper.class);
		authenticator = ContextHolder.INSTANCE.getBean(IClientAuthenticator.class);
        protocolMessageFactory = ContextHolder.INSTANCE.getBean(GeProtocolMessageFactory.class);
	}

	@Override
	public void handle(Packet packet) {
		// Currently handles only authentication request packets
		if (packet.getType() == Type.AUTH_REQUEST) {
			AuthRequest request = packet.getAuthRequest();

            final AuthenticationResult result = authenticator.authenticate(
					request.getUsername(), request.getPassword());

            if (result.isSuccess()) {
                // TODO: Depending on the player state!
				channelHandler.setStatefulPacketHandler(new FlightPacketHandler(
                        channelHandler, result.getPlayer()));


                new UnitOfWork() {
                    @Override
                    protected void doWork(Session session) {
                        // Load player data to build ship static info
                        result.setPlayer((Player)session.createCriteria(Player.class)
                                .setFetchMode("shipStates", FetchMode.JOIN)
                                .setFetchMode("inventoryItems", FetchMode.JOIN)
                                .setFetchMode("inventoryItems.item", FetchMode.JOIN)
                                .setFetchMode("shipConfigs", FetchMode.JOIN)
                                .setFetchMode("shipStates.locationObject", FetchMode.JOIN)
                                .uniqueResult());

                        // Indicate player is online
                        int idDynamic = dictionaryTypesMapper.getIdByLocationObjectBehaviorType(
                                LocationObjectBehaviorTypesMapperType.DYNAMIC);
                        LocationObject locationObject = result.getPlayer()
                                .getShipState().getLocationObject();
                        locationObject.setLocationObjectBehaviorTypeId(idDynamic);

                        session.merge(locationObject);
                    }
                }.execute();
			}

            if (UnauthenticatedPacketHandler.log.isDebugEnabled()) {
			    UnauthenticatedPacketHandler.log.debug("Authentication user = " + request.getUsername() + ", pass = "
                        + request.getPassword() + ", result = " + result.isSuccess());
            }

			// Sending response as authentication result
			AuthResponse authResponse = AuthResponse.newBuilder()
					.setIsSuccess(result.isSuccess()).build();
			Packet response = Packet.newBuilder()
					.setType(Type.AUTH_RESPONSE)
					.setAuthResponse(authResponse).build();


            // TODO Sending StartupInfo
            StartupInfo.Builder startupInfoBuilder = StartupInfo.newBuilder();


            channelHandler.sendPacket(response);
        }
	}
}

