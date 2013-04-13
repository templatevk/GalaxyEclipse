package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.server.data.model.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.*;
import arch.galaxyeclipse.shared.types.*;
import lombok.extern.slf4j.*;

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
        //shipConfig = player.getShipConfig();
        locationObject = shipState.getLocationObject();

        // Indicate player is online
        locationObject.setLocationObjectBehaviorTypeId(dictionaryTypesMapper
                .getIdByLocationObjectBehaviorType(LocationObjectBehaviorTypesMapperType.DYNAMIC));
	}

	@Override
	public void handle(Packet packet) {

	}

    @Override
    public void onChannelClosed() {
        if (FlightPacketHandler.log.isDebugEnabled()) {
            FlightPacketHandler.log.debug("Channel closed during flight mode, hibernating player");
        }

        // Stop the ship
        shipState.setShipStateMoveSpeed(0);
        shipState.setShipStateRotationSpeed(0);

        // Indicate player is offline
        locationObject.setLocationObjectBehaviorTypeId(dictionaryTypesMapper
                .getIdByLocationObjectBehaviorType(LocationObjectBehaviorTypesMapperType.STATIC));
    }
}
