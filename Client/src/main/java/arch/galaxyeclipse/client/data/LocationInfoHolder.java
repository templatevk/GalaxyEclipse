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
    @Getter
    private GeProtocol.LocationInfo locationInfo;
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
        this.locationInfo = locationInfo;

        List<LocationObject> objectsList = locationInfo.getLocationCachedObjects().getObjectsList();
        cachedObjects.addAll(objectsList);

        if (log.isDebugEnabled()) {
            log.debug("\tLocation " + locationInfo.getName());
            log.debug("\tId " + locationInfo.getLocationId());
            log.debug("\tWidth " + locationInfo.getWidth());
            log.debug("\tHeight " + locationInfo.getHeight());

            outputObjects(objectsList);
        }
    }

    public void setDynamicObjects(List<LocationObject> locationObjects) {
        if (log.isDebugEnabled()) {
            log.debug("Dynamic objects update" + dynamicObjects.size());
            outputObjects(locationObjects);
        }

        dynamicObjects = TreeMultiset.create(new LocationObjectOrdering());
        dynamicObjects.addAll(locationObjects);

    }

    public Multiset<LocationObject> getCachedObjects() {
        return cachedObjects;
    }

    public Set<LocationObject> getObjectsForRadius(Position position) {
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
         private Position position;

        @Override
        public boolean apply(LocationObject input) {
            float xAbs = input.getPositionX() - position.getX();
            float yAbs = input.getPositionY();
            return Math.abs(xAbs) < SharedInfo.DYNAMIC_OBJECT_QUERY_RADIUS
                    && Math.abs(yAbs - position.getY()) < SharedInfo.DYNAMIC_OBJECT_QUERY_RADIUS;
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
