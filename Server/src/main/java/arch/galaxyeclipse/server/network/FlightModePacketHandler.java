package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.server.data.model.*;
import arch.galaxyeclipse.server.data.repository.jpa.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.*;
import arch.galaxyeclipse.shared.types.*;
import lombok.extern.slf4j.*;

/**
 * Handles packets of authenticated players.
 */
@Slf4j
class FlightModePacketHandler implements IPacketHandler {
    private IServerChannelHandler channelHandler;
    private DictionaryTypesMapper dictionaryTypesMapper;

    private IShipStatesRepository shipStatesRepository;
    private IShipConfigsRepository shipConfigsRepository;
    private ILocationObjectsRepository locationObjectsRepository;

    private Players player;
    private ShipStates shipState;
    private ShipConfigs shipConfig;

    private LocationObjects locationObject;

    public FlightModePacketHandler(IServerChannelHandler channelHandler, Players player) {
        this.channelHandler = channelHandler;
        this.player = player;

        // Resolve dependencies
        dictionaryTypesMapper = ContextHolder.INSTANCE.getBean(DictionaryTypesMapper.class);
        shipStatesRepository = ContextHolder.INSTANCE.getBean(IShipStatesRepository.class);
        shipConfigsRepository = ContextHolder.INSTANCE.getBean(IShipConfigsRepository.class);
        locationObjectsRepository = ContextHolder.INSTANCE.getBean(
                ILocationObjectsRepository.class);

        // Initialize player's data e.g. ship state and ship config
        shipState = shipStatesRepository.findByPlayerId(player.getPlayerId());
        shipConfig = shipConfigsRepository.findByPlayerId(player.getPlayerId());
        locationObject = locationObjectsRepository.findOne(shipState.getLocationObjectId());

        // Indicate player is online
        locationObject.setLocationObjectBehaviorTypeId(dictionaryTypesMapper
                .getIdByLocationObjectBehaviorType(LocationObjectBehaviorTypesMapperType.DYNAMIC));
	}
	
	@Override
	public void handle(Packet packet) {

	}

    @Override
    public void onChannelClosed() {
        if (log.isDebugEnabled()) {
            log.debug("Channel closed during flight mode, hibernating player");
        }

        // Stop the ship
        shipState.setShipStateMoveSpeed(0);
        shipState.setShipStateRotationSpeed(0);

        // Indicate player is offline
        locationObject.setLocationObjectBehaviorTypeId(dictionaryTypesMapper
                .getIdByLocationObjectBehaviorType(LocationObjectBehaviorTypesMapperType.STATIC));
    }
}
