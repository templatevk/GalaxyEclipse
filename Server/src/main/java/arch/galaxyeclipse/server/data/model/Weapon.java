package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Table(name = "weapon", schema = "", catalog = "ge")
@Entity
@DiscriminatorValue(value = Item.WEAPON_TYPE)
@PrimaryKeyJoinColumn(name = "item_id")
public class Weapon extends Item {
    private int damage;

    @Column(name = "damage", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    private int delaySpeed;

    @Column(name = "delay_speed", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getDelaySpeed() {
        return delaySpeed;
    }

    public void setDelaySpeed(int delaySpeed) {
        this.delaySpeed = delaySpeed;
    }

    private int bulletSpeed;

    @Column(name = "bullet_speed", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getBulletSpeed() {
        return bulletSpeed;
    }

    public void setBulletSpeed(int bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }

    private int maxDistance;

    @Column(name = "max_distance", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    private int energyCost;

    @Column(name = "energy_cost", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getEnergyCost() {
        return energyCost;
    }

    public void setEnergyCost(int energyCost) {
        this.energyCost = energyCost;
    }

    private int weaponTypeId;

    @Column(name = "weapon_type_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getWeaponTypeId() {
        return weaponTypeId;
    }

    public void setWeaponTypeId(int weaponTypeId) {
        this.weaponTypeId = weaponTypeId;
    }

    private int itemId;

    @Column(name = "item_id", nullable = false, insertable = false, updatable = false, length = 10, precision = 0)
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

        Weapon weapon = (Weapon) o;

        if (bulletSpeed != weapon.bulletSpeed) return false;
        if (damage != weapon.damage) return false;
        if (delaySpeed != weapon.delaySpeed) return false;
        if (energyCost != weapon.energyCost) return false;
        if (itemId != weapon.itemId) return false;
        if (maxDistance != weapon.maxDistance) return false;
        if (weaponTypeId != weapon.weaponTypeId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = itemId;
        result = 31 * result + damage;
        result = 31 * result + delaySpeed;
        result = 31 * result + bulletSpeed;
        result = 31 * result + maxDistance;
        result = 31 * result + energyCost;
        result = 31 * result + weaponTypeId;
        result = 31 * result + itemId;
        return result;
    }

    private WeaponType weaponType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weapon_type_id", referencedColumnName = "weapon_type_id", nullable = false, insertable = false, updatable = false)
    public WeaponType getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }
}
