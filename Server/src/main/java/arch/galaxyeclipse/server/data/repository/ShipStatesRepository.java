package arch.galaxyeclipse.server.data.repository;

import arch.galaxyeclipse.server.data.model.*;
import org.springframework.data.jpa.repository.*;

/**
 *
 */
public interface ShipStatesRepository extends JpaRepository<ShipStates, Integer> {
    ShipStates findByPlayerId(int playerId);
}
