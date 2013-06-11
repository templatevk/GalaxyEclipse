package arch.galaxyeclipse.server.data;

import arch.galaxyeclipse.server.GeAbstractTestNGServerTest;
import arch.galaxyeclipse.server.data.model.*;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class GeEntityCriteriaTest extends GeAbstractTestNGServerTest {

    @Test(groups = "fast")
    public void criteriaTest() {
        List<Class<?>> entities = Arrays.asList(
                GeBonus.class,
                GeBonusType.class,
                GeEngine.class,
                GeInventoryItem.class,
                GeItem.class,
                GeItemType.class,
                GeLocationObjectBehaviorType.class,
                GeLocationObject.class,
                GeLocationObjectType.class,
                GeLocation.class,
                GePlayer.class,
                GePlayerActivationHash.class,
                GeShipConfigBonusSlot.class,
                GeShipConfig.class,
                GeShipConfigWeaponSlot.class,
                GeShipState.class,
                GeShipType.class,
                GeWeapon.class,
                GeWeaponType.class
        );

        for (final Class<?> entity : entities) {
            new GeCriteriaUnitOfWork(entity).execute();
        }
    }
}
