package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Table(name = "item_types", schema = "", catalog = "ge")
@Entity
public class ItemTypes {
    private int itemTypeId;

    @Column(name = "item_type_id")
    @Id
    public int getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(int itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    private String name;

    @Column(name = "name")
    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemTypes itemTypes = (ItemTypes) o;

        if (itemTypeId != itemTypes.itemTypeId) return false;
        if (name != null ? !name.equals(itemTypes.name) : itemTypes.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = itemTypeId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
