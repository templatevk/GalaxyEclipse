package arch.galaxyeclipse.server.data;

import arch.galaxyeclipse.server.data.GeDynamicObjectsHolder.GeLocationObjectsHolder;
import arch.galaxyeclipse.server.data.model.GeLocationObject;
import arch.galaxyeclipse.server.data.model.GePlayer;
import arch.galaxyeclipse.server.data.model.GeShipConfig;
import arch.galaxyeclipse.server.data.model.GeShipState;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeLocationInfoPacket.GeLocationObjectPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeLocationInfoPacket.GeLocationObjectPacket.Builder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeShipStateResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class GePlayerInfoHolder {

    private static Map<Integer, GePlayerInfoHolder> thisById = new HashMap<>();

    // MySQL
    private @Getter @Setter GePlayer player;
    private @Getter @Setter GeShipState shipState;
    private @Getter @Setter GeShipConfig shipConfig;
    private @Getter @Setter GeLocationObject locationObject;
    // Protobuf
    private @Getter @Setter GeLocationObjectsHolder locationObjectsHolder;
    private @Getter @Setter GeShipStateResponse.Builder ssrBuilder;

    private @Getter GeLocationObjectPacket.Builder lopBuilder;

    public static GePlayerInfoHolder getByLopId(int id) {
        return thisById.get(id);
    }

    public void setLopBuilder(Builder lopBuilder) {
        this.lopBuilder = lopBuilder;
        thisById.put(lopBuilder.getObjectId(), this);
    }

    public void dispose() {
        if (lopBuilder != null) {
            thisById.remove(lopBuilder.getObjectId());
        }
    }
}
