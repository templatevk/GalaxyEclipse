package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;
import java.util.Set;

/**
 *
 */
@Table(name = "location", schema = "", catalog = "ge")
@Entity
public class Location {
    private int locationId;

    @Column(name = "location_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    private String locationName;

    @Column(name = "location_name", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Basic
    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    private float locationWidth;

    @Column(name = "location_width", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public float getLocationWidth() {
        return locationWidth;
    }

    public void setLocationWidth(float locationWidth) {
        this.locationWidth = locationWidth;
    }

    private float locationHeight;

    @Column(name = "location_height", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public float getLocationHeight() {
        return locationHeight;
    }

    public void setLocationHeight(float locationHeight) {
        this.locationHeight = locationHeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (locationHeight != location.locationHeight) return false;
        if (locationId != location.locationId) return false;
        if (locationWidth != location.locationWidth) return false;
        if (locationName != null ? !locationName.equals(location.locationName) : location.locationName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = locationId;
        result = 31 * result + (locationName != null ? locationName.hashCode() : 0);
        return result;
    }

    private Set<LocationObject> location;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    public Set<LocationObject> getLocation() {
        return location;
    }

    public void setLocation(Set<LocationObject> location) {
        this.location = location;
    }
}
