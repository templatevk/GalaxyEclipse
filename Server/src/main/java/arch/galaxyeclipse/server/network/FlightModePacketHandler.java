package arch.galaxyeclipse.server.network;

import arch.galaxyeclipse.server.data.model.*;
import arch.galaxyeclipse.server.data.repository.*;
import arch.galaxyeclipse.shared.inject.*;
import arch.galaxyeclipse.shared.protocol.GalaxyEclipseProtocol.*;

/**
 * Handles packets of authenticated players.
 */
class FlightModePacketHandler implements IPacketHandler {
    private IServerChannelHandler channelHandler;

    private Players player;
    private ShipStates shipState;
    private ShipConfigs shipConfig;

    private ShipStatesRepository shipStatesRepository;
    private ShipConfigsRepository shipConfigsRepository;

    public FlightModePacketHandler(IServerChannelHandler channelHandler, Players player) {
        this.channelHandler = channelHandler;
        this.player = player;

        // Resolve dependencies
        shipStatesRepository = SpringContextHolder.CONTEXT.getBean(ShipStatesRepository.class);
        shipConfigsRepository = SpringContextHolder.CONTEXT.getBean(ShipConfigsRepository.class);

        // Initialize player's data e.g. ship state and ship config
        shipState = shipStatesRepository.findByPlayerId(player.getPlayerId());
        shipConfig = shipConfigsRepository.findByPlayerId(player.getPlayerId());
	}
	
	@Override
	public void handle(Packet packet) {

	}
}
