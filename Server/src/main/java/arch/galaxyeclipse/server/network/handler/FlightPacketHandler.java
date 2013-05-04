package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.data.*;
import arch.galaxyeclipse.server.data.model.*;
import arch.galaxyeclipse.server.network.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.*;
import arch.galaxyeclipse.shared.types.*;
import lombok.extern.slf4j.*;
import org.hibernate.*;

/**
* Handles packets of authenticated players.
*/
@Slf4j
class FlightPacketHandler implements IChannelAwarePacketHandler {
    private IServerChannelHandler serverChannelHandler;
    private DictionaryTypesMapper dictionaryTypesMapper;

    private PlayerInfoHolder playerInfoHolder;
    private Player player;

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

        hibernatePlayer();
    }

    @Override
    public IServerChannelHandler getServerChannelHandler() {
        return serverChannelHandler;
    }

    private void hibernatePlayer() {
        new UnitOfWork() {
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
