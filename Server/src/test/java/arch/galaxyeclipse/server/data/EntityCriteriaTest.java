package arch.galaxyeclipse.server.data;

import arch.galaxyeclipse.server.AbstractTestNGServerTest;
import arch.galaxyeclipse.server.data.model.*;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class EntityCriteriaTest extends AbstractTestNGServerTest {
    @Test(groups = "fast")
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
            new CriteriaUnitOfWork(entity).execute();
        }
    }
}
