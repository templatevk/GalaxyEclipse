package arch.galaxyeclipse.server.data.repository;

import arch.galaxyeclipse.server.*;
import arch.galaxyeclipse.shared.inject.*;
import org.junit.*;
import org.springframework.data.jpa.repository.*;

import static org.junit.Assert.assertNotNull;

/**
 *
 */
public abstract class AbstractRepositoryTest extends AbstractServerTest {
    private JpaRepository<?, ?> repository;
    private Class<? extends JpaRepository<?, ?>> clazz;

    public AbstractRepositoryTest(Class<? extends JpaRepository<?, ?>> clazz) {
        this.clazz = clazz;
    }

    protected JpaRepository<?, ?> getRepository() {
        return repository;
    }

    @Before
    public void initDependencies() {
        repository = SpringContextHolder.CONTEXT.getBean(clazz);
    }

    @Test
    public void testSelect() {
        assertNotNull(repository.findAll());
    }
}
