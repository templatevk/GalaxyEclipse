package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.authentication.GeAuthenticationResult;
import arch.galaxyeclipse.server.authentication.IGeClientAuthenticator;
import arch.galaxyeclipse.server.data.GeDynamicObjectsHolder;
import arch.galaxyeclipse.server.data.GeDynamicObjectsHolder.GeLocationObjectsHolder;
import arch.galaxyeclipse.server.data.GeHibernateUnitOfWork;
import arch.galaxyeclipse.server.data.GePlayerInfoHolder;
import arch.galaxyeclipse.server.data.model.GeLocation;
import arch.galaxyeclipse.server.data.model.GeLocationObject;
import arch.galaxyeclipse.server.data.model.GePlayer;
import arch.galaxyeclipse.server.data.model.GeShipState;
import arch.galaxyeclipse.server.network.IGeServerChannelHandler;
import arch.galaxyeclipse.server.protocol.GeProtocolMessageFactory;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeLocationInfoPacket.GeLocationObjectPacket;
import arch.galaxyeclipse.shared.types.GeDictionaryTypesMapper;
import arch.galaxyeclipse.shared.types.GeLocationObjectBehaviorTypesMapperType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Processes the messages of unauthenticated players.
 */
@Slf4j
class GeUnauthenticatedPacketHandler extends GeStatefulPacketHandler {

    private GeDictionaryTypesMapper dictionaryTypesMapper;
    private IGeServerChannelHandler serverChannelHandler;
    private IGeClientAuthenticator authenticator;
    private GeProtocolMessageFactory messageFactory;

    public GeUnauthenticatedPacketHandler(IGeServerChannelHandler serverChannelHandler) {
        this.serverChannelHandler = serverChannelHandler;

        dictionaryTypesMapper = GeContextHolder.getBean(GeDictionaryTypesMapper.class);
        authenticator = GeContextHolder.getBean(IGeClientAuthenticator.class);
        messageFactory = GeContextHolder.getBean(GeProtocolMessageFactory.class);
    }

    @Override
    public boolean handle(GePacket packet) {
        switch (packet.getType()) {
            case AUTH_REQUEST:
                processAuthRequest(packet.getAuthRequest());
                return true;
        }
        return false;
    }

    private void processAuthRequest(GeAuthRequest authRequest) {
        GeAuthenticationResult authenticationResult = authenticator.authenticate(
                authRequest.getUsername(), authRequest.getPassword());
        sendAuthResponse(authenticationResult);

        if (authenticationResult.isSuccess()) {
            StartupInfoData startupInfoData = getStartupDataInfo(
                    authenticationResult.getPlayer().getPlayerId());
            indicatePlayerOnline(startupInfoData.getPlayer());
            processStartupInfoData(startupInfoData);
            sendStartupInfo(startupInfoData);

            // TODO: Depending on the player state!
            IGeStatefulPacketHandler statefulPacketHandler = GePacketHandlerFactory
                    .createStatefulPacketHandler(serverChannelHandler, GeGameModeType.FLIGHT);
            serverChannelHandler.setStatefulPacketHandler(statefulPacketHandler);
        }

        if (GeUnauthenticatedPacketHandler.log.isDebugEnabled()) {
            GeUnauthenticatedPacketHandler.log.debug("Authentication user = " + authRequest.getUsername() + ", pass = "
                    + authRequest.getPassword() + ", result = " + authenticationResult.isSuccess());
        }
    }

    private void indicatePlayerOnline(final GePlayer player) {
        new GeHibernateUnitOfWork() {
            @Override
            protected void doWork(Session session) {
                int idDynamic = dictionaryTypesMapper.getIdByLocationObjectBehaviorType(
                        GeLocationObjectBehaviorTypesMapperType.DYNAMIC);
                GeLocationObject locationObject = player.getLocationObject();
                locationObject.setLocationObjectBehaviorTypeId(idDynamic);

                session.merge(locationObject);
            }
        }.execute();
    }

    private void sendStartupInfo(StartupInfoData startupInfoData) {
        GeStartupInfoPacket startupInfo = GeStartupInfoPacket.newBuilder()
                .setShipStaticInfo(messageFactory.createShipStaticInfo(
                        startupInfoData.getPlayer()))
                .setLocationInfo(messageFactory.createLocationInfo(
                        startupInfoData.getLocation(),
                        startupInfoData.getLocationCachedObjects()))
                .setTypesMap(messageFactory.createTypesMap()).build();
        GePacket startupInfoPacket = GePacket.newBuilder().setType(GePacket.Type.STARTUP_INFO)
                .setStartupInfo(startupInfo).build();
        serverChannelHandler.sendPacket(startupInfoPacket);
    }

    private void sendAuthResponse(GeAuthenticationResult authenticationResult) {
        GeAuthResponse authResponse = GeAuthResponse.newBuilder()
                .setIsSuccess(authenticationResult.isSuccess()).build();
        GePacket authResponsePacket = GePacket.newBuilder()
                .setType(GePacket.Type.AUTH_RESPONSE)
                .setAuthResponse(authResponse).build();
        serverChannelHandler.sendPacket(authResponsePacket);
    }

    private StartupInfoData getStartupDataInfo(final int playerId) {
        StartupInfoData startupInfoData = new GeHibernateUnitOfWork<StartupInfoData>() {
            @Override
            protected void doWork(Session session) {
                StartupInfoData startupInfoData = new StartupInfoData();

                GePlayer player = (GePlayer) session.getNamedQuery("player.startupInfo")
                        .setParameter("playerId", playerId).uniqueResult();
                startupInfoData.setPlayer(player);

                GeLocation location = player.getLocationObject().getLocation();
                startupInfoData.setLocation(location);

                int idStatic = dictionaryTypesMapper.getIdByLocationObjectBehaviorType(
                        GeLocationObjectBehaviorTypesMapperType.STATIC);
                int idDrawable = dictionaryTypesMapper.getIdByLocationObjectBehaviorType(
                        GeLocationObjectBehaviorTypesMapperType.DRAWABLE);

                startupInfoData.setLocationCachedObjects(
                        session.createCriteria(GeLocationObject.class)
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
        GePlayerInfoHolder playerInfoHolder = serverChannelHandler.getPlayerInfoHolder();
        GeLocationObject locationObject = startupInfoData.getPlayer().getLocationObject();
        GeShipState shipState = startupInfoData.getPlayer().getShipState();

        GeLocationObjectPacket lop = messageFactory.getLocationObject(locationObject);
        GeShipStateResponse ssr = messageFactory.createShipStateResponse(shipState, locationObject);
        GeLocationObjectPacket.Builder lopBuilder = GeLocationObjectPacket.newBuilder().mergeFrom(lop);
        GeShipStateResponse.Builder ssrBuilder = GeShipStateResponse.newBuilder().mergeFrom(ssr);

        int locationId = locationObject.getLocationId();
        GeDynamicObjectsHolder dynamicObjectsHolder = GeContextHolder.getBean(
                GeDynamicObjectsHolder.class);
        GeLocationObjectsHolder locationObjectsHolder = dynamicObjectsHolder
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

        private GePlayer player;
        private List<GeLocationObject> locationCachedObjects;
        private GeLocation location;
    }
}

