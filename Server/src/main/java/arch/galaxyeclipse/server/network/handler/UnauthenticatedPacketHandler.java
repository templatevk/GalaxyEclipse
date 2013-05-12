package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.authentication.AuthenticationResult;
import arch.galaxyeclipse.server.authentication.IClientAuthenticator;
import arch.galaxyeclipse.server.data.DynamicObjectsHolder;
import arch.galaxyeclipse.server.data.DynamicObjectsHolder.LocationObjectsHolder;
import arch.galaxyeclipse.server.data.HibernateUnitOfWork;
import arch.galaxyeclipse.server.data.PlayerInfoHolder;
import arch.galaxyeclipse.server.data.model.Location;
import arch.galaxyeclipse.server.data.model.LocationObject;
import arch.galaxyeclipse.server.data.model.Player;
import arch.galaxyeclipse.server.data.model.ShipState;
import arch.galaxyeclipse.server.network.IServerChannelHandler;
import arch.galaxyeclipse.server.protocol.GeProtocolMessageFactory;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket;
import arch.galaxyeclipse.shared.types.DictionaryTypesMapper;
import arch.galaxyeclipse.shared.types.LocationObjectBehaviorTypesMapperType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Processes the messages of unauthenticated players.
 */
@Slf4j
class UnauthenticatedPacketHandler extends StatefulPacketHandler {
    private DictionaryTypesMapper dictionaryTypesMapper;
    private IServerChannelHandler serverChannelHandler;
	private IClientAuthenticator authenticator;
    private GeProtocolMessageFactory messageFactory;

    public UnauthenticatedPacketHandler(IServerChannelHandler serverChannelHandler) {
		this.serverChannelHandler = serverChannelHandler;

        dictionaryTypesMapper = ContextHolder.getBean(DictionaryTypesMapper.class);
		authenticator = ContextHolder.getBean(IClientAuthenticator.class);
        messageFactory = ContextHolder.getBean(GeProtocolMessageFactory.class);
	}

	@Override
	public boolean handle(Packet packet) {
        switch (packet.getType()) {
            case AUTH_REQUEST:
                processAuthRequest(packet.getAuthRequest());
                return true;
        }
        return false;
	}

    private void processAuthRequest(AuthRequest authRequest) {
        AuthenticationResult authenticationResult = authenticator.authenticate(
                authRequest.getUsername(), authRequest.getPassword());
        sendAuthResponse(authenticationResult);

        if (authenticationResult.isSuccess()) {
            StartupInfoData startupInfoData = getStartupDataInfo(
                    authenticationResult.getPlayer().getPlayerId());
            indicatePlayerOnline(startupInfoData.getPlayer());
            processStartupInfoData(startupInfoData);
            sendStartupInfo(startupInfoData);

            // TODO: Depending on the player state!
            IStatefulPacketHandler statefulPacketHandler = PacketHandlerFactory
                    .createStatefulPacketHandler(serverChannelHandler, GameModeType.FLIGHT);
            serverChannelHandler.setStatefulPacketHandler(statefulPacketHandler);
        }

        if (UnauthenticatedPacketHandler.log.isDebugEnabled()) {
            UnauthenticatedPacketHandler.log.debug("Authentication user = " + authRequest.getUsername() + ", pass = "
                    + authRequest.getPassword() + ", result = " + authenticationResult.isSuccess());
        }
    }

    private void indicatePlayerOnline(final Player player) {
        new HibernateUnitOfWork() {
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
        StartupInfoPacket startupInfo = StartupInfoPacket.newBuilder()
                .setShipStaticInfo(messageFactory.createShipStaticInfo(
                        startupInfoData.getPlayer()))
                .setLocationInfo(messageFactory.createLocationInfo(
                        startupInfoData.getLocation(),
                        startupInfoData.getLocationCachedObjects()))
                .setTypesMap(messageFactory.createTypesMap()).build();
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
        StartupInfoData startupInfoData = new HibernateUnitOfWork<StartupInfoData>() {
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

        return startupInfoData;
    }

    private void processStartupInfoData(StartupInfoData startupInfoData) {
        PlayerInfoHolder playerInfoHolder = serverChannelHandler.getPlayerInfoHolder();
        LocationObject locationObject = startupInfoData.getPlayer().getLocationObject();
        ShipState shipState = startupInfoData.getPlayer().getShipState();

        LocationObjectPacket lop = messageFactory.getLocationObject(locationObject);
        ShipStateResponse ssr = messageFactory.createShipStateResponse(shipState, locationObject);
        LocationObjectPacket.Builder lopBuilder = LocationObjectPacket.newBuilder().mergeFrom(lop);
        ShipStateResponse.Builder ssrBuilder = ShipStateResponse.newBuilder().mergeFrom(ssr);

        int locationId = locationObject.getLocationId();
        DynamicObjectsHolder dynamicObjectsHolder = ContextHolder.getBean(
                DynamicObjectsHolder.class);
        LocationObjectsHolder locationObjectsHolder = dynamicObjectsHolder
                .getLocationObjectsHolder(locationId);
        locationObjectsHolder.addLopBuilder(lopBuilder);

        playerInfoHolder.setPlayer(startupInfoData.getPlayer());
        playerInfoHolder.setShipConfig(startupInfoData.getPlayer().getShipConfig());
        playerInfoHolder.setShipState(shipState);
        playerInfoHolder.setLocationObjectsHolder(locationObjectsHolder);
        playerInfoHolder.setLocationObject(locationObject);
        playerInfoHolder.setLopBuilder(lopBuilder);
        playerInfoHolder.setSsrBuilder(ssrBuilder);
    }

    @Data
    private static class StartupInfoData {
        private Player player;
        private List<LocationObject> locationCachedObjects;
        private Location location;
    }
}

