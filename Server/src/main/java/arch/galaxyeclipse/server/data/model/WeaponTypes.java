package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Table(name = "weapon_types", schema = "", catalog = "ge")
@Entity
public class WeaponTypes {
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

    @Column(name = "weapon_type_name", nullable = false, insertable = true, updatable = true, length = 16, precision = 0)
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

        WeaponTypes that = (WeaponTypes) o;

        if (weaponTypeId != that.weaponTypeId) return false;
        if (weaponTypeName != null ? !weaponTypeName.equals(that.weaponTypeName) : that.weaponTypeName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = weaponTypeId;
        result = 31 * result + (weaponTypeName != null ? weaponTypeName.hashCode() : 0);
        return result;
    }
}
