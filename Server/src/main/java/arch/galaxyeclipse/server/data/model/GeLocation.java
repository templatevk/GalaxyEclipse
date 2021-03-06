package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;
import java.util.Set;

/**
 *
 */
@Table(name = "location", schema = "", catalog = "ge")
@Entity
public class GeLocation {

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

    private int locationWidth;

    @Column(name = "location_width", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getLocationWidth() {
        return locationWidth;
    }

    public void setLocationWidth(int locationWidth) {
        this.locationWidth = locationWidth;
    }

    private int locationHeight;

    @Column(name = "location_height", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getLocationHeight() {
        return locationHeight;
    }

    public void setLocationHeight(int locationHeight) {
        this.locationHeight = locationHeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeLocation location = (GeLocation) o;

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

    private Set<GeLocationObject> location;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    public Set<GeLocationObject> getLocation() {
        return location;
    }

    public void setLocation(Set<GeLocationObject> location) {
        this.location = location;
    }
}
