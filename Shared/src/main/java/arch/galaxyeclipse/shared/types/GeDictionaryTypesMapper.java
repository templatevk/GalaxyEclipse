package arch.galaxyeclipse.shared.types;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps values from dictionary tables, name-id and id-name associations.
 */
@Slf4j
public class GeDictionaryTypesMapper {

    // Mapping primary keys of the dictionary tables to the enum values and vise versa
    private Map<Integer, GeLocationObjectTypesMapperType> idsObjectTypes;
    private Map<GeLocationObjectTypesMapperType, Integer> objectTypesIds;

    private Map<Integer, GeLocationObjectBehaviorTypesMapperType> idsObjectBehaviorTypes;
    private Map<GeLocationObjectBehaviorTypesMapperType, Integer> objectBehaviorTypesIds;

    private Map<Integer, GeWeaponTypesMapperType> idsWeaponTypes;
    private Map<GeWeaponTypesMapperType, Integer> weaponTypesIds;

    private Map<Integer, GeBonusTypesMapperType> idsBonusTypes;
    private Map<GeBonusTypesMapperType, Integer> bonusTypesIds;

    private Map<Integer, GeItemTypesMapperType> idsItemTypes;
    private Map<GeItemTypesMapperType, Integer> itemTypesIds;

    public GeDictionaryTypesMapper() {
        idsObjectTypes = new HashMap<Integer, GeLocationObjectTypesMapperType>();
        idsObjectBehaviorTypes = new HashMap<Integer, GeLocationObjectBehaviorTypesMapperType>();
        idsWeaponTypes = new HashMap<Integer, GeWeaponTypesMapperType>();
        idsBonusTypes = new HashMap<Integer, GeBonusTypesMapperType>();
        idsItemTypes = new HashMap<Integer, GeItemTypesMapperType>();

        objectTypesIds = new HashMap<GeLocationObjectTypesMapperType, Integer>();
        objectBehaviorTypesIds = new HashMap<GeLocationObjectBehaviorTypesMapperType, Integer>();
        weaponTypesIds = new HashMap<GeWeaponTypesMapperType, Integer>();
        bonusTypesIds = new HashMap<GeBonusTypesMapperType, Integer>();
        itemTypesIds = new HashMap<GeItemTypesMapperType, Integer>();
    }

    public GeLocationObjectTypesMapperType getLocationObjectTypeById(int id) {
        return idsObjectTypes.get(id);
    }

    public GeLocationObjectBehaviorTypesMapperType getLocationObjectBehaviorTypeById(int id) {
        return idsObjectBehaviorTypes.get(id);
    }

    public GeWeaponTypesMapperType getWeaponTypeById(int id) {
        return idsWeaponTypes.get(id);
    }

    public GeBonusTypesMapperType getBonusTypeById(int id) {
        return idsBonusTypes.get(id);
    }

    public GeItemTypesMapperType getItemTypeById(int id) {
        return idsItemTypes.get(id);
    }

    public Integer getIdByLocationObjectType(GeLocationObjectTypesMapperType type) {
        return objectTypesIds.get(type);
    }

    public Integer getIdByLocationObjectBehaviorType(GeLocationObjectBehaviorTypesMapperType type) {
        return objectBehaviorTypesIds.get(type);
    }

    public Integer getIdByWeaponType(GeWeaponTypesMapperType type) {
        return weaponTypesIds.get(type);
    }

    public Integer getIdByBonusType(GeBonusTypesMapperType type) {
        return bonusTypesIds.get(type);
    }

    public Integer getIdByItemType(GeItemTypesMapperType type) {
        return itemTypesIds.get(type);
    }

    public void fillLocationObjectTypes(Map<Integer, String> types) {
        idsObjectTypes.clear();
        try {
            for (Map.Entry<Integer, String> entry : types.entrySet()) {
                idsObjectTypes.put(entry.getKey(),
                        GeLocationObjectTypesMapperType.valueOf(entry.getValue().toUpperCase()));
                objectTypesIds.put(GeLocationObjectTypesMapperType.valueOf(
                        entry.getValue().toUpperCase()), entry.getKey());
            }

            if (GeDictionaryTypesMapper.log.isInfoEnabled()) {
                GeDictionaryTypesMapper.log.info("Location object types mapped");
            }
        } catch (Exception e) {
            GeDictionaryTypesMapper.log.error("Error mapping location object types", e);
        }
    }

    public void fillLocationObjectBehaviorTypes(Map<Integer, String> types) {
        idsObjectBehaviorTypes.clear();
        try {
            for (Map.Entry<Integer, String> entry : types.entrySet()) {
                idsObjectBehaviorTypes.put(entry.getKey(),
                        GeLocationObjectBehaviorTypesMapperType.valueOf(entry.getValue().toUpperCase()));
                objectBehaviorTypesIds.put(GeLocationObjectBehaviorTypesMapperType.valueOf(
                        entry.getValue().toUpperCase()), entry.getKey());
            }

            if (GeDictionaryTypesMapper.log.isInfoEnabled()) {
                GeDictionaryTypesMapper.log.info("Location object behavior types mapped");
            }
        } catch (Exception e) {
            GeDictionaryTypesMapper.log.error("Error mapping location behavior object types", e);
        }
    }

    public void fillWeaponTypes(Map<Integer, String> types) {
        idsWeaponTypes.clear();
        try {
            for (Map.Entry<Integer, String> entry : types.entrySet()) {
                idsWeaponTypes.put(entry.getKey(),
                        GeWeaponTypesMapperType.valueOf(entry.getValue().toUpperCase()));
                weaponTypesIds.put(GeWeaponTypesMapperType.valueOf(entry.getValue().toUpperCase()),
                        entry.getKey());
            }

            if (GeDictionaryTypesMapper.log.isInfoEnabled()) {
                GeDictionaryTypesMapper.log.info("Weapon types mapped");
            }
        } catch (Exception e) {
            GeDictionaryTypesMapper.log.error("Error mapping weapon types", e);
        }
    }

    public void fillItemTypes(Map<Integer, String> types) {
        idsItemTypes.clear();
        try {
            for (Map.Entry<Integer, String> entry : types.entrySet()) {
                idsItemTypes.put(entry.getKey(),
                        GeItemTypesMapperType.valueOf(entry.getValue().toUpperCase()));
                itemTypesIds.put(GeItemTypesMapperType.valueOf(entry.getValue().toUpperCase()),
                        entry.getKey());
            }
            if (GeDictionaryTypesMapper.log.isInfoEnabled()) {
                GeDictionaryTypesMapper.log.info("Item types mapped");
            }
        } catch (Exception e) {
            GeDictionaryTypesMapper.log.error("Error mapping item types", e);
        }
    }

    public void fillBonusTypes(Map<Integer, String> types) {
        idsBonusTypes.clear();
        try {
            for (Map.Entry<Integer, String> entry : types.entrySet()) {
                idsBonusTypes.put(entry.getKey(),
                        GeBonusTypesMapperType.valueOf(entry.getValue().toUpperCase()));
                bonusTypesIds.put(GeBonusTypesMapperType.valueOf(entry.getValue().toUpperCase()),
                        entry.getKey());
            }

            if (GeDictionaryTypesMapper.log.isInfoEnabled()) {
                GeDictionaryTypesMapper.log.info("Bonus types mapped");
            }
        } catch (Exception e) {
            GeDictionaryTypesMapper.log.error("Error mapping bonus types", e);
        }
    }
}
