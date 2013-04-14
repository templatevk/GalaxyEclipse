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
class FlightPacketHandler implements IStatefulPacketHandler {
    private IServerChannelHandler channelHandler;
    private DictionaryTypesMapper dictionaryTypesMapper;

    private Player player;
    private ShipState shipState;
    private ShipConfig shipConfig;
    private LocationObject locationObject;

    public FlightPacketHandler(IServerChannelHandler channelHandler, Player player) {
        this.channelHandler = channelHandler;
        this.player = player;

        // Resolve dependencies
        dictionaryTypesMapper = ContextHolder.INSTANCE.getBean(DictionaryTypesMapper.class);

        // Initialize player's data e.g. ship state and ship config
        shipState = player.getShipState();
        shipConfig = player.getShipConfig();
        locationObject = shipState.getLocationObject();
	}

	@Override
	public void handle(Packet packet) {

	}

    @Override
    public void onChannelClosed() {
        if (FlightPacketHandler.log.isDebugEnabled()) {
            FlightPacketHandler.log.debug("Channel closed during flight mode, hibernating player");
        }

        new UnitOfWork() {
            @Override
            protected void doWork(Session session) {
                // Stop the ship
                shipState.setShipStateMoveSpeed(0);
                shipState.setShipStateRotationSpeed(0);

                // Indicate player is offline
                int idStatic = dictionaryTypesMapper.getIdByLocationObjectBehaviorType(
                        LocationObjectBehaviorTypesMapperType.STATIC);
                locationObject.setLocationObjectBehaviorTypeId(idStatic);

                session.merge(shipState);
                session.merge(locationObject);
            }
        }.execute();
    }
}
