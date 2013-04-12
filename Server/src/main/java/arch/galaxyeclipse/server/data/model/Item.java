package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;
import java.util.*;

/**
 *
 */
@Table(name = "item", schema = "", catalog = "ge")
@Entity
public class Item {
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

        Item item = (Item) o;

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

    private Set<Bonus> bonuses;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    public Set<Bonus> getBonuses() {
        return bonuses;
    }

    public void setBonuses(Set<Bonus> bonuses) {
        this.bonuses = bonuses;
    }

    private Set<Engine> engines;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    public Set<Engine> getEngines() {
        return engines;
    }

    public void setEngines(Set<Engine> engines) {
        this.engines = engines;
    }

    private Set<InventoryItem> inventoryItems;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    public Set<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    public void setInventoryItems(Set<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    private ItemType itemType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_type_id", referencedColumnName = "item_type_id", nullable = false, insertable = false, updatable = false)
    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    private Set<ShipConfig> shipConfigs;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    public Set<ShipConfig> getShipConfigs() {
        return shipConfigs;
    }

    public void setShipConfigs(Set<ShipConfig> shipConfigs) {
        this.shipConfigs = shipConfigs;
    }

    private Set<ShipConfigBonusSlot> shipConfigBonusSlots;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    public Set<ShipConfigBonusSlot> getShipConfigBonusSlots() {
        return shipConfigBonusSlots;
    }

    public void setShipConfigBonusSlots(Set<ShipConfigBonusSlot> shipConfigBonusSlots) {
        this.shipConfigBonusSlots = shipConfigBonusSlots;
    }

    private Set<ShipConfigWeaponSlot> shipConfigWeaponSlots;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    public Set<ShipConfigWeaponSlot> getShipConfigWeaponSlots() {
        return shipConfigWeaponSlots;
    }

    public void setShipConfigWeaponSlots(Set<ShipConfigWeaponSlot> shipConfigWeaponSlots) {
        this.shipConfigWeaponSlots = shipConfigWeaponSlots;
    }

    private Set<Weapon> weapons;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    public Set<Weapon> getWeapons() {
        return weapons;
    }

    public void setWeapons(Set<Weapon> weapons) {
        this.weapons = weapons;
    }
}
