package arch.galaxyeclipse.server.data;

 import arch.galaxyeclipse.shared.types.*;

import java.util.*;

/**
 * Helper interface for filling DictionaryTypesMapper.
 */
public interface IDictionaryTypesMapperHelper {
    Map<Integer, String>  getBonusTypes();
    Map<Integer, String>  getItemTypes();
    Map<Integer, String>  getWeaponTypes();
    Map<Integer, String>  getLocationObjectTypes();
    Map<Integer, String> getLocationObjectBehaviorTypes();

    void fillAll(DictionaryTypesMapper dictionaryTypesMapper);
}
