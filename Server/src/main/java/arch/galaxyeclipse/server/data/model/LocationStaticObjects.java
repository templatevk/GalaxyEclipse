package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Table(name = "location_static_objects", schema = "", catalog = "ge")
@Entity
public class LocationStaticObjects {
    private int locationStaticObjectId;

    @Column(name = "location_static_object_id")
    @Id
    public int getLocationStaticObjectId() {
        return locationStaticObjectId;
    }

    public void setLocationStaticObjectId(int locationStaticObjectId) {
        this.locationStaticObjectId = locationStaticObjectId;
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

        LocationStaticObjects that = (LocationStaticObjects) o;

        if (locationStaticObjectId != that.locationStaticObjectId) return false;
        if (positionX != that.positionX) return false;
        if (positionY != that.positionY) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = locationStaticObjectId;
        result = 31 * result + positionX;
        result = 31 * result + positionY;
        return result;
    }
}
