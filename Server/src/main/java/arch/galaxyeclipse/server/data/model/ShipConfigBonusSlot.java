package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Table(name = "ship_config_bonus_slot", schema = "", catalog = "ge")
@Entity
public class ShipConfigBonusSlot {
    private int shipConfigBonusSlotId;

    @Column(name = "ship_config_bonus_slot_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getShipConfigBonusSlotId() {
        return shipConfigBonusSlotId;
    }

    public void setShipConfigBonusSlotId(int shipConfigBonusSlotId) {
        this.shipConfigBonusSlotId = shipConfigBonusSlotId;
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

        ShipConfigBonusSlot that = (ShipConfigBonusSlot) o;

        if (itemId != that.itemId) return false;
        if (shipConfigBonusSlotId != that.shipConfigBonusSlotId) return false;
        if (shipConfigId != that.shipConfigId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = shipConfigBonusSlotId;
        result = 31 * result + shipConfigId;
        result = 31 * result + itemId;
        return result;
    }

    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", referencedColumnName = "item_id", nullable = false, insertable = false, updatable = false)
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    private ShipConfig shipConfig;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ship_config_id", referencedColumnName = "ship_config_id", nullable = false, insertable = false, updatable = false)
    public ShipConfig getShipConfig() {
        return shipConfig;
    }

    public void setShipConfig(ShipConfig shipConfig) {
        this.shipConfig = shipConfig;
    }
}
