package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Table(name = "location_objects", schema = "", catalog = "ge")
@Entity
public class LocationObjects {
    private int locationObjectId;

    @Column(name = "location_object_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getLocationObjectId() {
        return locationObjectId;
    }

    public void setLocationObjectId(int locationObjectId) {
        this.locationObjectId = locationObjectId;
    }

    private int locationObjectBehaviorTypeId;

    @Column(name = "location_object_behavior_type_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getLocationObjectBehaviorTypeId() {
        return locationObjectBehaviorTypeId;
    }

    public void setLocationObjectBehaviorTypeId(int locationObjectBehaviorTypeId) {
        this.locationObjectBehaviorTypeId = locationObjectBehaviorTypeId;
    }

    private int locationObjectTypeId;

    @Column(name = "location_object_type_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getLocationObjectTypeId() {
        return locationObjectTypeId;
    }

    public void setLocationObjectTypeId(int locationObjectTypeId) {
        this.locationObjectTypeId = locationObjectTypeId;
    }

    private int objectNativeId;

    @Column(name = "object_native_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getObjectNativeId() {
        return objectNativeId;
    }

    public void setObjectNativeId(int objectNativeId) {
        this.objectNativeId = objectNativeId;
    }

    private int locationId;

    @Column(name = "location_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    private int positionX;

    @Column(name = "position_x", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    private int positionY;

    @Column(name = "position_y", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocationObjects that = (LocationObjects) o;

        if (locationId != that.locationId) return false;
        if (locationObjectBehaviorTypeId != that.locationObjectBehaviorTypeId) return false;
        if (locationObjectId != that.locationObjectId) return false;
        if (locationObjectTypeId != that.locationObjectTypeId) return false;
        if (objectNativeId != that.objectNativeId) return false;
        if (positionX != that.positionX) return false;
        if (positionY != that.positionY) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = locationObjectId;
        result = 31 * result + locationObjectBehaviorTypeId;
        result = 31 * result + locationObjectTypeId;
        result = 31 * result + objectNativeId;
        result = 31 * result + locationId;
        result = 31 * result + positionX;
        result = 31 * result + positionY;
        return result;
    }
}
