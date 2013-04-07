package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Table(name = "location_object_behavior_types", schema = "", catalog = "ge")
@Entity
public class LocationObjectBehaviorTypes {
    private int locationObjectBehaviorTypeId;

    @Column(name = "location_object_behavior_type_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getLocationObjectBehaviorTypeId() {
        return locationObjectBehaviorTypeId;
    }

    public void setLocationObjectBehaviorTypeId(int locationObjectBehaviorTypeId) {
        this.locationObjectBehaviorTypeId = locationObjectBehaviorTypeId;
    }

    private String objectBehaviorTypeName;

    @Column(name = "object_behavior_type_name", nullable = false, insertable = true, updatable = true, length = 16, precision = 0)
    @Basic
    public String getObjectBehaviorTypeName() {
        return objectBehaviorTypeName;
    }

    public void setObjectBehaviorTypeName(String objectBehaviorTypeName) {
        this.objectBehaviorTypeName = objectBehaviorTypeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocationObjectBehaviorTypes that = (LocationObjectBehaviorTypes) o;

        if (locationObjectBehaviorTypeId != that.locationObjectBehaviorTypeId) return false;
        if (objectBehaviorTypeName != null ? !objectBehaviorTypeName.equals(that.objectBehaviorTypeName) : that.objectBehaviorTypeName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = locationObjectBehaviorTypeId;
        result = 31 * result + (objectBehaviorTypeName != null ? objectBehaviorTypeName.hashCode() : 0);
        return result;
    }
}
