package arch.galaxyeclipse.server.data.repository;

import arch.galaxyeclipse.server.data.model.*;
import org.springframework.data.jpa.repository.*;

/**
 *
 */
public interface ShipConfigsRepository extends JpaRepository<ShipConfigs, Integer> {
    ShipConfigs findByPlayerId(int playerId);
}
