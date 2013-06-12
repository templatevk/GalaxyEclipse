package arch.galaxyeclipse.server.data.util;

import arch.galaxyeclipse.server.GeAbstractTestNGServerTest;
import arch.galaxyeclipse.server.data.GeDictionaryTypesMapperHelper;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import arch.galaxyeclipse.shared.types.*;
import org.fest.assertions.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 *
 */
public class GeDictionaryTypesMapperHelperTest extends GeAbstractTestNGServerTest {

    private GeDictionaryTypesMapper dictionaryTypesMapper;

    @BeforeClass
    public void initDependencies() {
        dictionaryTypesMapper = GeContextHolder.getBean(
                GeDictionaryTypesMapper.class);
    }

    @Test(groups = "fast")
    public void testFilling() {
        GeDictionaryTypesMapperHelper.fillAll(dictionaryTypesMapper);

        Assertions.assertThat(dictionaryTypesMapper.getItemTypeById(1)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getWeaponTypeById(1)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getBonusTypeById(1)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getLocationObjectBehaviorTypeById(1)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getLocationObjectTypeById(1)).isNotNull();

        Assertions.assertThat(dictionaryTypesMapper.getIdByItemType(
                GeItemTypesMapperType.BONUS)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByItemType(
                GeItemTypesMapperType.ENGINE)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByItemType(
                GeItemTypesMapperType.SALE)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByItemType(
                GeItemTypesMapperType.WEAPON)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByItemType(
                GeItemTypesMapperType.MONEY)).isNotNull();

        Assertions.assertThat(dictionaryTypesMapper.getIdByWeaponType(
                GeWeaponTypesMapperType.LASER)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByWeaponType(
                GeWeaponTypesMapperType.ROCKET)).isNotNull();

        Assertions.assertThat(dictionaryTypesMapper.getIdByBonusType(
                GeBonusTypesMapperType.ACCELERATION_MOVE)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByBonusType(
                GeBonusTypesMapperType.ACCELERATION_ROTATION)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByBonusType(
                GeBonusTypesMapperType.SPEED_MOVE)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByBonusType(
                GeBonusTypesMapperType.SPEED_ROTATION)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByBonusType(
                GeBonusTypesMapperType.ARMOR)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByBonusType(
                GeBonusTypesMapperType.ENERGY_REGEN)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByBonusType(
                GeBonusTypesMapperType.HP_REGEN)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByBonusType(
                GeBonusTypesMapperType.WEAPON_SPEED)).isNotNull();

        Assertions.assertThat(dictionaryTypesMapper.getIdByLocationObjectBehaviorType(
                GeLocationObjectBehaviorTypesMapperType.DYNAMIC)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByLocationObjectBehaviorType(
                GeLocationObjectBehaviorTypesMapperType.STATIC)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByLocationObjectBehaviorType(
                GeLocationObjectBehaviorTypesMapperType.DRAWABLE)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByLocationObjectBehaviorType(
                GeLocationObjectBehaviorTypesMapperType.IGNORED)).isNotNull();

        Assertions.assertThat(dictionaryTypesMapper.getIdByLocationObjectType(
                GeLocationObjectTypesMapperType.ASTEROID)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByLocationObjectType(
                GeLocationObjectTypesMapperType.FOG)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByLocationObjectType(
                GeLocationObjectTypesMapperType.STAR)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByLocationObjectType(
                GeLocationObjectTypesMapperType.PLAYER)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByLocationObjectType(
                GeLocationObjectTypesMapperType.BULLET)).isNotNull();
        Assertions.assertThat(dictionaryTypesMapper.getIdByLocationObjectType(
                GeLocationObjectTypesMapperType.STATION)).isNotNull();
    }
}
