package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;
import java.util.*;

/**
 *
 */
@Table(name = "ship_type", schema = "", catalog = "ge")
@Entity
public class ShipType {
    private int shipTypeId;

    @Column(name = "ship_type_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getShipTypeId() {
        return shipTypeId;
    }

    public void setShipTypeId(int shipTypeId) {
        this.shipTypeId = shipTypeId;
    }

    private String shipTypeName;

    @Column(name = "ship_type_name", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Basic
    public String getShipTypeName() {
        return shipTypeName;
    }

    public void setShipTypeName(String shipTypeName) {
        this.shipTypeName = shipTypeName;
    }

    private int shipTypeArmor;

    @Column(name = "ship_type_armor", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipTypeArmor() {
        return shipTypeArmor;
    }

    public void setShipTypeArmor(int shipTypeArmor) {
        this.shipTypeArmor = shipTypeArmor;
    }

    private int shipTypeArmorDurability;

    @Column(name = "ship_type_armor_durability", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipTypeArmorDurability() {
        return shipTypeArmorDurability;
    }

    public void setShipTypeArmorDurability(int shipTypeArmorDurability) {
        this.shipTypeArmorDurability = shipTypeArmorDurability;
    }

    private int shipTypeEnergyMax;

    @Column(name = "ship_type_energy_max", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipTypeEnergyMax() {
        return shipTypeEnergyMax;
    }

    public void setShipTypeEnergyMax(int shipTypeEnergyMax) {
        this.shipTypeEnergyMax = shipTypeEnergyMax;
    }

    private int shipTypeHpMax;

    @Column(name = "ship_type_hp_max", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipTypeHpMax() {
        return shipTypeHpMax;
    }

    public void setShipTypeHpMax(int shipTypeHpMax) {
        this.shipTypeHpMax = shipTypeHpMax;
    }

    private int shipTypeEnergyRegen;

    @Column(name = "ship_type_energy_regen", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipTypeEnergyRegen() {
        return shipTypeEnergyRegen;
    }

    public void setShipTypeEnergyRegen(int shipTypeEnergyRegen) {
        this.shipTypeEnergyRegen = shipTypeEnergyRegen;
    }

    private int shipTypeHpRegen;

    @Column(name = "ship_type_hp_regen", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipTypeHpRegen() {
        return shipTypeHpRegen;
    }

    public void setShipTypeHpRegen(int shipTypeHpRegen) {
        this.shipTypeHpRegen = shipTypeHpRegen;
    }

    private int shipTypeMoveMaxSpeed;

    @Column(name = "ship_type_move_max_speed", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipTypeMoveMaxSpeed() {
        return shipTypeMoveMaxSpeed;
    }

    public void setShipTypeMoveMaxSpeed(int shipTypeMoveMaxSpeed) {
        this.shipTypeMoveMaxSpeed = shipTypeMoveMaxSpeed;
    }

    private int shipTypeMoveAcceleration;

    @Column(name = "ship_type_move_acceleration", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipTypeMoveAcceleration() {
        return shipTypeMoveAcceleration;
    }

    public void setShipTypeMoveAcceleration(int shipTypeMoveAcceleration) {
        this.shipTypeMoveAcceleration = shipTypeMoveAcceleration;
    }

    private int shipTypeRotationMaxSpeed;

    @Column(name = "ship_type_rotation_max_speed", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipTypeRotationMaxSpeed() {
        return shipTypeRotationMaxSpeed;
    }

    public void setShipTypeRotationMaxSpeed(int shipTypeRotationMaxSpeed) {
        this.shipTypeRotationMaxSpeed = shipTypeRotationMaxSpeed;
    }

    private int shipTypeRotationAcceleration;

    @Column(name = "ship_type_rotation_acceleration", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipTypeRotationAcceleration() {
        return shipTypeRotationAcceleration;
    }

    public void setShipTypeRotationAcceleration(int shipTypeRotationAcceleration) {
        this.shipTypeRotationAcceleration = shipTypeRotationAcceleration;
    }

    private int weaponSlotsCount;

    @Column(name = "weapon_slots_count", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getWeaponSlotsCount() {
        return weaponSlotsCount;
    }

    public void setWeaponSlotsCount(int weaponSlotsCount) {
        this.weaponSlotsCount = weaponSlotsCount;
    }

    private int bonusSlotsCount;

    @Column(name = "bonus_slots_count", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getBonusSlotsCount() {
        return bonusSlotsCount;
    }

    public void setBonusSlotsCount(int bonusSlotsCount) {
        this.bonusSlotsCount = bonusSlotsCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShipType shipType = (ShipType) o;

        if (bonusSlotsCount != shipType.bonusSlotsCount) return false;
        if (shipTypeArmor != shipType.shipTypeArmor) return false;
        if (shipTypeArmorDurability != shipType.shipTypeArmorDurability) return false;
        if (shipTypeEnergyMax != shipType.shipTypeEnergyMax) return false;
        if (shipTypeEnergyRegen != shipType.shipTypeEnergyRegen) return false;
        if (shipTypeHpMax != shipType.shipTypeHpMax) return false;
        if (shipTypeHpRegen != shipType.shipTypeHpRegen) return false;
        if (shipTypeId != shipType.shipTypeId) return false;
        if (shipTypeMoveAcceleration != shipType.shipTypeMoveAcceleration) return false;
        if (shipTypeMoveMaxSpeed != shipType.shipTypeMoveMaxSpeed) return false;
        if (shipTypeRotationAcceleration != shipType.shipTypeRotationAcceleration) return false;
        if (shipTypeRotationMaxSpeed != shipType.shipTypeRotationMaxSpeed) return false;
        if (weaponSlotsCount != shipType.weaponSlotsCount) return false;
        if (shipTypeName != null ? !shipTypeName.equals(shipType.shipTypeName) : shipType.shipTypeName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = shipTypeId;
        result = 31 * result + (shipTypeName != null ? shipTypeName.hashCode() : 0);
        result = 31 * result + shipTypeArmor;
        result = 31 * result + shipTypeArmorDurability;
        result = 31 * result + shipTypeEnergyMax;
        result = 31 * result + shipTypeHpMax;
        result = 31 * result + shipTypeEnergyRegen;
        result = 31 * result + shipTypeHpRegen;
        result = 31 * result + shipTypeMoveMaxSpeed;
        result = 31 * result + shipTypeMoveAcceleration;
        result = 31 * result + shipTypeRotationMaxSpeed;
        result = 31 * result + shipTypeRotationAcceleration;
        result = 31 * result + weaponSlotsCount;
        result = 31 * result + bonusSlotsCount;
        return result;
    }

    private Set<ShipConfig> shipConfigs;

    @OneToMany(mappedBy = "shipType", fetch = FetchType.LAZY)
    public Set<ShipConfig> getShipConfigs() {
        return shipConfigs;
    }

    public void setShipConfigs(Set<ShipConfig> shipConfigs) {
        this.shipConfigs = shipConfigs;
    }
}
