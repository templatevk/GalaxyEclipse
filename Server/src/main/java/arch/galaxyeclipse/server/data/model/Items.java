package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Entity
public class Items {
    private int itemId;

    @Column(name = "item_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    private String itemName;

    @Column(name = "item_name", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Basic
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    private String itemDescription;

    @Column(name = "item_description", nullable = false, insertable = true, updatable = true, length = 64, precision = 0)
    @Basic
    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    private int itemPrice;

    @Column(name = "item_price", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    private int itemTypeId;

    @Column(name = "item_type_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(int itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Items items = (Items) o;

        if (itemId != items.itemId) return false;
        if (itemPrice != items.itemPrice) return false;
        if (itemTypeId != items.itemTypeId) return false;
        if (itemDescription != null ? !itemDescription.equals(items.itemDescription) : items.itemDescription != null)
            return false;
        if (itemName != null ? !itemName.equals(items.itemName) : items.itemName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = itemId;
        result = 31 * result + (itemName != null ? itemName.hashCode() : 0);
        result = 31 * result + (itemDescription != null ? itemDescription.hashCode() : 0);
        result = 31 * result + itemPrice;
        result = 31 * result + itemTypeId;
        return result;
    }
}
