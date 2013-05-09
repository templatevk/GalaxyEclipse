package arch.galaxyeclipse.server.data;

import arch.galaxyeclipse.server.AbstractTestNGServerTest;
import arch.galaxyeclipse.server.data.model.Weapon;
import org.hibernate.Session;
import org.testng.annotations.Test;

/**
 *
 */
public class ItemHierarchyTest extends AbstractTestNGServerTest {
    @Test(groups = "fast")
    public void criteriaTest() {
        new HibernateUnitOfWork() {
            @Override
            protected void doWork(Session session) {
                Weapon weapon = (Weapon)session.get(Weapon.class, 1);

            }
        }.execute();
    }
}
