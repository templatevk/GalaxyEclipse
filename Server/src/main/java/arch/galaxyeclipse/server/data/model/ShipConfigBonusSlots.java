package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Table(name = "ship_config_bonus_slots", schema = "", catalog = "ge")
@Entity
public class ShipConfigBonusSlots {
    private int shipConfigBonusSlotsId;

    @Column(name = "ship_config_bonus_slots_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getShipConfigBonusSlotsId() {
        return shipConfigBonusSlotsId;
    }

    public void setShipConfigBonusSlotsId(int shipConfigBonusSlotsId) {
        this.shipConfigBonusSlotsId = shipConfigBonusSlotsId;
    }

    private int shipConfigId;

    @Column(name = "ship_config_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipConfigId() {
        return shipConfigId;
    }

    public void setShipConfigId(int shipConfigId) {
        this.shipConfigId = shipConfigId;
    }

    private int itemId;

    @Column(name = "item_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShipConfigBonusSlots that = (ShipConfigBonusSlots) o;

        if (itemId != that.itemId) return false;
        if (shipConfigBonusSlotsId != that.shipConfigBonusSlotsId) return false;
        if (shipConfigId != that.shipConfigId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = shipConfigBonusSlotsId;
        result = 31 * result + shipConfigId;
        result = 31 * result + itemId;
        return result;
    }
}
