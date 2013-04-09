package arch.galaxyeclipse.server.data.repository;

import arch.galaxyeclipse.server.*;
import arch.galaxyeclipse.shared.context.*;
import org.junit.*;
import org.springframework.data.repository.*;

import static org.junit.Assert.*;

/**
 *
 */
public abstract class AbstractRepositoryTest extends AbstractServerTest {
    private CrudRepository<?, ?> repository;
    private Class<? extends CrudRepository<?, ?>> clazz;

    public AbstractRepositoryTest(Class<? extends CrudRepository<?, ?>> clazz) {
        this.clazz = clazz;
    }

    public static void testRepositories(Class<? extends CrudRepository<?, ?>>... classes) {
        for (Class<? extends CrudRepository<?, ?>> clazz : classes) {
            assertNotNull(ContextHolder.INSTANCE.getBean(clazz).findAll());
        }
    }

    protected CrudRepository<?, ?> getRepository() {
        return repository;
    }

    @Before
    public void initDependencies() {
        repository = ContextHolder.INSTANCE.getBean(clazz);
    }

    @Test
    public void testSelect() {
        assertNotNull(repository.findAll());
    }
}
