package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Table(name = "location_dynamic_object_types", schema = "", catalog = "ge")
@Entity
public class LocationDynamicObjectTypes {
    private int locationDynamicObjectTypeId;

    @Column(name = "location_dynamic_object_type_id")
    @Id
    public int getLocationDynamicObjectTypeId() {
        return locationDynamicObjectTypeId;
    }

    public void setLocationDynamicObjectTypeId(int locationDynamicObjectTypeId) {
        this.locationDynamicObjectTypeId = locationDynamicObjectTypeId;
    }

    private String objectTypeName;

    @Column(name = "object_type_name")
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

        LocationDynamicObjectTypes that = (LocationDynamicObjectTypes) o;

        if (locationDynamicObjectTypeId != that.locationDynamicObjectTypeId) return false;
        if (objectTypeName != null ? !objectTypeName.equals(that.objectTypeName) : that.objectTypeName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = locationDynamicObjectTypeId;
        result = 31 * result + (objectTypeName != null ? objectTypeName.hashCode() : 0);
        return result;
    }
}
