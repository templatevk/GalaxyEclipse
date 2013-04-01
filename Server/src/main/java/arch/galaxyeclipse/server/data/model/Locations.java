package arch.galaxyeclipse.server.data.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 */
@Entity
public class Locations {
    private int locationId;

    @Column(name = "location_id")
    @Id
    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    private String locationName;

    @Column(name = "location_name")
    @Basic
    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    private int locationWidth;

    @Column(name = "location_width")
    @Basic
    public int getLocationWidth() {
        return locationWidth;
    }

    public void setLocationWidth(int locationWidth) {
        this.locationWidth = locationWidth;
    }

    private int locationHeight;

    @Column(name = "location_height")
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

        Locations locations = (Locations) o;

        if (locationHeight != locations.locationHeight) return false;
        if (locationId != locations.locationId) return false;
        if (locationWidth != locations.locationWidth) return false;
        if (locationName != null ? !locationName.equals(locations.locationName) : locations.locationName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = locationId;
        result = 31 * result + (locationName != null ? locationName.hashCode() : 0);
        result = 31 * result + locationWidth;
        result = 31 * result + locationHeight;
        return result;
    }
}
