package arch.galaxyeclipse.server.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 */
@Table(name = "ship_config_bonus_slots", schema = "", catalog = "ge")
@Entity
public class ShipConfigBonusSlots {
    private int shipConfigBonusSlotsId;

    @Column(name = "ship_config_bonus_slots_id")
    @Id
    public int getShipConfigBonusSlotsId() {
        return shipConfigBonusSlotsId;
    }

    public void setShipConfigBonusSlotsId(int shipConfigBonusSlotsId) {
        this.shipConfigBonusSlotsId = shipConfigBonusSlotsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShipConfigBonusSlots that = (ShipConfigBonusSlots) o;

        if (shipConfigBonusSlotsId != that.shipConfigBonusSlotsId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return shipConfigBonusSlotsId;
    }
}
