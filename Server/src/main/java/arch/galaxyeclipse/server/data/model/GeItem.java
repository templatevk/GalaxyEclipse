package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;
import java.util.Set;

/**
 *
 */
@Table(name = "item", schema = "", catalog = "ge")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "item_type_id", discriminatorType = DiscriminatorType.INTEGER)
public class GeItem {

    @Transient
    public static final String ENGINE_TYPE = "1";
    @Transient
    public static final String WEAPON_TYPE = "2";
    @Transient
    public static final String BONUS_TYPE = "3";

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

        GeItem item = (GeItem) o;

        if (itemId != item.itemId) return false;
        if (itemPrice != item.itemPrice) return false;
        if (itemTypeId != item.itemTypeId) return false;
        if (itemDescription != null ? !itemDescription.equals(item.itemDescription) : item.itemDescription != null)
            return false;
        if (itemName != null ? !itemName.equals(item.itemName) : item.itemName != null) return false;

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

    private Set<GeInventoryItem> inventoryItems;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    public Set<GeInventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    public void setInventoryItems(Set<GeInventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    private GeItemType itemType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_type_id", referencedColumnName = "item_type_id", nullable = false, insertable = false, updatable = false)
    public GeItemType getItemType() {
        return itemType;
    }

    public void setItemType(GeItemType itemType) {
        this.itemType = itemType;
    }

    private Set<GeShipConfig> shipConfigs;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    public Set<GeShipConfig> getShipConfigs() {
        return shipConfigs;
    }

    public void setShipConfigs(Set<GeShipConfig> shipConfigs) {
        this.shipConfigs = shipConfigs;
    }

    private Set<GeShipConfigBonusSlot> shipConfigBonusSlots;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    public Set<GeShipConfigBonusSlot> getShipConfigBonusSlots() {
        return shipConfigBonusSlots;
    }

    public void setShipConfigBonusSlots(Set<GeShipConfigBonusSlot> shipConfigBonusSlots) {
        this.shipConfigBonusSlots = shipConfigBonusSlots;
    }

    private Set<GeShipConfigWeaponSlot> shipConfigWeaponSlots;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    public Set<GeShipConfigWeaponSlot> getShipConfigWeaponSlots() {
        return shipConfigWeaponSlots;
    }

    public void setShipConfigWeaponSlots(Set<GeShipConfigWeaponSlot> shipConfigWeaponSlots) {
        this.shipConfigWeaponSlots = shipConfigWeaponSlots;
    }
}
