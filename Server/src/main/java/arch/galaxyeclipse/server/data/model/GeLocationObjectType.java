package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;
import java.util.Set;

/**
 *
 */
@Table(name = "location_object_type", schema = "", catalog = "ge")
@Entity
public class GeLocationObjectType {
    private int locationObjectTypeId;

    @Column(name = "location_object_type_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getLocationObjectTypeId() {
        return locationObjectTypeId;
    }

    public void setLocationObjectTypeId(int locationObjectTypeId) {
        this.locationObjectTypeId = locationObjectTypeId;
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

        GeLocationObjectType that = (GeLocationObjectType) o;

        if (locationObjectTypeId != that.locationObjectTypeId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return locationObjectTypeId;
    }

    private Set<GeLocationObject> locationObjects;

    @OneToMany(mappedBy = "locationObjectType", fetch = FetchType.LAZY)
    public Set<GeLocationObject> getLocationObjects() {
        return locationObjects;
    }

    public void setLocationObjects(Set<GeLocationObject> locationObjects) {
        this.locationObjects = locationObjects;
    }
}
