package arch.galaxyeclipse.server.data.util;

import arch.galaxyeclipse.server.AbstractTestNGServerTest;
import arch.galaxyeclipse.server.data.DictionaryTypesMapperHelper;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.types.*;
import org.fest.assertions.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 *
 */
public class DictionaryTypesMapperHelperTest extends AbstractTestNGServerTest {
    private DictionaryTypesMapper dictionaryTypesMapper;

    @BeforeClass
    public void initDependencies() {
        dictionaryTypesMapper = ContextHolder.getBean(
                DictionaryTypesMapper.class);
    }

    @Test(groups = "fast")
    public void testFilling() {
        DictionaryTypesMapperHelper.fillAll(dictionaryTypesMapper);

        Assertions.assertThat(dictionaryTypesMapper.getItemTypeById(1)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getWeaponTypeById(1)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getBonusTypeById(1)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getLocationObjectBehaviorTypeById(1)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getLocationObjectTypeById(1)).isNotNull();

        Assertions.assertThat(dictionaryTypesMapper.getIdByItemType(
                ItemTypesMapperType.BONUS)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByItemType(
                ItemTypesMapperType.ENGINE)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByItemType(
                ItemTypesMapperType.SALE)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByItemType(
                ItemTypesMapperType.WEAPON)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByItemType(
                ItemTypesMapperType.MONEY)).isNotNull();

        Assertions.assertThat(dictionaryTypesMapper.getIdByWeaponType(
                WeaponTypesMapperType.LASER)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByWeaponType(
                WeaponTypesMapperType.ROCKET)).isNotNull();

        Assertions.assertThat(dictionaryTypesMapper.getIdByBonusType(
                BonusTypesMapperType.ACCELERATION_MOVE)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByBonusType(
                BonusTypesMapperType.ACCELERATION_ROTATION)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByBonusType(
                BonusTypesMapperType.SPEED_MOVE)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByBonusType(
                BonusTypesMapperType.SPEED_ROTATION)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByBonusType(
                BonusTypesMapperType.ARMOR)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByBonusType(
                BonusTypesMapperType.ENERGY_REGEN)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByBonusType(
                BonusTypesMapperType.HP_REGEN)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByBonusType(
                BonusTypesMapperType.WEAPON_SPEED)).isNotNull();

        Assertions.assertThat(dictionaryTypesMapper.getIdByLocationObjectBehaviorType(
                LocationObjectBehaviorTypesMapperType.DYNAMIC)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByLocationObjectBehaviorType(
                LocationObjectBehaviorTypesMapperType.STATIC)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByLocationObjectBehaviorType(
                LocationObjectBehaviorTypesMapperType.DRAWABLE)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByLocationObjectBehaviorType(
                LocationObjectBehaviorTypesMapperType.IGNORED)).isNotNull();

        Assertions.assertThat(dictionaryTypesMapper.getIdByLocationObjectType(
                LocationObjectTypesMapperType.ASTEROID)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByLocationObjectType(
                LocationObjectTypesMapperType.FOG)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByLocationObjectType(
                LocationObjectTypesMapperType.STAR)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByLocationObjectType(
                LocationObjectTypesMapperType.PLAYER)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByLocationObjectType(
                LocationObjectTypesMapperType.ROCKET)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByLocationObjectType(
                LocationObjectTypesMapperType.STATION)).isNotNull();
    }
}
