package arch.galaxyeclipse.server.data.repository;

import org.junit.*;

import static junit.framework.Assert.*;

/**
 *
 */
public class ShipStatesRepositoryTest extends AbstractRepositoryTest {
    public ShipStatesRepositoryTest() {
        super(IShipStatesRepository.class);
    }

    @Test
    public void testFindByPlayerId() {
        IShipStatesRepository repository = (IShipStatesRepository)getRepository();

        assertNotNull(repository.findByPlayerId(1));
    }
}
