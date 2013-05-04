package arch.galaxyeclipse.client.data;

import arch.galaxyeclipse.shared.SharedInfo;
import arch.galaxyeclipse.shared.context.ContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfo.LocationObject;
import arch.galaxyeclipse.shared.types.DictionaryTypesMapper;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultiset;
import lombok.Data;
import lombok.Delegate;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Slf4j
public class LocationInfoHolder {
    private @Getter String name;
    private @Getter int locationId;
    private @Getter float width;
    private @Getter float height;
    private TreeMultiset<LocationObject> cachedObjects;
    private TreeMultiset<LocationObject> dynamicObjects;
    private PositionPredicate positionPredicate;

    LocationInfoHolder() {
        positionPredicate = new PositionPredicate();
        cachedObjects = TreeMultiset.create(new LocationObjectPositionOrdering());
        dynamicObjects = TreeMultiset.create(new LocationObjectPositionOrdering());
    }

    public void setLocationInfo(GeProtocol.LocationInfo locationInfo) {
        if (LocationInfoHolder.log.isInfoEnabled()) {
            LocationInfoHolder.log.info("Updating location info");
        }
        name = locationInfo.getName();
        locationId = locationInfo.getLocationId();
        width = locationInfo.getWidth();
        height = locationInfo.getHeight();

        List<LocationObject> objectsList = locationInfo.getLocationCachedObjects().getObjectsList();
        cachedObjects.addAll(objectsList);

        if (LocationInfoHolder.log.isDebugEnabled()) {
            LocationInfoHolder.log.debug("\tLocation " + name);
            LocationInfoHolder.log.debug("\tId " + locationId);
            LocationInfoHolder.log.debug("\tWidth " + width);
            LocationInfoHolder.log.debug("\tHeight " + height);

            outputObjects(objectsList);
        }
    }

    public void setDynamicObjects(List<LocationObject> locationObjects) {
        if (LocationInfoHolder.log.isDebugEnabled()) {
            LocationInfoHolder.log.debug("Dynamic objects update, count = " + dynamicObjects.size());
            outputObjects(locationObjects);
        }

        dynamicObjects = TreeMultiset.create(new LocationObjectPositionOrdering());
        dynamicObjects.addAll(locationObjects);
    }

    public Multiset<LocationObject> getCachedObjects() {
        return cachedObjects;
    }

    public List<LocationObject> getObjectsForRadius(GePosition position) {
        positionPredicate.setPosition(position);

        List<LocationObject> locationObjects = new ArrayList<>();
        locationObjects.addAll(Collections2.filter(cachedObjects, positionPredicate));
        locationObjects.addAll(Collections2.filter(dynamicObjects, positionPredicate));

        return locationObjects;
    }

    private void outputObjects(List<LocationObject> locationObjects) {
        LocationInfoHolder.log.debug("Objects:");
        DictionaryTypesMapper dictionaryTypesMapper = ContextHolder
                .getBean(DictionaryTypesMapper.class);
        for (LocationObject locationObject : locationObjects) {
            LocationInfoHolder.log.debug("\t" + dictionaryTypesMapper.getLocationObjectTypeById(
                    locationObject.getObjectTypeId()));
            LocationInfoHolder.log.debug("\tx = " + locationObject.getPositionX());
            LocationInfoHolder.log.debug("\ty = " + locationObject.getPositionY());
        }
    }

    @Data
    static class PositionPredicate implements Predicate<LocationObject> {
        @Delegate
        private GePosition position;

        @Override
        public boolean apply(LocationObject input) {
            float xAbs = Math.abs(input.getPositionX() - position.getX());
            float yAbs = Math.abs(input.getPositionY() - position.getY());
            return xAbs < SharedInfo.DYNAMIC_OBJECT_QUERY_RADIUS
                    && yAbs < SharedInfo.DYNAMIC_OBJECT_QUERY_RADIUS;
        }
    }

    static class LocationObjectPositionOrdering extends Ordering<LocationObject> {
        @Override
        public int compare(LocationObject left, LocationObject right) {
            return left.getPositionX() < right.getPositionX()
                    ? -1 : left.getPositionX() > right.getPositionX()
                    ? 1 : left.getPositionY() < right.getPositionY()
                    ? -1 : left.getPositionY() > right.getPositionY()
                    // Dirty hack, objectId will always be unique, doing that because guava's
                    // TreeMultiset duplicates existing element instead of adding the new one :(
                    ? 1 : left.getObjectId() - right.getObjectId();
        }
    }
}
