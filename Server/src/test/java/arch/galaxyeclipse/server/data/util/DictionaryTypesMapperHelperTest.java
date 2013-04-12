package arch.galaxyeclipse.server.data.util;

import arch.galaxyeclipse.server.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.types.*;
import org.junit.*;

import static junit.framework.Assert.*;

/**
 *
 */
public class DictionaryTypesMapperHelperTest extends AbstractJUnitServerTest {
    private IDictionaryTypesMapperHelper dictionaryTypesMapperHelper;
    private DictionaryTypesMapper dictionaryTypesMapper;

    @Before
    public void initDependencies() {
        dictionaryTypesMapper = ContextHolder.INSTANCE.getBean(
                DictionaryTypesMapper.class);
        dictionaryTypesMapperHelper = ContextHolder.INSTANCE.getBean(
                IDictionaryTypesMapperHelper.class);
    }

    @Test
    public void testFilling() {
        dictionaryTypesMapperHelper.fillAll(dictionaryTypesMapper);

        assertNotNull(dictionaryTypesMapper.getItemTypeById(1));
        assertNotNull(dictionaryTypesMapper.getWeaponTypeById(1));
        assertNotNull(dictionaryTypesMapper.getBonusTypeById(1));
        assertNotNull(dictionaryTypesMapper.getLocationObjectBehaviorTypeById(1));
        assertNotNull(dictionaryTypesMapper.getLocationObjectTypeById(1));

        assertNotNull(dictionaryTypesMapper.getIdByItemType(ItemTypesMapperType.BONUS));
        assertNotNull(dictionaryTypesMapper.getIdByItemType(ItemTypesMapperType.ENGINE));
        assertNotNull(dictionaryTypesMapper.getIdByItemType(ItemTypesMapperType.SALE));
        assertNotNull(dictionaryTypesMapper.getIdByItemType(ItemTypesMapperType.WEAPON));

        assertNotNull(dictionaryTypesMapper.getIdByWeaponType(
                WeaponTypesMapperType.LASER));
        assertNotNull(dictionaryTypesMapper.getIdByWeaponType(
                WeaponTypesMapperType.ROCKET));

        assertNotNull(dictionaryTypesMapper.getIdByBonusType(
                BonusTypesMapperType.ACCELERATION_MOVE));
        assertNotNull(dictionaryTypesMapper.getIdByBonusType(
                BonusTypesMapperType.ACCELERATION_ROTATION));
        assertNotNull(dictionaryTypesMapper.getIdByBonusType(
                BonusTypesMapperType.SPEED_MOVE));
        assertNotNull(dictionaryTypesMapper.getIdByBonusType(
                BonusTypesMapperType.SPEED_ROTATION));
        assertNotNull(dictionaryTypesMapper.getIdByBonusType(
                BonusTypesMapperType.ARMOR));
        assertNotNull(dictionaryTypesMapper.getIdByBonusType(
                BonusTypesMapperType.ENERGY_REGEN));
        assertNotNull(dictionaryTypesMapper.getIdByBonusType(
                BonusTypesMapperType.HP_REGEN));
        assertNotNull(dictionaryTypesMapper.getIdByBonusType(
                BonusTypesMapperType.WEAPON_SPEED));

        assertNotNull(dictionaryTypesMapper.getIdByLocationObjectBehaviorType(
                LocationObjectBehaviorTypesMapperType.DYNAMIC));
        assertNotNull(dictionaryTypesMapper.getIdByLocationObjectBehaviorType(
                LocationObjectBehaviorTypesMapperType.STATIC));

        assertNotNull(dictionaryTypesMapper.getIdByLocationObjectType(
                LocationObjectTypesMapperType.ASTEROID));
        assertNotNull(dictionaryTypesMapper.getIdByLocationObjectType(
                LocationObjectTypesMapperType.PLAYER_FLIGHT));
        assertNotNull(dictionaryTypesMapper.getIdByLocationObjectType(
                LocationObjectTypesMapperType.PLAYER_STATION));
        assertNotNull(dictionaryTypesMapper.getIdByLocationObjectType(
                LocationObjectTypesMapperType.ROCKET));
        assertNotNull(dictionaryTypesMapper.getIdByLocationObjectType(
                LocationObjectTypesMapperType.STATION));
    }
}
