package arch.galaxyeclipse.server.data;

import arch.galaxyeclipse.shared.GeConstants;
import arch.galaxyeclipse.shared.common.GeMathUtilsCopied;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeLocationInfoPacket.GeLocationObjectPacket;
import arch.galaxyeclipse.shared.protocol.GeProtocol.GeLocationInfoPacket.GeLocationObjectPacket.Builder;
import arch.galaxyeclipse.shared.thread.GeTaskRunnablePair;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Slf4j
public class GeDynamicObjectsHolder {

    private Map<Integer, GeLocationObjectsHolder> locationObjectHolders;

    public GeDynamicObjectsHolder() {
        locationObjectHolders = new HashMap<>();
    }

    public GeLocationObjectsHolder getLocationObjectsHolder(int locationId) {
        GeLocationObjectsHolder locationObjectsHolder = locationObjectHolders.get(locationId);
        if (locationObjectsHolder == null) {
            locationObjectsHolder = new GeLocationObjectsHolder();
            locationObjectHolders.put(locationId, locationObjectsHolder);
        }

        return locationObjectsHolder;
    }

    public static class GeLocationObjectsHolder {

        private GeLocationObjectsHolder.GeMovingObjectUpdater movingObjectUpdater;
        private NavigableSet<Builder> locationObjectsX;
        private NavigableSet<Builder> locationObjectsY;
        private Set<GeMovingLocationObject> movingObjects;
        private Map<Integer, Builder> lopByIdMap;

        private GeLocationObjectsHolder() {
            locationObjectsX = new ConcurrentSkipListSet<>(new LocationObjectXComparator());
            locationObjectsY = new ConcurrentSkipListSet<>(new LocationObjectYComparator());
            movingObjects = new CopyOnWriteArraySet<>();
            lopByIdMap = new HashMap<>();

            movingObjectUpdater = new GeMovingObjectUpdater();
            movingObjectUpdater.start();
        }

        public void addMovingObject(GeMovingLocationObject object) {
            addLopBuilder(object.lopBuilder);
            movingObjects.add(object);
        }

        public void removeMovingObject(GeMovingLocationObject object) {
            movingObjects.remove(object);
            removeLopBuilder(object.lopBuilder);
        }

        public void addLopBuilder(GeLocationObjectPacket.Builder lopBuilder) {
            locationObjectsX.add(lopBuilder);
            locationObjectsY.add(lopBuilder);
            lopByIdMap.put(lopBuilder.getObjectId(), lopBuilder);
        }

        public void removeLopBuilder(GeLocationObjectPacket.Builder lopBuilder) {
            locationObjectsX.remove(lopBuilder);
            locationObjectsY.remove(lopBuilder);
            lopByIdMap.remove(lopBuilder.getObjectId());
        }

        public void updateLopBuilderX(GeLocationObjectPacket.Builder lopBuilder, float positionX) {
            locationObjectsX.remove(lopBuilder);
            lopBuilder.setPositionX(positionX);
            locationObjectsX.add(lopBuilder);
        }

        public void updateLopBuilderY(GeLocationObjectPacket.Builder lopBuilder, float positionY) {
            locationObjectsY.remove(lopBuilder);
            lopBuilder.setPositionY(positionY);
            locationObjectsY.add(lopBuilder);
        }

        public Builder getLopById(int id) {
            return lopByIdMap.get(id);
        }

        public Collection<Builder> getNearbyObjects(Builder lopBuilder, float radius) {
            float positionX = lopBuilder.getPositionX();
            float positionY = lopBuilder.getPositionY();
            float x1Pos = positionX - radius;
            float x2Pos = positionX + radius;
            float y1Pos = positionY - radius;
            float y2Pos = positionY + radius;

            GeLocationObjectPacket.Builder x1 = GeLocationObjectPacket.newBuilder();
            GeLocationObjectPacket.Builder x2 = GeLocationObjectPacket.newBuilder();
            GeLocationObjectPacket.Builder y1 = GeLocationObjectPacket.newBuilder();
            GeLocationObjectPacket.Builder y2 = GeLocationObjectPacket.newBuilder();
            x1.setPositionX(x1Pos);
            x2.setPositionX(x2Pos);
            y1.setPositionY(y1Pos);
            y2.setPositionY(y2Pos);

            NavigableSet<Builder> xMatching = locationObjectsX.subSet(x1, true, x2, true);
            NavigableSet<Builder> yMatching = locationObjectsY.subSet(y1, true, y2, true);
            return CollectionUtils.intersection(xMatching, yMatching);
        }

        public class GeMovingLocationObject {

            private GeLocationObjectPacket.Builder lopBuilder;
            private float moveSpeed;
            private float elapseDistance;
            private float initialX;
            private float initialY;

            public GeMovingLocationObject(Builder lopBuilder, float moveSpeed) {
                this(lopBuilder, moveSpeed, 0);
            }

            public GeMovingLocationObject(Builder lopBuilder, float moveSpeed, float elapseDistance) {
                this.lopBuilder = lopBuilder;
                this.moveSpeed = moveSpeed;
                this.elapseDistance = elapseDistance;

                initialX = lopBuilder.getPositionX();
                initialY = lopBuilder.getPositionY();
            }

            public void move(long msElapsed) {
                float positionX = lopBuilder.getPositionX();
                float positionY = lopBuilder.getPositionY();
                float rotationAngle = lopBuilder.getRotationAngle();

                float coef = msElapsed / GeConstants.DELAY_OBJECT_POSITION_UPDATE;
                float xDiff = moveSpeed * coef * GeMathUtilsCopied.sinDeg(rotationAngle);
                float yDiff = moveSpeed * coef * GeMathUtilsCopied.cosDeg(rotationAngle);

                positionX += xDiff;
                // -= here because of client rendering
                positionY -= yDiff;

                updateLopBuilderX(lopBuilder, positionX);
                updateLopBuilderY(lopBuilder, positionY);

                if (elapseDistance != 0) {
                    double x = initialX - positionX;
                    double y = initialY - positionY;
                    if (Math.sqrt(x * x + y * y) > elapseDistance) {
                        removeMovingObject(this);
                    }
                }
            }
        }

        private class GeMovingObjectUpdater extends GeTaskRunnablePair<Runnable>
                implements Runnable {

            private static final long DELAY = 10;

            private Stopwatch stopwatch = new Stopwatch();
            private long prevElapsed;

            private GeMovingObjectUpdater() {
                super(DELAY);
                setRunnable(this);
            }

            @Override
            public void run() {
                long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
                for (GeMovingLocationObject object : movingObjects) {
                    object.move(elapsed - prevElapsed);
                }
                prevElapsed = elapsed;
            }

            @Override
            public void start() {
                stopwatch.start();
                super.start();
            }

            @Override
            public void stop() {
                stopwatch.stop();
                super.stop();
            }
        }
    }

    private static class LocationObjectXComparator
            implements Comparator<GeLocationObjectPacket.Builder> {

        @Override
        public int compare(GeLocationObjectPacket.Builder o1, GeLocationObjectPacket.Builder o2) {
            boolean equal = o1.getObjectId() == o2.getObjectId();
            float x1 = o1.getPositionX();
            float x2 = o2.getPositionX();
            return x1 < x2 ? -1 : (equal ? 0 : 1);
        }
    }

    private static class LocationObjectYComparator
            implements Comparator<GeLocationObjectPacket.Builder> {

        @Override
        public int compare(GeLocationObjectPacket.Builder o1, GeLocationObjectPacket.Builder o2) {
            boolean equal = o1.getObjectId() == o2.getObjectId();
            float y1 = o1.getPositionY();
            float y2 = o2.getPositionY();
            return y1 < y2 ? -1 : (equal ? 0 : 1);
        }
    }
}
