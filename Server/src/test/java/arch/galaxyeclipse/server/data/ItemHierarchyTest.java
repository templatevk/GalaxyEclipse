package arch.galaxyeclipse.server.data;

import arch.galaxyeclipse.server.*;
import arch.galaxyeclipse.server.data.*;
import arch.galaxyeclipse.server.data.model.*;
import org.hibernate.*;
import org.testng.annotations.*;

import java.util.*;

/**
 *
 */
public class ItemHierarchyTest extends AbstractTestNGServerTest {
    @Test(groups = "fast")
    public void criteriaTest() {
        new UnitOfWork() {
            @Override
            protected void doWork(Session session) {
                Weapon weapon = (Weapon)session.get(Weapon.class, 1);

            }
        }.execute();
    }
}
