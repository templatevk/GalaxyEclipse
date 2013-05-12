package arch.galaxyeclipse.server.data;

import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;

import static arch.galaxyeclipse.shared.GeConstants.DYNAMIC_OBJECT_QUERY_RADIUS;

/**
 *
 */
@Slf4j
public class DynamicObjectsHolder {
    private Map<Integer, LocationObjectsHolder> locationObjectHolders;

    public DynamicObjectsHolder() {
        locationObjectHolders = new HashMap<>();
    }

    public LocationObjectsHolder getLocationObjectsHolder(int locationId) {
        LocationObjectsHolder locationObjectsHolder = locationObjectHolders.get(locationId);
        if (locationObjectsHolder == null) {
            locationObjectsHolder = new LocationObjectsHolder();
            locationObjectHolders.put(locationId, locationObjectsHolder);
        }

        return locationObjectsHolder;
    }

    public static class LocationObjectsHolder {
        private TreeSet<LocationObjectPacket.Builder> locationObjectsX;
        private TreeSet<LocationObjectPacket.Builder> locationObjectsY;

        private LocationObjectsHolder() {
            locationObjectsX = new TreeSet<>(new LocationObjectXComparator());
            locationObjectsY = new TreeSet<>(new LocationObjectYComparator());
        }

        public void addLopBuilder(LocationObjectPacket.Builder lopBuilder) {
            locationObjectsX.add(lopBuilder);
            locationObjectsY.add(lopBuilder);
        }

        public void removeLopBuilder(LocationObjectPacket.Builder lopBuilder) {
            locationObjectsX.remove(lopBuilder);
            locationObjectsY.remove(lopBuilder);
        }

        public void updateLopBuilderX(LocationObjectPacket.Builder lopBuilder, float positionX) {
            locationObjectsX.remove(lopBuilder);
            lopBuilder.setPositionX(positionX);
            locationObjectsX.add(lopBuilder);
        }

        public void updateLopBuilderY(LocationObjectPacket.Builder lopBuilder, float positionY) {
            locationObjectsY.remove(lopBuilder);
            lopBuilder.setPositionY(positionY);
            locationObjectsY.add(lopBuilder);
        }

        public Collection<LocationObjectPacket.Builder> getMatchingObjects(
                LocationObjectPacket.Builder lopBuilder) {
            float positionX = lopBuilder.getPositionX();
            float positionY = lopBuilder.getPositionY();
            float x1Pos = positionX - DYNAMIC_OBJECT_QUERY_RADIUS;
            float x2Pos = positionX + DYNAMIC_OBJECT_QUERY_RADIUS;
            float y1Pos = positionY - DYNAMIC_OBJECT_QUERY_RADIUS;
            float y2Pos = positionY + DYNAMIC_OBJECT_QUERY_RADIUS;

            LocationObjectPacket.Builder x1 = LocationObjectPacket.newBuilder();
            LocationObjectPacket.Builder x2 = LocationObjectPacket.newBuilder();
            LocationObjectPacket.Builder y1 = LocationObjectPacket.newBuilder();
            LocationObjectPacket.Builder y2 = LocationObjectPacket.newBuilder();
            x1.setPositionX(x1Pos);
            x2.setPositionX(x2Pos);
            y1.setPositionY(y1Pos);
            y2.setPositionY(y2Pos);

            try {
                NavigableSet xMatchingSet = ((TreeSet)locationObjectsX.clone())
                        .subSet(x1, true, x2, true);
                NavigableSet yMatchingSet = ((TreeSet)locationObjectsY.clone())
                        .subSet(y1, true, y2, true);
                return CollectionUtils.intersection(xMatchingSet, yMatchingSet);
            } catch (Exception e) {
                log.error("FATAL ERROR", e);
                System.exit(0);
                return null;
            }
        }

        private static class LocationObjectXComparator
                implements Comparator<LocationObjectPacket.Builder> {
            @Override
            public int compare(LocationObjectPacket.Builder o1, LocationObjectPacket.Builder o2) {
                boolean equal = o1.getObjectId() == o2.getObjectId();
                float x1 = o1.getPositionX();
                float x2 = o2.getPositionX();
                return x1 < x2 ? -1 : (equal ? 0 : 1);
            }
        }

        private static class LocationObjectYComparator
                implements Comparator<LocationObjectPacket.Builder> {
            @Override
            public int compare(LocationObjectPacket.Builder o1, LocationObjectPacket.Builder o2) {
                boolean equal = o1.getObjectId() == o2.getObjectId();
                float y1 = o1.getPositionY();
                float y2 = o2.getPositionY();
                return y1 < y2 ? -1 : (equal ? 0 : 1);
            }
        }
    }
}
