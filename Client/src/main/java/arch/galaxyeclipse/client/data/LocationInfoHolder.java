package arch.galaxyeclipse.client.data;

import arch.galaxyeclipse.shared.*;
import arch.galaxyeclipse.shared.context.*;
import arch.galaxyeclipse.shared.protocol.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfo.*;
import arch.galaxyeclipse.shared.types.*;
import com.google.common.collect.*;
import lombok.*;
import lombok.extern.slf4j.*;

import java.util.*;

/**
 *
 */
@Slf4j
public class LocationInfoHolder {
    private @Getter String name;
    private @Getter int locationId;
    private @Getter float width;
    private @Getter float height;
    private Multiset<LocationObject> cachedObjects;
    private Multiset<LocationObject> dynamicObjects;
    private PositionPredicate positionPredicate;

    LocationInfoHolder() {
        positionPredicate = new PositionPredicate();
        cachedObjects = TreeMultiset.create(new LocationObjectOrdering());
        dynamicObjects = TreeMultiset.create(new LocationObjectOrdering());
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

        if (log.isDebugEnabled()) {
            log.debug("\tLocation " + name);
            log.debug("\tId " + locationId);
            log.debug("\tWidth " + width);
            log.debug("\tHeight " + height);

            outputObjects(objectsList);
        }
    }

    public void setDynamicObjects(List<LocationObject> locationObjects) {
        if (log.isDebugEnabled()) {
            log.debug("Dynamic objects update, count = " + dynamicObjects.size());
            outputObjects(locationObjects);
        }

        dynamicObjects = TreeMultiset.create(new LocationObjectOrdering());
        dynamicObjects.addAll(locationObjects);

    }

    public Multiset<LocationObject> getCachedObjects() {
        return cachedObjects;
    }

    public Set<LocationObject> getObjectsForRadius(GePosition position) {
        positionPredicate.setPosition(position);

        Set<LocationObject> locationObjects = new HashSet<>();
        locationObjects.addAll(Multisets.filter(cachedObjects, positionPredicate));
        locationObjects.addAll(Multisets.filter(dynamicObjects, positionPredicate));

        return locationObjects;
    }

    private void outputObjects(List<LocationObject> locationObjects) {
        log.debug("Objects:");
        DictionaryTypesMapper dictionaryTypesMapper = ContextHolder
                .getBean(DictionaryTypesMapper.class);
        for (LocationObject locationObject : locationObjects) {
            log.debug("\t" + dictionaryTypesMapper.getLocationObjectTypeById(
                    locationObject.getObjectTypeId()));
            log.debug("\tx = " + locationObject.getPositionX());
            log.debug("\ty = " + locationObject.getPositionY());
        }
    }

    @Data
    private static class PositionPredicate implements com.google.common.base.Predicate<LocationObject> {
        private GePosition position;

        @Override
        public boolean apply(LocationObject input) {
            float xAbs = Math.abs(input.getPositionX() - position.getX());
            float yAbs = Math.abs(input.getPositionY() - position.getY());
            return xAbs < SharedInfo.DYNAMIC_OBJECT_QUERY_RADIUS
                    && yAbs < SharedInfo.DYNAMIC_OBJECT_QUERY_RADIUS;
        }
    }

    private static class LocationObjectOrdering extends Ordering<LocationObject> {
        @Override
        public int compare(LocationObject left, LocationObject right) {
            return left.getPositionX() < right.getPositionX()
                    ? -1 : left.getPositionX() > right.getPositionX()
                    ? 1 : left.getPositionY() < right.getPositionY()
                    ? -1 : left.getPositionY() > right.getPositionY()
                    ? 1 : 0;
        }
    }
}
