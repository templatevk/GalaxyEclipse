package arch.galaxyeclipse.server.data.repository.jpa;

import arch.galaxyeclipse.server.data.repository.*;
import org.junit.*;

import static junit.framework.Assert.*;

/**
 *
 */
public class ShipConfigsRepositoryTest extends AbstractRepositoryTest {
    public ShipConfigsRepositoryTest() {
        super(IShipConfigsRepository.class);
    }

    @Test
    public void testFindByPlayerId() {
        IShipConfigsRepository repository = (IShipConfigsRepository)getRepository();

        assertNotNull(repository.findByPlayerId(1));
    }
}
