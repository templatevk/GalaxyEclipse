package arch.galaxyeclipse.server.data.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 */
@Entity
public class Weapons {
    private int weaponId;

    @Column(name = "weapon_id")
    @Id
    public int getWeaponId() {
        return weaponId;
    }

    public void setWeaponId(int weaponId) {
        this.weaponId = weaponId;
    }

    private int damage;

    @Column(name = "damage")
    @Basic
    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    private int delaySpeed;

    @Column(name = "delay_speed")
    @Basic
    public int getDelaySpeed() {
        return delaySpeed;
    }

    public void setDelaySpeed(int delaySpeed) {
        this.delaySpeed = delaySpeed;
    }

    private int bulletSpeed;

    @Column(name = "bullet_speed")
    @Basic
    public int getBulletSpeed() {
        return bulletSpeed;
    }

    public void setBulletSpeed(int bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }

    private int distance;

    @Column(name = "distance")
    @Basic
    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    private int energyCost;

    @Column(name = "energy_cost")
    @Basic
    public int getEnergyCost() {
        return energyCost;
    }

    public void setEnergyCost(int energyCost) {
        this.energyCost = energyCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Weapons weapons = (Weapons) o;

        if (bulletSpeed != weapons.bulletSpeed) return false;
        if (damage != weapons.damage) return false;
        if (delaySpeed != weapons.delaySpeed) return false;
        if (distance != weapons.distance) return false;
        if (energyCost != weapons.energyCost) return false;
        if (weaponId != weapons.weaponId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = weaponId;
        result = 31 * result + damage;
        result = 31 * result + delaySpeed;
        result = 31 * result + bulletSpeed;
        result = 31 * result + distance;
        result = 31 * result + energyCost;
        return result;
    }
}
