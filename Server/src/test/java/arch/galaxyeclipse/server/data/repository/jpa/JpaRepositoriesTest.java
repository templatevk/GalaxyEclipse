package arch.galaxyeclipse.server.data.repository.jpa;

import arch.galaxyeclipse.server.*;
import arch.galaxyeclipse.server.data.repository.*;
import org.junit.*;

/**
 *
 */
@Ignore
public class JpaRepositoriesTest extends AbstractServerTest {
    @Test
    public void testBonusTypesRepository() {
        AbstractRepositoryTest.testRepositories(IBonusTypesRepository.class);
    }

    @Test
    public void testItemTypesRepository() {
        AbstractRepositoryTest.testRepositories(IItemTypesRepository.class);
    }

    @Test
    public void testLocatinObjectBehaviorTypesRepository() {
        AbstractRepositoryTest.testRepositories(ILocationObjectBehaviorTypesRepository.class);
    }

    @Test
    public void testLocationObjectsRepository() {
        AbstractRepositoryTest.testRepositories(ILocationObjectsRepository.class);
    }

    @Test
    public void testLocationObjectTypesRepository() {
        AbstractRepositoryTest.testRepositories(ILocationObjectTypesRepository.class);
    }

    @Test
    public void testPlayersRepository() {
        AbstractRepositoryTest.testRepositories(IPlayersRepository.class);
    }

    @Test
    public void testShipConfigsRepository() {
        AbstractRepositoryTest.testRepositories(IShipConfigsRepository.class);
    }

    @Test
    public void testShipStatesRepository() {
        AbstractRepositoryTest.testRepositories(IShipStatesRepository.class);
    }

    @Test
    public void testWeaponTypesRepository() {
        AbstractRepositoryTest.testRepositories(IWeaponTypesRepository.class);
    }
}
