package arch.galaxyeclipse.shared.typesdictionary;

import java.util.*;

import org.apache.log4j.*;

/**
 * Maps values from dictionary tables, name-id and id-name associations. 
 */
public class DictionaryTypesMapper {
	private static final Logger log = Logger.getLogger(DictionaryTypesMapper.class);
	
	// Mapping primary keys of the dictionary tables to the enum values and vise versa
	private Map<Integer, LocationStaticObjectType> idsStaticObjectTypes;
	private Map<Integer, LocationDynamicObjectType> idsDynamicObjectTypes;
	private Map<Integer, WeaponType> idsWeaponTypes;
	private Map<Integer, BonusType> idsBonusTypes;
	private Map<Integer, ItemType> idsItemTypes;
	private Map<LocationStaticObjectType, Integer> staticObjectTypesIds;
	private Map<LocationDynamicObjectType, Integer> dynamicObjectTypesIds;
	private Map<WeaponType, Integer> weaponTypesIds;
	private Map<BonusType, Integer> bonusTypesIds;
	private Map<ItemType, Integer> itemTypesIds;
	
	private DictionaryTypesMapper() {
		idsStaticObjectTypes = new HashMap<Integer, LocationStaticObjectType>();
		idsDynamicObjectTypes = new HashMap<Integer, LocationDynamicObjectType>();
		idsWeaponTypes = new HashMap<Integer, WeaponType>();
		idsBonusTypes = new HashMap<Integer, BonusType>();
		idsItemTypes = new HashMap<Integer, ItemType>();
	}
	
	public LocationStaticObjectType getLocationStaticObjectTypeById(int id) {
		return idsStaticObjectTypes.get(id);
	}
	
	public LocationDynamicObjectType getLocationDynamicObjectTypeById(int id) {
		return idsDynamicObjectTypes.get(id);
	}
	
	public WeaponType getWeaponTypeById(int id) {
		return idsWeaponTypes.get(id);
	}
	
	public BonusType getBonusTypeById(int id) {
		return idsBonusTypes.get(id);
	}
	
	public ItemType getIdByItemTypeById(int id) {
		return idsItemTypes.get(id);
	}
	
	public Integer getIdByLocationStaticObjectType(LocationStaticObjectType type) {
		return staticObjectTypesIds.get(type);
	}
	
	public Integer getIdByLocationDynamicObjectType(LocationDynamicObjectType type) {
		return dynamicObjectTypesIds.get(type);
	}
	
	public Integer getIdByWeaponType(WeaponType type) {
		return weaponTypesIds.get(type);
	}
	
	public Integer getIdByBonusType(BonusType type) {
		return bonusTypesIds.get(type);
	}
	
	public Integer getIdByItemType(ItemType type) {
		return itemTypesIds.get(type);
	}
	
	public void fillLocationStaticObjectTypes(Map<Integer, String> types) {
		idsStaticObjectTypes.clear();
		try {
			for (Map.Entry<Integer, String> entry : types.entrySet()) {
				idsStaticObjectTypes.put(entry.getKey(), 
						LocationStaticObjectType.valueOf(entry.getValue().toUpperCase()));
				staticObjectTypesIds.put(LocationStaticObjectType.valueOf(
						entry.getValue().toUpperCase()), entry.getKey());
			}
			log.info("Location static objects mapped");
		} catch (Exception e) {
			log.error("Error mapping location static object types", e);
		}
	}
	
	public void fillLocationDynamicObjectTypes(Map<Integer, String> types) {
		idsDynamicObjectTypes.clear();
		try {
			for (Map.Entry<Integer, String> entry : types.entrySet()) {
				idsDynamicObjectTypes.put(entry.getKey(), 
						LocationDynamicObjectType.valueOf(entry.getValue().toUpperCase()));
				dynamicObjectTypesIds.put(LocationDynamicObjectType.valueOf(
						entry.getValue().toUpperCase()), entry.getKey());
			}
			log.info("Location dynamic objects mapped");
		} catch (Exception e) {
			log.error("Error mapping location c object types", e);
		}
	}
	
	public void fillWeaponTypes(Map<Integer, String> types) {
		idsWeaponTypes.clear();
		try {
			for (Map.Entry<Integer, String> entry : types.entrySet()) {
				idsWeaponTypes.put(entry.getKey(), 
						WeaponType.valueOf(entry.getValue().toUpperCase()));
				weaponTypesIds.put(WeaponType.valueOf(entry.getValue().toUpperCase()),
						entry.getKey());
			}
			log.info("Weapon types mapped");
		} catch (Exception e) {
			log.error("Error mapping weapon types", e);
		}
	}
	
	public void fillItemTypes(Map<Integer, String> types) {
		idsItemTypes.clear();
		try {
			for (Map.Entry<Integer, String> entry : types.entrySet()) {
				idsItemTypes.put(entry.getKey(), 
						ItemType.valueOf(entry.getValue().toUpperCase()));
				itemTypesIds.put(ItemType.valueOf(entry.getValue().toUpperCase()),
						entry.getKey());
			}
			log.info("Item types mapped");
		} catch (Exception e) {
			log.error("Error mapping item types", e);
		}
	}
	
	public void fillBonusTypes(Map<Integer, String> types) {
		idsBonusTypes.clear();
		try {
			for (Map.Entry<Integer, String> entry : types.entrySet()) {
				idsBonusTypes.put(entry.getKey(), 
						BonusType.valueOf(entry.getValue().toUpperCase()));
				bonusTypesIds.put(BonusType.valueOf(entry.getValue().toUpperCase()),
						entry.getKey());
			}
			log.info("Bonus types mapped");
		} catch (Exception e) {
			log.error("Error mapping bonus types", e);
		}
	}
}
