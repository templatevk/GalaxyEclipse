package arch.galaxyeclipse.server.data.repository;

import arch.galaxyeclipse.server.data.model.*;
import org.springframework.data.jpa.repository.*;

/**
 *
 */
public interface PlayersRepository extends JpaRepository<Players, Integer> {
    Players findByUsernameAndPassword(String username, String password);
}
