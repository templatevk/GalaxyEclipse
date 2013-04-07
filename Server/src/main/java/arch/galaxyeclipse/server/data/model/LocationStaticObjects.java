package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Table(name = "location_static_objects", schema = "", catalog = "ge")
@Entity
public class LocationStaticObjects {
    private int locationStaticObjectId;

    @Column(name = "location_static_object_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getLocationStaticObjectId() {
        return locationStaticObjectId;
    }

    public void setLocationStaticObjectId(int locationStaticObjectId) {
        this.locationStaticObjectId = locationStaticObjectId;
    }

    private int locationStaticObjectTypeId;

    @Column(name = "location_static_object_type_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getLocationStaticObjectTypeId() {
        return locationStaticObjectTypeId;
    }

    public void setLocationStaticObjectTypeId(int locationStaticObjectTypeId) {
        this.locationStaticObjectTypeId = locationStaticObjectTypeId;
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

        LocationStaticObjects that = (LocationStaticObjects) o;

        if (locationId != that.locationId) return false;
        if (locationStaticObjectId != that.locationStaticObjectId) return false;
        if (locationStaticObjectTypeId != that.locationStaticObjectTypeId) return false;
        if (positionX != that.positionX) return false;
        if (positionY != that.positionY) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = locationStaticObjectId;
        result = 31 * result + locationStaticObjectTypeId;
        result = 31 * result + locationId;
        result = 31 * result + positionX;
        result = 31 * result + positionY;
        return result;
    }
}
