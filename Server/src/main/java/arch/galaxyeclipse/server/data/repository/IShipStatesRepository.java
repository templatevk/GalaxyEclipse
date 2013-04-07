package arch.galaxyeclipse.server.data.repository;

import arch.galaxyeclipse.server.data.model.*;
import org.springframework.data.jpa.repository.*;

/**
 *
 */
public interface IShipStatesRepository extends JpaRepository<ShipStates, Integer> {
    ShipStates findByPlayerId(int playerId);
}