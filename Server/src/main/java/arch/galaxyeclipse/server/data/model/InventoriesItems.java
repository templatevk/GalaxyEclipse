package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Table(name = "inventories_items", schema = "", catalog = "ge")
@Entity
public class InventoriesItems {
    private int inventoryItemId;

    @Column(name = "inventory_item_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getInventoryItemId() {
        return inventoryItemId;
    }

    public void setInventoryItemId(int inventoryItemId) {
        this.inventoryItemId = inventoryItemId;
    }

    private int amount;

    @Column(name = "amount", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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

    private int playerId;

    @Column(name = "player_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InventoriesItems that = (InventoriesItems) o;

        if (amount != that.amount) return false;
        if (inventoryItemId != that.inventoryItemId) return false;
        if (itemId != that.itemId) return false;
        if (playerId != that.playerId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = inventoryItemId;
        result = 31 * result + amount;
        result = 31 * result + itemId;
        result = 31 * result + playerId;
        return result;
    }
}
