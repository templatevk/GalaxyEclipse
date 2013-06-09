package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 *
 */
@Table(name = "location_object", schema = "", catalog = "ge")
@Entity
public class LocationObject implements Serializable {
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

    private float rotationAngle;

    @Column(name = "rotation_angle", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public float getRotationAngle() {
        return rotationAngle;
    }

    public void setRotationAngle(float rotationAngle) {
        this.rotationAngle = rotationAngle;
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

        LocationObject that = (LocationObject) o;

        if (locationId != that.locationId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return locationObjectId;
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
