package arch.galaxyeclipse.server.data;

import arch.galaxyeclipse.server.data.model.*;
import arch.galaxyeclipse.shared.types.*;
import arch.galaxyeclipse.shared.util.*;
import lombok.extern.slf4j.*;

import java.util.*;

/**
 *
 */
@Slf4j
class DictionaryTypesMapperHelper implements IDictionaryTypesMapperHelper {
    public DictionaryTypesMapperHelper() {

    }

    @Override
    public Map<Integer, String> getBonusTypes() {
        Map<Integer, String> result = new HashMap<>();

        for (BonusType type : new CriteriaUnitOfWork<>(BonusType.class).execute()) {
            result.put(type.getBonusTypeId(), type.getBonusTypeName());
        }
        return result;
    }

    @Override
    public Map<Integer, String> getItemTypes() {
        Map<Integer, String> result = new HashMap<>();

        for (ItemType type : new CriteriaUnitOfWork<>(ItemType.class).execute()) {
            result.put(type.getItemTypeId(), type.getItemTypeName());
        }
        return result;
    }

    @Override
    public Map<Integer, String> getWeaponTypes() {
        Map<Integer, String> result = new HashMap<>();

        for (WeaponType type : new CriteriaUnitOfWork<>(WeaponType.class).execute()) {
            result.put(type.getWeaponTypeId(), type.getWeaponTypeName());
        }
        return result;
    }

    @Override
    public Map<Integer, String> getLocationObjectTypes() {
        Map<Integer, String> result = new HashMap<>();

        for (LocationObjectType type : new CriteriaUnitOfWork<>(
                LocationObjectType.class).execute()) {
            result.put(type.getLocationObjectTypeId(), type.getObjectTypeName());
        }
        return result;
    }

    @Override
    public Map<Integer, String> getLocationObjectBehaviorTypes() {
        Map<Integer, String> result = new HashMap<>();

        for (LocationObjectBehaviorType type : new CriteriaUnitOfWork<>(
                LocationObjectBehaviorType.class).execute()) {
            result.put(type.getLocationObjectBehaviorTypeId(), type.getObjectBehaviorTypeName());
        }
        return result;
    }

    @Override
    public void fillAll(DictionaryTypesMapper dictionaryTypesMapper) {
        if (DictionaryTypesMapperHelper.log.isDebugEnabled()) {
            DictionaryTypesMapperHelper.log.debug(LogUtils.getObjectInfo(this) + " filling in dictionary types");
        }

        dictionaryTypesMapper.fillItemTypes(getItemTypes());
        dictionaryTypesMapper.fillBonusTypes(getBonusTypes());
        dictionaryTypesMapper.fillWeaponTypes(getWeaponTypes());
        dictionaryTypesMapper.fillLocationObjectTypes(getLocationObjectTypes());
        dictionaryTypesMapper.fillLocationObjectBehaviorTypes(getLocationObjectBehaviorTypes());
    }
}
