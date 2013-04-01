package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Table(name = "inventories_items", schema = "", catalog = "ge")
@Entity
public class InventoriesItems {
    private int inventoryItemId;

    @Column(name = "inventory_item_id")
    @Id
    public int getInventoryItemId() {
        return inventoryItemId;
    }

    public void setInventoryItemId(int inventoryItemId) {
        this.inventoryItemId = inventoryItemId;
    }

    private int amount;

    @Column(name = "amount")
    @Basic
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InventoriesItems that = (InventoriesItems) o;

        if (amount != that.amount) return false;
        if (inventoryItemId != that.inventoryItemId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = inventoryItemId;
        result = 31 * result + amount;
        return result;
    }
}
