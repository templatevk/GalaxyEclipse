package arch.galaxyeclipse.server;

import arch.galaxyeclipse.server.data.*;
import arch.galaxyeclipse.server.data.model.*;
import arch.galaxyeclipse.shared.context.*;
import org.hibernate.*;
import org.testng.annotations.*;

import java.util.*;

/**
 *
 */
public class EntityCriteriaTest extends AbstractTestNGServerTest {
    @Test
    public void criteriaTest() {
        List<Class<?>> entities = Arrays.asList(
                Bonus.class,
                BonusType.class,
                Engine.class,
                InventoryItem.class,
                Item.class,
                ItemType.class,
                LocationObjectBehaviorType.class,
                LocationObject.class,
                LocationObjectType.class,
                Location.class,
                Player.class,
                PlayerActivationHash.class,
                ShipConfigBonusSlot.class,
                ShipConfig.class,
                ShipConfigWeaponSlot.class,
                ShipState.class,
                ShipType.class,
                Weapon.class,
                WeaponType.class
        );
        for (final Class<?> entity : entities) {
            new DataWorker() {
                @Override
                protected void doWork(Session session) {
                    session.createCriteria(entity).list();
                }
            }.execute();
        }
    }
}
