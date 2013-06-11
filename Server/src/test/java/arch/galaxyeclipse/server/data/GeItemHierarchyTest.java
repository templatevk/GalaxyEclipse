package arch.galaxyeclipse.server.data;

import arch.galaxyeclipse.server.GeAbstractTestNGServerTest;
import arch.galaxyeclipse.server.data.model.GeWeapon;
import org.hibernate.Session;
import org.testng.annotations.Test;

/**
 *
 */
public class GeItemHierarchyTest extends GeAbstractTestNGServerTest {

    @Test(groups = "fast")
    public void criteriaTest() {
        new GeHibernateUnitOfWork() {
            @Override
            protected void doWork(Session session) {
                GeWeapon weapon = (GeWeapon) session.get(GeWeapon.class, 1);

            }
        }.execute();
    }
}
