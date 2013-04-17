package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;
import java.util.*;

/**
 *
 */
@Table(name = "location_object", schema = "", catalog = "ge")
@Entity
public class LocationObject {
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

    private float positionX;

    @Column(name = "position_x", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    private float positionY;

    @Column(name = "position_y", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocationObject that = (LocationObject) o;

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
        return result;
    }

    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", referencedColumnName = "location_id", nullable = false, insertable = false, updatable = false)
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    private LocationObjectType locationObjectType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_object_type_id", referencedColumnName = "location_object_type_id", nullable = false, insertable = false, updatable = false)
    public LocationObjectType getLocationObjectType() {
        return locationObjectType;
    }

    public void setLocationObjectType(LocationObjectType locationObjectTypeByLocationObjectTypeId) {
        this.locationObjectType = locationObjectTypeByLocationObjectTypeId;
    }

    private LocationObjectBehaviorType locationObjectBehaviorType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_object_behavior_type_id", referencedColumnName = "location_object_behavior_type_id", nullable = false, insertable = false, updatable = false)
    public LocationObjectBehaviorType getLocationObjectBehaviorType() {
        return locationObjectBehaviorType;
    }

    public void setLocationObjectBehaviorType(LocationObjectBehaviorType locationObjectBehaviorTypeByLocationObjectBehaviorTypeId) {
        this.locationObjectBehaviorType = locationObjectBehaviorTypeByLocationObjectBehaviorTypeId;
    }

    private Set<Player> players;

    @OneToMany(mappedBy = "locationObject", fetch = FetchType.LAZY)
    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }
}
