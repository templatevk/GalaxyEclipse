package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;
import java.util.Set;

/**
 *
 */
@Table(name = "ship_config", schema = "", catalog = "ge")
@Entity
public class GeShipConfig {
    private int shipConfigId;

    @Column(name = "ship_config_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getShipConfigId() {
        return shipConfigId;
    }

    public void setShipConfigId(int shipConfigId) {
        this.shipConfigId = shipConfigId;
    }

    private float shipConfigMoveMaxSpeed;

    @Column(name = "ship_config_move_max_speed", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public float getShipConfigMoveMaxSpeed() {
        return shipConfigMoveMaxSpeed;
    }

    public void setShipConfigMoveMaxSpeed(float shipConfigMoveMaxSpeed) {
        this.shipConfigMoveMaxSpeed = shipConfigMoveMaxSpeed;
    }

    private float shipConfigRotationMaxSpeed;

    @Column(name = "ship_config_rotation_max_speed", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public float getShipConfigRotationMaxSpeed() {
        return shipConfigRotationMaxSpeed;
    }

    public void setShipConfigRotationMaxSpeed(float shipConfigRotationMaxSpeed) {
        this.shipConfigRotationMaxSpeed = shipConfigRotationMaxSpeed;
    }

    private float shipConfigMoveAcceleration;

    @Column(name = "ship_config_move_acceleration", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public float getShipConfigMoveAcceleration() {
        return shipConfigMoveAcceleration;
    }

    public void setShipConfigMoveAcceleration(float shipConfigMoveAcceleration) {
        this.shipConfigMoveAcceleration = shipConfigMoveAcceleration;
    }

    private float shipConfigRotationAcceleration;

    @Column(name = "ship_config_rotation_acceleration", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public float getShipConfigRotationAcceleration() {
        return shipConfigRotationAcceleration;
    }

    public void setShipConfigRotationAcceleration(float shipConfigRotationAcceleration) {
        this.shipConfigRotationAcceleration = shipConfigRotationAcceleration;
    }

    private int shipConfigArmor;

    @Column(name = "ship_config_armor", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipConfigArmor() {
        return shipConfigArmor;
    }

    public void setShipConfigArmor(int shipConfigArmor) {
        this.shipConfigArmor = shipConfigArmor;
    }

    private int shipConfigEnergyMax;

    @Column(name = "ship_config_energy_max", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipConfigEnergyMax() {
        return shipConfigEnergyMax;
    }

    public void setShipConfigEnergyMax(int shipConfigEnergyMax) {
        this.shipConfigEnergyMax = shipConfigEnergyMax;
    }

    private int shipConfigHpMax;

    @Column(name = "ship_config_hp_max", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipConfigHpMax() {
        return shipConfigHpMax;
    }

    public void setShipConfigHpMax(int shipConfigHpMax) {
        this.shipConfigHpMax = shipConfigHpMax;
    }

    private int shipConfigEnergyRegen;

    @Column(name = "ship_config_energy_regen", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipConfigEnergyRegen() {
        return shipConfigEnergyRegen;
    }

    public void setShipConfigEnergyRegen(int shipConfigEnergyRegen) {
        this.shipConfigEnergyRegen = shipConfigEnergyRegen;
    }

    private int shipConfigHpRegen;

    @Column(name = "ship_config_hp_regen", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipConfigHpRegen() {
        return shipConfigHpRegen;
    }

    public void setShipConfigHpRegen(int shipConfigHpRegen) {
        this.shipConfigHpRegen = shipConfigHpRegen;
    }

    private int shipTypeId;

    @Column(name = "ship_type_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipTypeId() {
        return shipTypeId;
    }

    public void setShipTypeId(int shipTypeId) {
        this.shipTypeId = shipTypeId;
    }

    private int engineItemId;

    @Column(name = "engine_item_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getEngineItemId() {
        return engineItemId;
    }

    public void setEngineItemId(int engineItemId) {
        this.engineItemId = engineItemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeShipConfig that = (GeShipConfig) o;

        if (shipConfigId != that.shipConfigId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return shipConfigId;
    }

    private GeItem item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "engine_item_id", referencedColumnName = "item_id", nullable = false, insertable = false, updatable = false)
    public GeItem getItem() {
        return item;
    }

    public void setItem(GeItem item) {
        this.item = item;
    }

    private GeShipType shipType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ship_type_id", referencedColumnName = "ship_type_id", nullable = false, insertable = false, updatable = false)
    public GeShipType getShipType() {
        return shipType;
    }

    public void setShipType(GeShipType shipType) {
        this.shipType = shipType;
    }

    private Set<GeShipConfigBonusSlot> shipConfigBonusSlots;

    @OneToMany(mappedBy = "shipConfig", fetch = FetchType.LAZY)
    public Set<GeShipConfigBonusSlot> getShipConfigBonusSlots() {
        return shipConfigBonusSlots;
    }

    public void setShipConfigBonusSlots(Set<GeShipConfigBonusSlot> shipConfigBonusSlots) {
        this.shipConfigBonusSlots = shipConfigBonusSlots;
    }

    private Set<GeShipConfigWeaponSlot> shipConfigWeaponSlots;

    @OneToMany(mappedBy = "shipConfig", fetch = FetchType.LAZY)
    public Set<GeShipConfigWeaponSlot> getShipConfigWeaponSlots() {
        return shipConfigWeaponSlots;
    }

    public void setShipConfigWeaponSlots(Set<GeShipConfigWeaponSlot> shipConfigWeaponSlots) {
        this.shipConfigWeaponSlots = shipConfigWeaponSlots;
    }

    private GeEngine engine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "engine_item_id", referencedColumnName = "item_id", updatable = false, insertable = false, nullable = false)
    public GeEngine getEngine() {
        return engine;
    }

    public void setEngine(GeEngine engine) {
        this.engine = engine;
    }

    private Set<GePlayer> players;

    @OneToMany(mappedBy = "shipConfig", fetch = FetchType.LAZY)
    public Set<GePlayer> getPlayers() {
        return players;
    }

    public void setPlayers(Set<GePlayer> players) {
        this.players = players;
    }
}
