package arch.galaxyeclipse.server.network.handler;

import arch.galaxyeclipse.server.data.model.*;
import lombok.*;

/**
 *
 */
@Data
public class PlayerInfoHolder {
    private Player player;
    private ShipState shipState;
    private ShipConfig shipConfig;
    private LocationObject locationObject;
}
