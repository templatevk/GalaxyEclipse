package arch.galaxyeclipse.client.data;

import arch.galaxyeclipse.shared.*;
import arch.galaxyeclipse.shared.protocol.*;
import arch.galaxyeclipse.shared.protocol.GeProtocol.LocationInfo.*;
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
        cachedObjects = TreeMultiset.create(new Ordering<LocationObject>() {
            @Override
            public int compare(LocationObject left, LocationObject right) {
                return left.getPositionX() < right.getPositionX()
                        ? -1 : left.getPositionX() > right.getPositionX()
                        ? 1 : left.getPositionY() < right.getPositionY()
                        ? -1 : left.getPositionY() > right.getPositionY()
                        ? 1 : 0;
            }
        });
    }

    public void setLocationInfo(GeProtocol.LocationInfo locationInfo) {
        // TODO process location info
        if (LocationInfoHolder.log.isInfoEnabled()) {
            LocationInfoHolder.log.info("Updating location info");
        }
        this.locationInfo = locationInfo;

        List<LocationObject> objectsList = locationInfo.getLocationCachedObjects().getObjectsList();
        for (LocationObject locationObject : objectsList) {
            cachedObjects.add(locationObject);
        }
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

    @Data
    private class PositionPredicate implements com.google.common.base.Predicate<LocationObject> {
         private Position position;

        @Override
        public boolean apply(LocationObject input) {
            float xAbs = input.getPositionX() - position.getX();
            float yAbs = input.getPositionY();
            return Math.abs(xAbs) < SharedInfo.DYNAMIC_OBJECT_QUERY_RADIUS
                    && Math.abs(yAbs - position.getY()) < SharedInfo.DYNAMIC_OBJECT_QUERY_RADIUS;
        }
    }
}
