package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;
import java.util.Set;

/**
 *
 */
@Table(name = "item_type", schema = "", catalog = "ge")
@Entity
public class ItemType {
    private int itemTypeId;

    @Column(name = "item_type_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(int itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    private String itemTypeName;

    @Column(name = "item_type_name", nullable = false, insertable = true, updatable = true, length = 16, precision = 0)
    @Basic
    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemType itemType = (ItemType) o;

        if (itemTypeId != itemType.itemTypeId) return false;
        if (itemTypeName != null ? !itemTypeName.equals(itemType.itemTypeName) : itemType.itemTypeName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = itemTypeId;
        result = 31 * result + (itemTypeName != null ? itemTypeName.hashCode() : 0);
        return result;
    }

    private Set<Item> items;

    @OneToMany(mappedBy = "itemType", fetch = FetchType.LAZY)
    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }
}
