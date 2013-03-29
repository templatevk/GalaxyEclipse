package arch.galaxyeclipse.shared.typesdictionary;

import java.util.*;

import org.apache.log4j.*;
import org.springframework.stereotype.*;

@Component
public class TypesMapper {
	private static final Logger log = Logger.getLogger(TypesMapper.class);
	
	private Map<Integer, LocationStaticObjectType> staticObjectTypes;
	private Map<Integer, LocationDynamicObjectType> dynamicObjectTypes;
	private Map<Integer, WeaponType> weaponTypes;
	private Map<Integer, BonusType> bonusTypes;
	private Map<Integer, ItemType> itemTypes;
	
	private TypesMapper() {
		staticObjectTypes = new HashMap<Integer, LocationStaticObjectType>();
		dynamicObjectTypes = new HashMap<Integer, LocationDynamicObjectType>();
		weaponTypes = new HashMap<Integer, WeaponType>();
		bonusTypes = new HashMap<Integer, BonusType>();
		itemTypes = new HashMap<Integer, ItemType>();
	}
	
	public LocationStaticObjectType getLocationStaticObjectType(int id) {
		return staticObjectTypes.get(id);
	}
	
	public LocationDynamicObjectType getLocationDynamicObjectType(int id) {
		return dynamicObjectTypes.get(id);
	}
	
	public WeaponType getWeaponType(int id) {
		return weaponTypes.get(id);
	}
	
	public BonusType getBonusType(int id) {
		return bonusTypes.get(id);
	}
	
	public ItemType getItemType(int id) {
		return itemTypes.get(id);
	}
	
	public void fillLocationStaticObjectType(Map<Integer, String> types) {
		staticObjectTypes.clear();
		try {
			for (Map.Entry<Integer, String> entry : types.entrySet()) {
				staticObjectTypes.put(entry.getKey(), 
						LocationStaticObjectType.valueOf(entry.getValue().toUpperCase()));
			}
			log.info("Location static objects mapped");
		} catch (Exception e) {
			log.error("Error mapping location static object types", e);
		}
	}
	
	public void fillLocationDynamicObjectType(Map<Integer, String> types) {
		dynamicObjectTypes.clear();
		try {
			for (Map.Entry<Integer, String> entry : types.entrySet()) {
				dynamicObjectTypes.put(entry.getKey(), 
						LocationDynamicObjectType.valueOf(entry.getValue().toUpperCase()));
			}
			log.info("Location dynamic objects mapped");
		} catch (Exception e) {
			log.error("Error mapping location c object types", e);
		}
	}
	
	public void fillWeaponType(Map<Integer, String> types) {
		weaponTypes.clear();
		try {
			for (Map.Entry<Integer, String> entry : types.entrySet()) {
				weaponTypes.put(entry.getKey(), 
						WeaponType.valueOf(entry.getValue().toUpperCase()));
			}
			log.info("Weapon types mapped");
		} catch (Exception e) {
			log.error("Error mapping weapon types", e);
		}
	}
	
	public void fillItemType(Map<Integer, String> types) {
		itemTypes.clear();
		try {
			for (Map.Entry<Integer, String> entry : types.entrySet()) {
				itemTypes.put(entry.getKey(), 
						ItemType.valueOf(entry.getValue().toUpperCase()));
			}
			log.info("Item types mapped");
		} catch (Exception e) {
			log.error("Error mapping item types", e);
		}
	}
	
	public void fillBonusType(Map<Integer, String> types) {
		bonusTypes.clear();
		try {
			for (Map.Entry<Integer, String> entry : types.entrySet()) {
				bonusTypes.put(entry.getKey(), 
						BonusType.valueOf(entry.getValue().toUpperCase()));
			}
			log.info("Bonus types mapped");
		} catch (Exception e) {
			log.error("Error mapping bonus types", e);
		}
	}
}
