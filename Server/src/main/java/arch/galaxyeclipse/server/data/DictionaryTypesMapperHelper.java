package arch.galaxyeclipse.server.data;

import arch.galaxyeclipse.server.data.model.*;
import arch.galaxyeclipse.shared.types.DictionaryTypesMapper;
import arch.galaxyeclipse.shared.util.LogUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Slf4j
public class DictionaryTypesMapperHelper {
    private DictionaryTypesMapperHelper() {

    }

    private static Map<Integer, String> getBonusTypes() {
        Map<Integer, String> result = new HashMap<>();

        for (BonusType type : new CriteriaUnitOfWork<>(BonusType.class).execute()) {
            result.put(type.getBonusTypeId(), type.getBonusTypeName());
        }
        return result;
    }

    private static Map<Integer, String> getItemTypes() {
        Map<Integer, String> result = new HashMap<>();

        for (ItemType type : new CriteriaUnitOfWork<>(ItemType.class).execute()) {
            result.put(type.getItemTypeId(), type.getItemTypeName());
        }
        return result;
    }

    private static Map<Integer, String> getWeaponTypes() {
        Map<Integer, String> result = new HashMap<>();

        for (WeaponType type : new CriteriaUnitOfWork<>(WeaponType.class).execute()) {
            result.put(type.getWeaponTypeId(), type.getWeaponTypeName());
        }
        return result;
    }

    private static Map<Integer, String> getLocationObjectTypes() {
        Map<Integer, String> result = new HashMap<>();

        for (LocationObjectType type : new CriteriaUnitOfWork<>(
                LocationObjectType.class).execute()) {
            result.put(type.getLocationObjectTypeId(), type.getObjectTypeName());
        }
        return result;
    }

    private static Map<Integer, String> getLocationObjectBehaviorTypes() {
        Map<Integer, String> result = new HashMap<>();

        for (LocationObjectBehaviorType type : new CriteriaUnitOfWork<>(
                LocationObjectBehaviorType.class).execute()) {
            result.put(type.getLocationObjectBehaviorTypeId(), type.getObjectBehaviorTypeName());
        }
        return result;
    }

    public static void fillAll(DictionaryTypesMapper dictionaryTypesMapper) {
        if (DictionaryTypesMapperHelper.log.isDebugEnabled()) {
            DictionaryTypesMapperHelper.log.debug(LogUtils.getObjectInfo(DictionaryTypesMapper.class)
                    + " filling in dictionary types");
        }

        dictionaryTypesMapper.fillItemTypes(getItemTypes());
        dictionaryTypesMapper.fillBonusTypes(getBonusTypes());
        dictionaryTypesMapper.fillWeaponTypes(getWeaponTypes());
        dictionaryTypesMapper.fillLocationObjectTypes(getLocationObjectTypes());
        dictionaryTypesMapper.fillLocationObjectBehaviorTypes(getLocationObjectBehaviorTypes());
    }
}
