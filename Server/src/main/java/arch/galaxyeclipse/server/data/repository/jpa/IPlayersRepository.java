package arch.galaxyeclipse.server.data.repository.jpa;

import arch.galaxyeclipse.server.data.model.*;
import org.springframework.data.jpa.repository.*;

/**
 *
 */
public interface IPlayersRepository extends JpaRepository<Players, Integer> {
    Players findByUsernameAndPassword(String username, String password);
}
