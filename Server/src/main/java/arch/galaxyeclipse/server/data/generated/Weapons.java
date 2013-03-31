package arch.galaxyeclipse.server.data.generated;

// Generated Mar 30, 2013 4:40:47 PM by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Weapons generated by hbm2java
 */
@Entity
@Table(name = "weapons", catalog = "ge")
public class Weapons implements java.io.Serializable {

	private Integer weaponId;
	private Items items;
	private int damage;
	private int delaySpeed;
	private int bulletSpeed;
	private int distance;
	private int energyCost;
	private int weaponTypeId;

	public Weapons() {
	}

	public Weapons(Items items, int damage, int delaySpeed, int bulletSpeed,
			int distance, int energyCost, int weaponTypeId) {
		this.items = items;
		this.damage = damage;
		this.delaySpeed = delaySpeed;
		this.bulletSpeed = bulletSpeed;
		this.distance = distance;
		this.energyCost = energyCost;
		this.weaponTypeId = weaponTypeId;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "weapon_id", unique = true, nullable = false)
	public Integer getWeaponId() {
		return this.weaponId;
	}

	public void setWeaponId(Integer weaponId) {
		this.weaponId = weaponId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	public Items getItems() {
		return this.items;
	}

	public void setItems(Items items) {
		this.items = items;
	}

	@Column(name = "damage", nullable = false)
	public int getDamage() {
		return this.damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	@Column(name = "delay_speed", nullable = false)
	public int getDelaySpeed() {
		return this.delaySpeed;
	}

	public void setDelaySpeed(int delaySpeed) {
		this.delaySpeed = delaySpeed;
	}

	@Column(name = "bullet_speed", nullable = false)
	public int getBulletSpeed() {
		return this.bulletSpeed;
	}

	public void setBulletSpeed(int bulletSpeed) {
		this.bulletSpeed = bulletSpeed;
	}

	@Column(name = "distance", nullable = false)
	public int getDistance() {
		return this.distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	@Column(name = "energy_cost", nullable = false)
	public int getEnergyCost() {
		return this.energyCost;
	}

	public void setEnergyCost(int energyCost) {
		this.energyCost = energyCost;
	}

	@Column(name = "weapon_type_id", nullable = false)
	public int getWeaponTypeId() {
		return this.weaponTypeId;
	}

	public void setWeaponTypeId(int weaponTypeId) {
		this.weaponTypeId = weaponTypeId;
	}

}
