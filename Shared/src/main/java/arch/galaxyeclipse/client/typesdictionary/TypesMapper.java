package arch.galaxyeclipse.client.typesdictionary;

import java.util.*;

public class TypesMapper {
	private static final TypesMapper INSTANCE = new TypesMapper();
	
	private Map<Integer, LocationStaticObjectType> staticObjectTypes;
	private Map<Integer, WeaponType> weaponTypes;
	private Map<Integer, BonusType> bonusTypes;
	private Map<Integer, ItemType> itemTypes;
	
	private TypesMapper() {
		staticObjectTypes = new HashMap<Integer, LocationStaticObjectType>();
		weaponTypes = new HashMap<Integer, WeaponType>();
		bonusTypes = new HashMap<Integer, BonusType>();
		itemTypes = new HashMap<Integer, ItemType>();
	}
	
	public static TypesMapper getInstance() {
		return INSTANCE;
	}
	
	public LocationStaticObjectType getLocationStaticObjectType(int id) {
		return staticObjectTypes.get(id);
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
		} catch (Exception e) {
			throw new RuntimeException("Error mapping location static object types", e);
		}
	}
	
	public void fillWeaponType(Map<Integer, String> types) {
		weaponTypes.clear();
		try {
			for (Map.Entry<Integer, String> entry : types.entrySet()) {
				weaponTypes.put(entry.getKey(), 
						WeaponType.valueOf(entry.getValue().toUpperCase()));
			}
		} catch (Exception e) {
			throw new RuntimeException("Error mapping weapon types", e);
		}
	}
	
	public void fillItemType(Map<Integer, String> types) {
		itemTypes.clear();
		try {
			for (Map.Entry<Integer, String> entry : types.entrySet()) {
				itemTypes.put(entry.getKey(), 
						ItemType.valueOf(entry.getValue().toUpperCase()));
			}
		} catch (Exception e) {
			throw new RuntimeException("Error mapping item types", e);
		}
	}
	
	public void fillBonusType(Map<Integer, String> types) {
		bonusTypes.clear();
		try {
			for (Map.Entry<Integer, String> entry : types.entrySet()) {
				bonusTypes.put(entry.getKey(), 
						BonusType.valueOf(entry.getValue().toUpperCase()));
			}
		} catch (Exception e) {
			throw new RuntimeException("Error mapping bonus types", e);
		}
	}
}
