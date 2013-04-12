package arch.galaxyeclipse.server.data;


import java.util.*;

/**
 *
 */
public class EntityUtils {
    private EntityUtils() {

    }

    public static <T> T getFirst(Collection<T> collection) {
        if (!collection.isEmpty()) {
            return collection.iterator().next();
        }
        return null;
    }
}
