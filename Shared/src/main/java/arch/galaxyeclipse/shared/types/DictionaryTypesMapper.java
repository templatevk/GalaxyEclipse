package arch.galaxyeclipse.shared.types;

import lombok.extern.slf4j.*;

import java.util.*;

/**
 * Maps values from dictionary tables, name-id and id-name associations. 
 */
@Slf4j
public class DictionaryTypesMapper {
	// Mapping primary keys of the dictionary tables to the enum values and vise versa
	private Map<Integer, LocationObjectTypesMapperType> idsObjectTypes;
    private Map<LocationObjectTypesMapperType, Integer> objectTypesIds;

    private Map<Integer, LocationObjectBehaviorTypesMapperType> idsObjectBehaviorTypes;
    private Map<LocationObjectBehaviorTypesMapperType, Integer> objectBehaviorTypesIds;

    private Map<Integer, WeaponTypesMapperType> idsWeaponTypes;
    private Map<WeaponTypesMapperType, Integer> weaponTypesIds;

    private Map<Integer, BonusTypesMapperType> idsBonusTypes;
    private Map<BonusTypesMapperType, Integer> bonusTypesIds;

    private Map<Integer, ItemTypesMapperType> idsItemTypes;
	private Map<ItemTypesMapperType, Integer> itemTypesIds;
	
	public DictionaryTypesMapper() {
		idsObjectTypes = new HashMap<Integer, LocationObjectTypesMapperType>();
		idsObjectBehaviorTypes = new HashMap<Integer, LocationObjectBehaviorTypesMapperType>();
		idsWeaponTypes = new HashMap<Integer, WeaponTypesMapperType>();
		idsBonusTypes = new HashMap<Integer, BonusTypesMapperType>();
		idsItemTypes = new HashMap<Integer, ItemTypesMapperType>();

        objectTypesIds = new HashMap<LocationObjectTypesMapperType, Integer>();
        objectBehaviorTypesIds = new HashMap<LocationObjectBehaviorTypesMapperType, Integer>();
        weaponTypesIds = new HashMap<WeaponTypesMapperType, Integer>();
        bonusTypesIds = new HashMap<BonusTypesMapperType, Integer>();
        itemTypesIds = new HashMap<ItemTypesMapperType, Integer>();
	}
	
	public LocationObjectTypesMapperType getLocationObjectTypeById(int id) {
		return idsObjectTypes.get(id);
	}

    public LocationObjectBehaviorTypesMapperType getLocationObjectBehaviorTypeById(int id) {
        return idsObjectBehaviorTypes.get(id);
    }
	
	public WeaponTypesMapperType getWeaponTypeById(int id) {
		return idsWeaponTypes.get(id);
	}
	
	public BonusTypesMapperType getBonusTypeById(int id) {
		return idsBonusTypes.get(id);
	}
	
	public ItemTypesMapperType getItemTypeById(int id) {
		return idsItemTypes.get(id);
	}
	
	public Integer getIdByLocationObjectType(LocationObjectTypesMapperType type) {
		return objectTypesIds.get(type);
	}
	
	public Integer getIdByLocationObjectBehaviorType(LocationObjectBehaviorTypesMapperType type) {
		return objectBehaviorTypesIds.get(type);
	}
	
	public Integer getIdByWeaponType(WeaponTypesMapperType type) {
		return weaponTypesIds.get(type);
	}
	
	public Integer getIdByBonusType(BonusTypesMapperType type) {
		return bonusTypesIds.get(type);
	}
	
	public Integer getIdByItemType(ItemTypesMapperType type) {
		return itemTypesIds.get(type);
	}
	
	public void fillLocationObjectTypes(Map<Integer, String> types) {
		idsObjectTypes.clear();
		try {
			for (Map.Entry<Integer, String> entry : types.entrySet()) {
				idsObjectTypes.put(entry.getKey(),
                        LocationObjectTypesMapperType.valueOf(entry.getValue().toUpperCase()));
				objectTypesIds.put(LocationObjectTypesMapperType.valueOf(
                        entry.getValue().toUpperCase()), entry.getKey());
			}

            if (log.isInfoEnabled()) {
			    log.info("Location object types mapped");
            }
		} catch (Exception e) {
			log.error("Error mapping location object types", e);
		}
	}
	
	public void fillLocationObjectBehaviorTypes(Map<Integer, String> types) {
		idsObjectBehaviorTypes.clear();
		try {
			for (Map.Entry<Integer, String> entry : types.entrySet()) {
				idsObjectBehaviorTypes.put(entry.getKey(),
                        LocationObjectBehaviorTypesMapperType.valueOf(entry.getValue().toUpperCase()));
				objectBehaviorTypesIds.put(LocationObjectBehaviorTypesMapperType.valueOf(
                        entry.getValue().toUpperCase()), entry.getKey());
			}

            if (log.isInfoEnabled()) {
			    log.info("Location object behavior types mapped");
            }
		} catch (Exception e) {
			log.error("Error mapping location behavior object types", e);
		}
	}
	
	public void fillWeaponTypes(Map<Integer, String> types) {
		idsWeaponTypes.clear();
		try {
			for (Map.Entry<Integer, String> entry : types.entrySet()) {
				idsWeaponTypes.put(entry.getKey(), 
						WeaponTypesMapperType.valueOf(entry.getValue().toUpperCase()));
				weaponTypesIds.put(WeaponTypesMapperType.valueOf(entry.getValue().toUpperCase()),
						entry.getKey());
			}

            if (log.isInfoEnabled()) {
			    log.info("Weapon types mapped");
            }
		} catch (Exception e) {
			log.error("Error mapping weapon types", e);
		}
	}
	
	public void fillItemTypes(Map<Integer, String> types) {
		idsItemTypes.clear();
		try {
			for (Map.Entry<Integer, String> entry : types.entrySet()) {
				idsItemTypes.put(entry.getKey(), 
						ItemTypesMapperType.valueOf(entry.getValue().toUpperCase()));
				itemTypesIds.put(ItemTypesMapperType.valueOf(entry.getValue().toUpperCase()),
						entry.getKey());
			}
            if (log.isInfoEnabled()) {
			    log.info("Item types mapped");
            }
		} catch (Exception e) {
			log.error("Error mapping item types", e);
		}
	}
	
	public void fillBonusTypes(Map<Integer, String> types) {
		idsBonusTypes.clear();
		try {
			for (Map.Entry<Integer, String> entry : types.entrySet()) {
				idsBonusTypes.put(entry.getKey(), 
						BonusTypesMapperType.valueOf(entry.getValue().toUpperCase()));
				bonusTypesIds.put(BonusTypesMapperType.valueOf(entry.getValue().toUpperCase()),
						entry.getKey());
			}

            if (log.isInfoEnabled()) {
			    log.info("Bonus types mapped");
            }
		} catch (Exception e) {
			log.error("Error mapping bonus types", e);
		}
	}
}
