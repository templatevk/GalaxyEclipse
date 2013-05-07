package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.authentication.AuthenticationResult;
import arch.galaxyeclipse.server.authentication.IClientAuthenticator;
import arch.galaxyeclipse.server.data.HibernateUnitOfWork;
import arch.galaxyeclipse.server.data.JedisSerializers.LocationObjectPacketSerializer;
import arch.galaxyeclipse.server.data.PlayerInfoHolder;
import arch.galaxyeclipse.server.data.RedisUnitOfWork;
import arch.galaxyeclipse.server.data.model.Location;
import arch.galaxyeclipse.server.data.model.LocationObject;
import arch.galaxyeclipse.server.data.model.Player;
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
import org.springframework.data.redis.connection.jedis.JedisConnection;

import java.util.List;

import static arch.galaxyeclipse.server.data.RedisStorage.*;

/**
 * Processes the messages of unauthenticated players.
 */
@Slf4j
class UnauthenticatedPacketHandler extends StatefulPacketHandler {
    private DictionaryTypesMapper dictionaryTypesMapper;
    private IServerChannelHandler serverChannelHandler;
	private IClientAuthenticator authenticator;
    private GeProtocolMessageFactory geProtocolMessageFactory;

    public UnauthenticatedPacketHandler(IServerChannelHandler serverChannelHandler) {
		this.serverChannelHandler = serverChannelHandler;

        dictionaryTypesMapper = ContextHolder.getBean(DictionaryTypesMapper.class);
		authenticator = ContextHolder.getBean(IClientAuthenticator.class);
        geProtocolMessageFactory = ContextHolder.getBean(GeProtocolMessageFactory.class);
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
        final PlayerInfoHolder playerInfoHolder = serverChannelHandler.getPlayerInfoHolder();
        playerInfoHolder.setPlayer(startupInfoData.getPlayer());
        playerInfoHolder.setShipConfig(startupInfoData.getPlayer().getShipConfig());
        playerInfoHolder.setShipState(startupInfoData.getPlayer().getShipState());
        playerInfoHolder.setLocationObject(startupInfoData.getPlayer().getLocationObject());

        new RedisUnitOfWork() {
            @Override
            protected void doWork(JedisConnection connection) {
                LocationObjectPacket lop = geProtocolMessageFactory.getLocationObject(
                        playerInfoHolder.getLocationObject());
                ShipStateResponse ssr = geProtocolMessageFactory.createShipStateResponse(
                        playerInfoHolder.getShipState(), playerInfoHolder.getLocationObject());
                LocationObjectPacketSerializer lopSerializer = new LocationObjectPacketSerializer();

                int locationObjectId = playerInfoHolder.getLocationObject().getLocationObjectId();
                int locationId = playerInfoHolder.getLocationObject().getLocationId();
                byte[] lopHashKey = getLocationObjectPacketHashKey(locationObjectId);
                byte[] lopSortedSetXKey = getLocationObjectPacketSortedSetXKey(locationId);
                byte[] lopSortedSetYKey = getLocationObjectPacketSortedSetYKey(locationId);

                connection.openPipeline();
                connection.hSet(lopHashKey, lopHashKey,
                        lopSerializer.serialize(lop));
                connection.zAdd(lopSortedSetXKey, lop.getPositionX(), lopHashKey);
                connection.zAdd(lopSortedSetYKey, lop.getPositionY(), lopHashKey);
                connection.closePipeline();

                playerInfoHolder.setLocationObjectPacketHashKey(lopHashKey);
                playerInfoHolder.setLocationObjectPacketSortedSetXKey(lopSortedSetXKey);
                playerInfoHolder.setLocationObjectPacketSortedSetYKey(lopSortedSetYKey);

                byte[] lopBufSetXKey = getLocationObjectPacketBufSetXKey(locationObjectId);
                byte[] lopBufSetYKey = getLocationObjectPacketBufSetYKey(locationObjectId);
                playerInfoHolder.setLocationObjectPacketBufSetXKey(lopBufSetXKey);
                playerInfoHolder.setLocationObjectPacketBufSetYKey(lopBufSetYKey);
            }
        }.execute();
    }

    @Data
    private static class StartupInfoData {
        private Player player;
        private List<LocationObject> locationCachedObjects;
        private Location location;
    }
}

