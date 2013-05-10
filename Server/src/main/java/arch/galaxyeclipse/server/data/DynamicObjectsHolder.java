package arch.galaxyeclipse.server.data;

import arch.galaxyeclipse.server.data.model.LocationObject;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;

import static arch.galaxyeclipse.shared.GeConstants.DYNAMIC_OBJECT_QUERY_RADIUS;

/**
 *
 */
public class DynamicObjectsHolder {
    private Map<Integer, LocationObjectsHolder> locationObjectHolders;

    public DynamicObjectsHolder() {
        locationObjectHolders = new HashMap<>();
    }

    public void addLocationObject(LocationObject locationObject) {
        int locationId = locationObject.getLocationId();

        LocationObjectsHolder locationObjectsHolder = locationObjectHolders.get(locationId);
        if (locationObjectsHolder == null) {
            locationObjectsHolder = new LocationObjectsHolder();
            locationObjectHolders.put(locationId, locationObjectsHolder);
        }

        locationObjectsHolder.locationObjectsX.add(locationObject);
        locationObjectsHolder.locationObjectsY.add(locationObject);
    }

    public Collection<LocationObject> getDynamicObjects(LocationObject locationObject) {
        int locationId = locationObject.getLocationId();
        float positionX = locationObject.getPositionX();
        float positionY = locationObject.getPositionY();
        float x1 = positionX - DYNAMIC_OBJECT_QUERY_RADIUS;
        float x2 = positionX + DYNAMIC_OBJECT_QUERY_RADIUS;
        float y1 = positionY - DYNAMIC_OBJECT_QUERY_RADIUS;
        float y2 = positionY + DYNAMIC_OBJECT_QUERY_RADIUS;

        LocationObjectsHolder locationObjectsHolder = locationObjectHolders.get(locationId);
        locationObjectsHolder.x1.setPositionX(x1);
        locationObjectsHolder.x2.setPositionX(x2);
        locationObjectsHolder.y1.setPositionY(y1);
        locationObjectsHolder.y2.setPositionY(y2);
        return locationObjectsHolder.getMatchingObjects();
    }

    private static class LocationObjectsHolder {
        private TreeSet<LocationObject> locationObjectsX;
        private TreeSet<LocationObject> locationObjectsY;
        private LocationObject x1;
        private LocationObject x2;
        private LocationObject y1;
        private LocationObject y2;

        private LocationObjectsHolder() {
            locationObjectsX = new TreeSet<>(new LocationObjectXComparator());
            locationObjectsY = new TreeSet<>(new LocationObjectYComparator());

            x1 = new LocationObject();
            x2 = new LocationObject();
            y1 = new LocationObject();
            y2 = new LocationObject();
        }

        public Collection<LocationObject> getMatchingObjects() {

            return CollectionUtils.intersection(locationObjectsX.subSet(x1, true, x2, true),
                    locationObjectsY.subSet(y1, true, y2, true));
        }

        private static class LocationObjectXComparator implements Comparator<LocationObject> {
            @Override
            public int compare(LocationObject o1, LocationObject o2) {
                float x1 = o1.getPositionX();
                float x2 = o2.getPositionX();
                return x1 < x2 ? -1 : (x1 == x2 ? 0 : 1);
            }
        }

        private static class LocationObjectYComparator implements Comparator<LocationObject> {
            @Override
            public int compare(LocationObject o1, LocationObject o2) {
                float y1 = o1.getPositionY();
                float y2 = o2.getPositionY();
                return y1 < y2 ? -1 : (y1 == y2 ? 0 : 1);
            }
        }
    }
}
