package arch.galaxyeclipse.server.data.util;

import arch.galaxyeclipse.server.data.model.*;
import arch.galaxyeclipse.server.data.repository.jpa.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.types.*;
import arch.galaxyeclipse.shared.util.*;
import lombok.extern.slf4j.*;

import java.util.*;

/**
 *
 */
@Slf4j
class DictionaryTypesMapperHelper implements IDictionaryTypesMapperHelper {
    private IBonusTypesRepository bonusTypesRepository;
    private IItemTypesRepository itemTypesRepository;
    private IWeaponTypesRepository weaponTypesRepository;
    private ILocationObjectTypesRepository locationObjectTypesRepository;
    private ILocationObjectBehaviorTypesRepository locationObjectBehaviorTypesRepository;

    public DictionaryTypesMapperHelper() {
        bonusTypesRepository = ContextHolder.INSTANCE.getBean(
                IBonusTypesRepository.class);
        itemTypesRepository = ContextHolder.INSTANCE.getBean(
                IItemTypesRepository.class);
        weaponTypesRepository = ContextHolder.INSTANCE.getBean(
                IWeaponTypesRepository.class);
        locationObjectBehaviorTypesRepository = ContextHolder.INSTANCE.getBean(
                ILocationObjectBehaviorTypesRepository.class);
        locationObjectTypesRepository = ContextHolder.INSTANCE.getBean(
                ILocationObjectTypesRepository.class);
    }

    @Override
    public Map<Integer, String> getBonusTypes() {
        Map<Integer, String> result = new HashMap<Integer, String>();
        for (BonusTypes type : bonusTypesRepository.findAll()) {
            result.put(type.getBonusTypeId(), type.getBonusTypeName());
        }
        return result;
    }

    @Override
    public Map<Integer, String> getItemTypes() {
        Map<Integer, String> result = new HashMap<Integer, String>();
        for (ItemTypes type : itemTypesRepository.findAll()) {
            result.put(type.getItemTypeId(), type.getItemTypeName());
        }
        return result;
    }

    @Override
    public Map<Integer, String> getWeaponTypes() {
        Map<Integer, String> result = new HashMap<Integer, String>();
        for (WeaponTypes type : weaponTypesRepository.findAll()) {
            result.put(type.getWeaponTypeId(), type.getWeaponTypeName());
        }
        return result;
    }

    @Override
    public Map<Integer, String> getLocationObjectTypes() {
        Map<Integer, String> result = new HashMap<Integer, String>();
        for (LocationObjectTypes type : locationObjectTypesRepository.findAll()) {
            result.put(type.getLocationObjectTypeId(), type.getObjectTypeName());
        }
        return result;
    }

    @Override
    public Map<Integer, String> getLocationObjectBehaviorTypes() {
        Map<Integer, String> result = new HashMap<Integer, String>();
        for (LocationObjectBehaviorTypes type : locationObjectBehaviorTypesRepository.findAll()) {
            result.put(type.getLocationObjectBehaviorTypeId(), type.getObjectBehaviorTypeName());
        }
        return result;
    }

    @Override
    public void fillAll(DictionaryTypesMapper dictionaryTypesMapper) {
        if (log.isDebugEnabled()) {
            log.debug(LogUtils.getObjectInfo(this) + " filling in dictionary types");
        }

        dictionaryTypesMapper.fillItemTypes(getItemTypes());
        dictionaryTypesMapper.fillBonusTypes(getBonusTypes());
        dictionaryTypesMapper.fillWeaponTypes(getWeaponTypes());
        dictionaryTypesMapper.fillLocationObjectTypes(getLocationObjectTypes());
        dictionaryTypesMapper.fillLocationObjectBehaviorTypes(getLocationObjectBehaviorTypes());
    }
}
