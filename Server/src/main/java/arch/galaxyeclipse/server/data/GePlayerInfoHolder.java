package arch.galaxyeclipse.server.data;

import arch.galaxyeclipse.server.data.GeDynamicObjectsHolder.GeLocationObjectsHolder;
import arch.galaxyeclipse.server.data.model.GeLocationObject;
import arch.galaxyeclipse.server.data.model.GePlayer;
import arch.galaxyeclipse.server.data.model.GeShipConfig;
import arch.galaxyeclipse.server.data.model.GeShipState;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeLocationInfoPacket.GeLocationObjectPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeShipStateResponse;
import lombok.Data;

/**
 *
 */
@Data
public class GePlayerInfoHolder {
    // MySQL
    private GePlayer player;
    private GeShipState shipState;
    private GeShipConfig shipConfig;
    private GeLocationObject locationObject;
    // Protobuf
    private GeLocationObjectPacket.Builder lopBuilder;
    private GeShipStateResponse.Builder ssrBuilder;

    private GeLocationObjectsHolder locationObjectsHolder;
}
