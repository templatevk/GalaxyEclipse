package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.authentication.*;
import arch.galaxyeclipse.server.data.*;
import arch.galaxyeclipse.server.data.model.*;
import arch.galaxyeclipse.server.network.*;
import arch.galaxyeclipse.server.protocol.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.Packet.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.Packet.*;
import arch.galaxyeclipse.shared.types.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.hibernate.*;
import org.hibernate.annotations.*;
import org.hibernate.criterion.*;

import java.util.*;

/**
 * Processes the messages of unauthenticated players.
 */
@Slf4j
class UnauthenticatedPacketHandler extends AbstractStubPacketHandler {
    private DictionaryTypesMapper dictionaryTypesMapper;
    private IServerChannelHandler serverChannelHandler;
	private IClientAuthenticator authenticator;
    private GeProtocolMessageFactory geProtocolMessageFactory;

	public UnauthenticatedPacketHandler(IServerChannelHandler serverChannelHandler) {
		this.serverChannelHandler = serverChannelHandler;

        dictionaryTypesMapper = ContextHolder.INSTANCE.getBean(DictionaryTypesMapper.class);
		authenticator = ContextHolder.INSTANCE.getBean(IClientAuthenticator.class);
        geProtocolMessageFactory = ContextHolder.INSTANCE.getBean(GeProtocolMessageFactory.class);
	}

	@Override
	public void handle(Packet packet) {

        switch (packet.getType()) {
            case AUTH_REQUEST:
                processAuthRequest(packet.getAuthRequest());
                break;
        }
	}

    private void processAuthRequest(AuthRequest authRequest) {
        AuthenticationResult authenticationResult = authenticator.authenticate(
                authRequest.getUsername(), authRequest.getPassword());
        sendAuthResponse(authenticationResult);

        if (authenticationResult.isSuccess()) {
            StartupInfoData startupInfoData = getStartupDataInfo(
                    authenticationResult.getPlayer().getPlayerId());
            sendStartupInfo(startupInfoData);

            indicatePlayerOnline(startupInfoData.getPlayer());

            // TODO: Depending on the player state!
            serverChannelHandler.setStatefulPacketHandler(new FlightPacketHandler(serverChannelHandler));
        }

        if (UnauthenticatedPacketHandler.log.isDebugEnabled()) {
            UnauthenticatedPacketHandler.log.debug("Authentication user = " + authRequest.getUsername() + ", pass = "
                    + authRequest.getPassword() + ", result = " + authenticationResult.isSuccess());
        }
    }

    private void indicatePlayerOnline(final Player player) {
        new UnitOfWork() {
            @Override
            protected void doWork(Session session) {
                int idDynamic = dictionaryTypesMapper.getIdByLocationObjectBehaviorType(
                        LocationObjectBehaviorTypesMapperType.DYNAMIC);
                LocationObject locationObject = player.getLocationObject();
                locationObject.setLocationObjectBehaviorTypeId(idDynamic);

                session.merge(locationObject);
            }
        }.execute();
    }

    private void sendStartupInfo(StartupInfoData startupInfoData) {
        StartupInfo startupInfo = StartupInfo.newBuilder()
                .setShipStaticInfo(geProtocolMessageFactory.createShipStaticInfo(
                        startupInfoData.getPlayer()))
                .setLocationInfo(geProtocolMessageFactory.createLocationInfo(
                        startupInfoData.getLocation(),
                        startupInfoData.getLocationCachedObjects()))
                .setTypesMap(geProtocolMessageFactory.createTypesMap()).build();
        Packet startupInfoPacket = Packet.newBuilder().setType(Packet.Type.STARTUP_INFO)
                .setStartupInfo(startupInfo).build();
        serverChannelHandler.sendPacket(startupInfoPacket);
    }

    private void sendAuthResponse(AuthenticationResult authenticationResult) {
        AuthResponse authResponse = AuthResponse.newBuilder()
                .setIsSuccess(authenticationResult.isSuccess()).build();
        Packet authResponsePacket = Packet.newBuilder()
                .setType(Packet.Type.AUTH_RESPONSE)
                .setAuthResponse(authResponse).build();
        serverChannelHandler.sendPacket(authResponsePacket);
    }

    private StartupInfoData getStartupDataInfo(final int playerId) {
        StartupInfoData startupInfoData = new UnitOfWork<StartupInfoData>() {
            @Override
            protected void doWork(Session session) {
                StartupInfoData startupInfoData = new StartupInfoData();

                Player player = (Player)session.getNamedQuery("player.startupInfo")
                        .setParameter("playerId", playerId).uniqueResult();
                startupInfoData.setPlayer(player);

                Location location = player.getLocationObject().getLocation();
                startupInfoData.setLocation(location);

                int idStatic = dictionaryTypesMapper.getIdByLocationObjectBehaviorType(
                        LocationObjectBehaviorTypesMapperType.STATIC);
                int idDrawable = dictionaryTypesMapper.getIdByLocationObjectBehaviorType(
                        LocationObjectBehaviorTypesMapperType.DRAWABLE);

                startupInfoData.setLocationCachedObjects(
                        session.createCriteria(LocationObject.class)
                                .add(Restrictions.or(
                                        Restrictions.eq("locationObjectBehaviorTypeId", idStatic),
                                        Restrictions.eq("locationObjectBehaviorTypeId", idDrawable)))
                                .add(Restrictions.eq("locationId", location.getLocationId())).list());

                setResult(startupInfoData);
            }
        }.execute();
        fillPlayerInfoHolder(startupInfoData);

        return startupInfoData;
    }

    private void fillPlayerInfoHolder(StartupInfoData startupInfoData) {
        PlayerInfoHolder playerInfoHolder = serverChannelHandler.getPlayerInfoHolder();
        playerInfoHolder.setPlayer(startupInfoData.getPlayer());
        playerInfoHolder.setShipConfig(startupInfoData.getPlayer().getShipConfig());
        playerInfoHolder.setShipState(startupInfoData.getPlayer().getShipState());
        playerInfoHolder.setLocationObject(startupInfoData.getPlayer().getLocationObject());
    }

    @Data
    private static class StartupInfoData {
        private Player player;
        private List<LocationObject> locationCachedObjects;
        private Location location;
    }
}

