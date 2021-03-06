package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;
import java.util.Set;

/**
 *
 */
@Table(name = "weapon_type", schema = "", catalog = "ge")
@Entity
public class GeWeaponType {

    private int weaponTypeId;

    @Column(name = "weapon_type_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getWeaponTypeId() {
        return weaponTypeId;
    }

    public void setWeaponTypeId(int weaponTypeId) {
        this.weaponTypeId = weaponTypeId;
    }

    private String weaponTypeName;

    @Column(name = "weapon_type_name", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Basic
    public String getWeaponTypeName() {
        return weaponTypeName;
    }

    public void setWeaponTypeName(String weaponTypeName) {
        this.weaponTypeName = weaponTypeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeWeaponType that = (GeWeaponType) o;

        if (weaponTypeId != that.weaponTypeId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return weaponTypeId;
    }

    private Set<GeWeapon> weapons;

    @OneToMany(mappedBy = "weaponType", fetch = FetchType.LAZY)
    public Set<GeWeapon> getWeapons() {
        return weapons;
    }

    public void setWeapons(Set<GeWeapon> weapons) {
        this.weapons = weapons;
    }
}
