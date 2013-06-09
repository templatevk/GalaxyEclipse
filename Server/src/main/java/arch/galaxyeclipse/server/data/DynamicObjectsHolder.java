package arch.galaxyeclipse.server.data;

import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfoPacket.LocationObjectPacket.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

import static arch.galaxyeclipse.shared.GeConstants.RADIUS_DYNAMIC_OBJECT_QUERY;

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
        private NavigableSet<Builder> locationObjectsX;
        private NavigableSet<LocationObjectPacket.Builder> locationObjectsY;

        private LocationObjectsHolder() {
            locationObjectsX = new ConcurrentSkipListSet<>(new LocationObjectXComparator());
            locationObjectsY = new ConcurrentSkipListSet<>(new LocationObjectYComparator());
        }

        public void addLopBuilder(LocationObjectPacket.Builder lopBuilder) {
            locationObjectsX.add(lopBuilder);
            locationObjectsY.add(lopBuilder);
        }

        public void removeLopBuilder(LocationObjectPacket.Builder lopBuilder) {
            locationObjectsX.remove(lopBuilder);
            locationObjectsY.remove(lopBuilder);
        }

        public void updateLopBuilderX(LocationObjectPacket.Builder lopBuilder, int positionX) {
            locationObjectsX.remove(lopBuilder);
            lopBuilder.setPositionX(positionX);
            locationObjectsX.add(lopBuilder);
        }

        public void updateLopBuilderY(LocationObjectPacket.Builder lopBuilder, int positionY) {
            locationObjectsY.remove(lopBuilder);
            lopBuilder.setPositionY(positionY);
            locationObjectsY.add(lopBuilder);
        }

        public Collection<LocationObjectPacket.Builder> getMatchingObjects(
                LocationObjectPacket.Builder lopBuilder) {
            int positionX = lopBuilder.getPositionX();
            int positionY = lopBuilder.getPositionY();
            int x1Pos = positionX - RADIUS_DYNAMIC_OBJECT_QUERY;
            int x2Pos = positionX + RADIUS_DYNAMIC_OBJECT_QUERY;
            int y1Pos = positionY - RADIUS_DYNAMIC_OBJECT_QUERY;
            int y2Pos = positionY + RADIUS_DYNAMIC_OBJECT_QUERY;

            LocationObjectPacket.Builder x1 = LocationObjectPacket.newBuilder();
            LocationObjectPacket.Builder x2 = LocationObjectPacket.newBuilder();
            LocationObjectPacket.Builder y1 = LocationObjectPacket.newBuilder();
            LocationObjectPacket.Builder y2 = LocationObjectPacket.newBuilder();
            x1.setPositionX(x1Pos);
            x2.setPositionX(x2Pos);
            y1.setPositionY(y1Pos);
            y2.setPositionY(y2Pos);

            NavigableSet<Builder> xMatching = locationObjectsX.subSet(x1, true, x2, true);
            NavigableSet<Builder> yMatching = locationObjectsY.subSet(y1, true, y2, true);
            return CollectionUtils.intersection(xMatching, yMatching);
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
