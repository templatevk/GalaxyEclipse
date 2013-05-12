package arch.galaxyeclipse.server.data;

import arch.galaxyeclipse.server.data.DynamicObjectsHolder.LocationObjectsHolder;
import arch.galaxyeclipse.server.data.model.LocationObject;
import arch.galaxyeclipse.server.data.model.Player;
import arch.galaxyeclipse.server.data.model.ShipConfig;
import arch.galaxyeclipse.server.data.model.ShipState;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.ShipStateResponse;
import lombok.Data;

/**
 *
 */
@Data
public class PlayerInfoHolder {
    // MySQL
    private Player player;
    private ShipState shipState;
    private ShipConfig shipConfig;
    private LocationObject locationObject;
    // Protobuf
    private LocationObjectPacket.Builder lopBuilder;
    private ShipStateResponse.Builder ssrBuilder;

    private LocationObjectsHolder locationObjectsHolder;
}
