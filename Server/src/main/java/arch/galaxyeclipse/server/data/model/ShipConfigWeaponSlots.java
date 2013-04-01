package arch.galaxyeclipse.server.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 */
@Table(name = "ship_config_weapon_slots", schema = "", catalog = "ge")
@Entity
public class ShipConfigWeaponSlots {
    private int shipConfigWeaponSlotsId;

    @Column(name = "ship_config_weapon_slots_id")
    @Id
    public int getShipConfigWeaponSlotsId() {
        return shipConfigWeaponSlotsId;
    }

    public void setShipConfigWeaponSlotsId(int shipConfigWeaponSlotsId) {
        this.shipConfigWeaponSlotsId = shipConfigWeaponSlotsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShipConfigWeaponSlots that = (ShipConfigWeaponSlots) o;

        if (shipConfigWeaponSlotsId != that.shipConfigWeaponSlotsId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return shipConfigWeaponSlotsId;
    }
}
