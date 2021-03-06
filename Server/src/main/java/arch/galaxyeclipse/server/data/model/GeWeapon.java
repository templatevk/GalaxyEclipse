package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Table(name = "weapon", schema = "", catalog = "ge")
@Entity
@DiscriminatorValue(value = GeItem.WEAPON_TYPE)
@PrimaryKeyJoinColumn(name = "item_id")
public class GeWeapon extends GeItem {

    private int damage;

    @Column(name = "damage", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    private int shotDelay;

    @Column(name = "shot_delay", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShotDelay() {
        return shotDelay;
    }

    public void setShotDelay(int shotDelay) {
        this.shotDelay = shotDelay;
    }

    private float bulletSpeed;

    @Column(name = "bullet_speed", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public float getBulletSpeed() {
        return bulletSpeed;
    }

    public void setBulletSpeed(float bulletSpeed) {
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

        GeWeapon weapon = (GeWeapon) o;

        if (itemId != weapon.itemId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return itemId;
    }

    private GeWeaponType weaponType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weapon_type_id", referencedColumnName = "weapon_type_id", nullable = false, insertable = false, updatable = false)
    public GeWeaponType getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(GeWeaponType weaponType) {
        this.weaponType = weaponType;
    }
}
