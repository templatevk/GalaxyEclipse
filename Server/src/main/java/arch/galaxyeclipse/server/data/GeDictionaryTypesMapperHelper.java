package arch.galaxyeclipse.server.data;

import arch.galaxyeclipse.server.data.model.*;
import arch.galaxyeclipse.shared.common.GeLogUtils;
import arch.galaxyeclipse.shared.types.GeDictionaryTypesMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Slf4j
public class GeDictionaryTypesMapperHelper {
    private GeDictionaryTypesMapperHelper() {

    }

    private static Map<Integer, String> getBonusTypes() {
        Map<Integer, String> result = new HashMap<>();

        for (GeBonusType type : new GeCriteriaUnitOfWork<>(GeBonusType.class).execute()) {
            result.put(type.getBonusTypeId(), type.getBonusTypeName());
        }
        return result;
    }

    private static Map<Integer, String> getItemTypes() {
        Map<Integer, String> result = new HashMap<>();

        for (GeItemType type : new GeCriteriaUnitOfWork<>(GeItemType.class).execute()) {
            result.put(type.getItemTypeId(), type.getItemTypeName());
        }
        return result;
    }

    private static Map<Integer, String> getWeaponTypes() {
        Map<Integer, String> result = new HashMap<>();

        for (GeWeaponType type : new GeCriteriaUnitOfWork<>(GeWeaponType.class).execute()) {
            result.put(type.getWeaponTypeId(), type.getWeaponTypeName());
        }
        return result;
    }

    private static Map<Integer, String> getLocationObjectTypes() {
        Map<Integer, String> result = new HashMap<>();

        for (GeLocationObjectType type : new GeCriteriaUnitOfWork<>(
                GeLocationObjectType.class).execute()) {
            result.put(type.getLocationObjectTypeId(), type.getObjectTypeName());
        }
        return result;
    }

    private static Map<Integer, String> getLocationObjectBehaviorTypes() {
        Map<Integer, String> result = new HashMap<>();

        for (GeLocationObjectBehaviorType type : new GeCriteriaUnitOfWork<>(
                GeLocationObjectBehaviorType.class).execute()) {
            result.put(type.getLocationObjectBehaviorTypeId(), type.getObjectBehaviorTypeName());
        }
        return result;
    }

    public static void fillAll(GeDictionaryTypesMapper dictionaryTypesMapper) {
        if (GeDictionaryTypesMapperHelper.log.isDebugEnabled()) {
            GeDictionaryTypesMapperHelper.log.debug(GeLogUtils.getObjectInfo(GeDictionaryTypesMapper.class)
                    + " filling in dictionary types");
        }

        dictionaryTypesMapper.fillItemTypes(getItemTypes());
        dictionaryTypesMapper.fillBonusTypes(getBonusTypes());
        dictionaryTypesMapper.fillWeaponTypes(getWeaponTypes());
        dictionaryTypesMapper.fillLocationObjectTypes(getLocationObjectTypes());
        dictionaryTypesMapper.fillLocationObjectBehaviorTypes(getLocationObjectBehaviorTypes());
    }
}
