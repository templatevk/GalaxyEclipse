package arch.galaxyeclipse.client.data;

import arch.galaxyeclipse.shared.GeConstants;
import arch.galaxyeclipse.shared.common.GePosition;
import arch.galaxyeclipse.shared.context.GeContextHolder;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeLocationInfoPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeLocationInfoPacket.GeLocationObjectPacket;
import arch.galaxyeclipse.shared.types.GeDictionaryTypesMapper;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultiset;
import lombok.Data;
import lombok.Delegate;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Slf4j
public class GeLocationInfoHolder {

    private @Delegate GeLocationInfoPacket lip = GeLocationInfoPacket.getDefaultInstance();

    private TreeMultiset<GeLocationObjectPacket> cachedObjects;
    private TreeMultiset<GeLocationObjectPacket> dynamicObjects;
    private PositionPredicate positionPredicate;

    GeLocationInfoHolder() {
        positionPredicate = new PositionPredicate();
        cachedObjects = TreeMultiset.create(new LocationObjectPositionOrdering());
        dynamicObjects = TreeMultiset.create(new LocationObjectPositionOrdering());
    }

    public void setLip(GeLocationInfoPacket lip) {
        this.lip = lip;
        if (GeLocationInfoHolder.log.isInfoEnabled()) {
            GeLocationInfoHolder.log.info("Updating location info");
        }

        List<GeLocationObjectPacket> objectsList = lip.getLocationCachedObjects()
                .getObjectsList();
        cachedObjects.addAll(objectsList);

        if (GeLocationInfoHolder.log.isDebugEnabled()) {
            GeLocationInfoHolder.log.debug("\tLocation " + lip.getName());
            GeLocationInfoHolder.log.debug("\tId " + lip.getLocationId());
            GeLocationInfoHolder.log.debug("\tWidth " + lip.getWidth());
            GeLocationInfoHolder.log.debug("\tHeight " + lip.getHeight());

            outputObjects(objectsList);
        }
    }

    public void setDynamicObjects(List<GeLocationObjectPacket> locationObjects) {
        if (GeLocationInfoHolder.log.isDebugEnabled()) {
            GeLocationInfoHolder.log.debug("Dynamic objects update, count = " + dynamicObjects.size());
            outputObjects(locationObjects);
        }

        dynamicObjects = TreeMultiset.create(new LocationObjectPositionOrdering());
        dynamicObjects.addAll(locationObjects);
    }

    public Multiset<GeLocationObjectPacket> getCachedObjects() {
        return cachedObjects;
    }

    public List<GeLocationObjectPacket> getObjectsForClientRadius(GePosition position) {
        positionPredicate.setPosition(position);

        List<GeLocationObjectPacket> locationObjects = new ArrayList<>();
        locationObjects.addAll(Collections2.filter(cachedObjects, positionPredicate));
        locationObjects.addAll(Collections2.filter(dynamicObjects, positionPredicate));

        return locationObjects;
    }

    private void outputObjects(List<GeLocationObjectPacket> locationObjects) {
        GeLocationInfoHolder.log.debug("Objects:");
        GeDictionaryTypesMapper dictionaryTypesMapper = GeContextHolder
                .getBean(GeDictionaryTypesMapper.class);
        for (GeLocationObjectPacket locationObject : locationObjects) {
            GeLocationInfoHolder.log.debug("\t" + dictionaryTypesMapper.getLocationObjectTypeById(
                    locationObject.getObjectTypeId()));
            GeLocationInfoHolder.log.debug("\tx = " + locationObject.getPositionX());
            GeLocationInfoHolder.log.debug("\ty = " + locationObject.getPositionY());
        }
    }

    @Data
    static class PositionPredicate implements Predicate<GeLocationObjectPacket> {

        @Delegate
        private GePosition position;

        @Override
        public boolean apply(GeLocationObjectPacket input) {
            float xAbs = Math.abs(input.getPositionX() - position.getX());
            float yAbs = Math.abs(input.getPositionY() - position.getY());
            return xAbs < GeConstants.RADIUS_DYNAMIC_OBJECT_QUERY
                    && yAbs < GeConstants.RADIUS_DYNAMIC_OBJECT_QUERY;
        }
    }

    static class LocationObjectPositionOrdering extends Ordering<GeLocationObjectPacket> {

        @Override
        public int compare(GeLocationObjectPacket left, GeLocationObjectPacket right) {
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
