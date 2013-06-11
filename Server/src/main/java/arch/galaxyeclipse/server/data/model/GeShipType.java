package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;
import java.util.Set;

/**
 *
 */
@Table(name = "ship_type", schema = "", catalog = "ge")
@Entity
public class GeShipType {
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

    private float shipTypeMoveMaxSpeed;

    @Column(name = "ship_type_move_max_speed", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public float getShipTypeMoveMaxSpeed() {
        return shipTypeMoveMaxSpeed;
    }

    public void setShipTypeMoveMaxSpeed(float shipTypeMoveMaxSpeed) {
        this.shipTypeMoveMaxSpeed = shipTypeMoveMaxSpeed;
    }

    private float shipTypeMoveAcceleration;

    @Column(name = "ship_type_move_acceleration", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public float getShipTypeMoveAcceleration() {
        return shipTypeMoveAcceleration;
    }

    public void setShipTypeMoveAcceleration(float shipTypeMoveAcceleration) {
        this.shipTypeMoveAcceleration = shipTypeMoveAcceleration;
    }

    private float shipTypeRotationMaxSpeed;

    @Column(name = "ship_type_rotation_max_speed", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public float getShipTypeRotationMaxSpeed() {
        return shipTypeRotationMaxSpeed;
    }

    public void setShipTypeRotationMaxSpeed(float shipTypeRotationMaxSpeed) {
        this.shipTypeRotationMaxSpeed = shipTypeRotationMaxSpeed;
    }

    private float shipTypeRotationAcceleration;

    @Column(name = "ship_type_rotation_acceleration", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public float getShipTypeRotationAcceleration() {
        return shipTypeRotationAcceleration;
    }

    public void setShipTypeRotationAcceleration(float shipTypeRotationAcceleration) {
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

        GeShipType shipType = (GeShipType) o;

        if (shipTypeId != shipType.shipTypeId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return shipTypeId;
    }

    private Set<GeShipConfig> shipConfigs;

    @OneToMany(mappedBy = "shipType", fetch = FetchType.LAZY)
    public Set<GeShipConfig> getShipConfigs() {
        return shipConfigs;
    }

    public void setShipConfigs(Set<GeShipConfig> shipConfigs) {
        this.shipConfigs = shipConfigs;
    }
}
