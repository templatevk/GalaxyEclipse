package arch.galaxyeclipse.shared.util;


import java.util.*;

/**
 *
 */
public class CollectionUtils {
    private CollectionUtils() {

    }

    public static <T> T getFirst(Collection<T> collection) {
        if (!collection.isEmpty()) {
            return collection.iterator().next();
        }
        return null;
    }
}
