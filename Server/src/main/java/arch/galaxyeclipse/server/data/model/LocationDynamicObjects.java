package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Table(name = "location_dynamic_objects", schema = "", catalog = "ge")
@Entity
public class LocationDynamicObjects {
    private int locationDynamicObjectId;

    @Column(name = "location_dynamic_object_id")
    @Id
    public int getLocationDynamicObjectId() {
        return locationDynamicObjectId;
    }

    public void setLocationDynamicObjectId(int locationDynamicObjectId) {
        this.locationDynamicObjectId = locationDynamicObjectId;
    }

    private int objectNativeId;

    @Column(name = "object_native_id")
    @Basic
    public int getObjectNativeId() {
        return objectNativeId;
    }

    public void setObjectNativeId(int objectNativeId) {
        this.objectNativeId = objectNativeId;
    }

    private int positionX;

    @Column(name = "position_x")
    @Basic
    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    private int positionY;

    @Column(name = "position_y")
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

        LocationDynamicObjects that = (LocationDynamicObjects) o;

        if (locationDynamicObjectId != that.locationDynamicObjectId) return false;
        if (objectNativeId != that.objectNativeId) return false;
        if (positionX != that.positionX) return false;
        if (positionY != that.positionY) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = locationDynamicObjectId;
        result = 31 * result + objectNativeId;
        result = 31 * result + positionX;
        result = 31 * result + positionY;
        return result;
    }
}
