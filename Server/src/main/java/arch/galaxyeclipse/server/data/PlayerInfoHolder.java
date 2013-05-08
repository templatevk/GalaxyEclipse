package arch.galaxyeclipse.server.data;

import arch.galaxyeclipse.server.data.model.*;
import lombok.*;

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
    // Redis
    private byte[] locationObjectPacketHashKey;
    private byte[] locationObjectPacketSortedSetXKey;
    private byte[] locationObjectPacketSortedSetYKey;
    private byte[] locationObjectPacketBufSetXKey;
    private byte[] locationObjectPacketBufSetYKey;
}
