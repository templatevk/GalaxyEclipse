package arch.galaxyeclipse.server.data.repository;

import arch.galaxyeclipse.server.data.model.*;
import org.junit.*;
import org.springframework.test.context.transaction.*;
import org.springframework.transaction.annotation.*;

import static junit.framework.Assert.*;

/**
 *
 */
@TransactionConfiguration(defaultRollback = true)
public class PlayersRepositoryTest extends AbstractRepositoryTest {
    public PlayersRepositoryTest() {
        super(IPlayersRepository.class);
    }

    @Override
    protected IPlayersRepository getRepository() {
        return (IPlayersRepository)super.getRepository();
    }

    @Test
    public void testFindTestPlayer() {
        IPlayersRepository repository = getRepository();

        assertNotNull(repository.findByUsernameAndPassword(
                TEST_PLAYER_USERNAME,
                TEST_PLAYER_PASSWORD_ENCRYPTED));
    }

    @Test
    @Transactional(isolation =  Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    public void testUpdateTestPlayer() {
        IPlayersRepository repository = getRepository();

        Players player = repository.findByUsernameAndPassword(
                TEST_PLAYER_USERNAME,
                TEST_PLAYER_PASSWORD_ENCRYPTED);
        player.setBanned(false);
        assertNotNull(repository.save(player));
    }
}