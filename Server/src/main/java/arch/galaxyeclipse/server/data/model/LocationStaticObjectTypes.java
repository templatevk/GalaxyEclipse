package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Table(name = "location_static_object_types", schema = "", catalog = "ge")
@Entity
public class LocationStaticObjectTypes {
    private int locationStaticObjectTypeId;

    @Column(name = "location_static_object_type_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getLocationStaticObjectTypeId() {
        return locationStaticObjectTypeId;
    }

    public void setLocationStaticObjectTypeId(int locationStaticObjectTypeId) {
        this.locationStaticObjectTypeId = locationStaticObjectTypeId;
    }

    private String objectTypeName;

    @Column(name = "object_type_name", nullable = false, insertable = true, updatable = true, length = 16, precision = 0)
    @Basic
    public String getObjectTypeName() {
        return objectTypeName;
    }

    public void setObjectTypeName(String objectTypeName) {
        this.objectTypeName = objectTypeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocationStaticObjectTypes that = (LocationStaticObjectTypes) o;

        if (locationStaticObjectTypeId != that.locationStaticObjectTypeId) return false;
        if (objectTypeName != null ? !objectTypeName.equals(that.objectTypeName) : that.objectTypeName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = locationStaticObjectTypeId;
        result = 31 * result + (objectTypeName != null ? objectTypeName.hashCode() : 0);
        return result;
    }
}
