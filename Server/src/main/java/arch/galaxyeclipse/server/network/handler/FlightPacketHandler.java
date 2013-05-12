package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.data.DynamicObjectsHolder.LocationObjectsHolder;
import arch.galaxyeclipse.server.data.HibernateUnitOfWork;
import arch.galaxyeclipse.server.data.PlayerInfoHolder;
import arch.galaxyeclipse.server.network.IServerChannelHandler;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket.Builder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.Packet;
import arch.galaxyeclipse.shared.types.DictionaryTypesMapper;
import arch.galaxyeclipse.shared.types.LocationObjectBehaviorTypesMapperType;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

/**
* Handles packets of authenticated players.
*/
@Slf4j
class FlightPacketHandler implements IChannelAwarePacketHandler {
    private IServerChannelHandler serverChannelHandler;
    private DictionaryTypesMapper dictionaryTypesMapper;

    private PlayerInfoHolder playerInfoHolder;

    public FlightPacketHandler(IServerChannelHandler serverChannelHandler) {
        this.serverChannelHandler = serverChannelHandler;

        dictionaryTypesMapper = ContextHolder.getBean(DictionaryTypesMapper.class);
        playerInfoHolder = serverChannelHandler.getPlayerInfoHolder();
	}

	@Override
	public boolean handle(Packet packet) {
        return false;
	}

    @Override
    public void onChannelClosed() {
        if (FlightPacketHandler.log.isDebugEnabled()) {
            FlightPacketHandler.log.debug("Channel closed during flight mode, hibernating player");
        }
        Builder lopBuilder = playerInfoHolder.getLopBuilder();
        LocationObjectsHolder locationObjectsHolder = playerInfoHolder.getLocationObjectsHolder();
        locationObjectsHolder.removeLopBuilder(lopBuilder);
        hibernatePlayer();
    }

    @Override
    public IServerChannelHandler getServerChannelHandler() {
        return serverChannelHandler;
    }

    private void hibernatePlayer() {
        new HibernateUnitOfWork() {
            @Override
            protected void doWork(Session session) {
                // Stop the ship
                playerInfoHolder.getShipState().setShipStateMoveSpeed(0);
                playerInfoHolder.getShipState().setShipStateRotationSpeed(0);

                // Indicate player is offline
                int idIgnored = dictionaryTypesMapper.getIdByLocationObjectBehaviorType(
                        LocationObjectBehaviorTypesMapperType.IGNORED);
                playerInfoHolder.getLocationObject().setLocationObjectBehaviorTypeId(idIgnored);

                session.merge(playerInfoHolder.getShipState());
                session.merge(playerInfoHolder.getLocationObject());
            }
        }.execute();
    }
}
