package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Table(name = "ship_configs", schema = "", catalog = "ge")
@Entity
public class ShipConfigs {
    private int shipConfigId;

    @Column(name = "ship_config_id")
    @Id
    public int getShipConfigId() {
        return shipConfigId;
    }

    public void setShipConfigId(int shipConfigId) {
        this.shipConfigId = shipConfigId;
    }

    private int shipConfigMoveMaxSpeed;

    @Column(name = "ship_config_move_max_speed")
    @Basic
    public int getShipConfigMoveMaxSpeed() {
        return shipConfigMoveMaxSpeed;
    }

    public void setShipConfigMoveMaxSpeed(int shipConfigMoveMaxSpeed) {
        this.shipConfigMoveMaxSpeed = shipConfigMoveMaxSpeed;
    }

    private int shipConfigRotationMaxSpeed;

    @Column(name = "ship_config_rotation_max_speed")
    @Basic
    public int getShipConfigRotationMaxSpeed() {
        return shipConfigRotationMaxSpeed;
    }

    public void setShipConfigRotationMaxSpeed(int shipConfigRotationMaxSpeed) {
        this.shipConfigRotationMaxSpeed = shipConfigRotationMaxSpeed;
    }

    private int shipConfigMoveAcceleration;

    @Column(name = "ship_config_move_acceleration")
    @Basic
    public int getShipConfigMoveAcceleration() {
        return shipConfigMoveAcceleration;
    }

    public void setShipConfigMoveAcceleration(int shipConfigMoveAcceleration) {
        this.shipConfigMoveAcceleration = shipConfigMoveAcceleration;
    }

    private int shipConfigRotationAcceleration;

    @Column(name = "ship_config_rotation_acceleration")
    @Basic
    public int getShipConfigRotationAcceleration() {
        return shipConfigRotationAcceleration;
    }

    public void setShipConfigRotationAcceleration(int shipConfigRotationAcceleration) {
        this.shipConfigRotationAcceleration = shipConfigRotationAcceleration;
    }

    private int shipConfigArmor;

    @Column(name = "ship_config_armor")
    @Basic
    public int getShipConfigArmor() {
        return shipConfigArmor;
    }

    public void setShipConfigArmor(int shipConfigArmor) {
        this.shipConfigArmor = shipConfigArmor;
    }

    private int shipConfigEnergyMax;

    @Column(name = "ship_config_energy_max")
    @Basic
    public int getShipConfigEnergyMax() {
        return shipConfigEnergyMax;
    }

    public void setShipConfigEnergyMax(int shipConfigEnergyMax) {
        this.shipConfigEnergyMax = shipConfigEnergyMax;
    }

    private int shipConfigHpMax;

    @Column(name = "ship_config_hp_max")
    @Basic
    public int getShipConfigHpMax() {
        return shipConfigHpMax;
    }

    public void setShipConfigHpMax(int shipConfigHpMax) {
        this.shipConfigHpMax = shipConfigHpMax;
    }

    private int shipConfigEnergyRegen;

    @Column(name = "ship_config_energy_regen")
    @Basic
    public int getShipConfigEnergyRegen() {
        return shipConfigEnergyRegen;
    }

    public void setShipConfigEnergyRegen(int shipConfigEnergyRegen) {
        this.shipConfigEnergyRegen = shipConfigEnergyRegen;
    }

    private int shipConfigHpRegen;

    @Column(name = "ship_config_hp_regen")
    @Basic
    public int getShipConfigHpRegen() {
        return shipConfigHpRegen;
    }

    public void setShipConfigHpRegen(int shipConfigHpRegen) {
        this.shipConfigHpRegen = shipConfigHpRegen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShipConfigs that = (ShipConfigs) o;

        if (shipConfigArmor != that.shipConfigArmor) return false;
        if (shipConfigEnergyMax != that.shipConfigEnergyMax) return false;
        if (shipConfigEnergyRegen != that.shipConfigEnergyRegen) return false;
        if (shipConfigHpMax != that.shipConfigHpMax) return false;
        if (shipConfigHpRegen != that.shipConfigHpRegen) return false;
        if (shipConfigId != that.shipConfigId) return false;
        if (shipConfigMoveAcceleration != that.shipConfigMoveAcceleration) return false;
        if (shipConfigMoveMaxSpeed != that.shipConfigMoveMaxSpeed) return false;
        if (shipConfigRotationAcceleration != that.shipConfigRotationAcceleration) return false;
        if (shipConfigRotationMaxSpeed != that.shipConfigRotationMaxSpeed) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = shipConfigId;
        result = 31 * result + shipConfigMoveMaxSpeed;
        result = 31 * result + shipConfigRotationMaxSpeed;
        result = 31 * result + shipConfigMoveAcceleration;
        result = 31 * result + shipConfigRotationAcceleration;
        result = 31 * result + shipConfigArmor;
        result = 31 * result + shipConfigEnergyMax;
        result = 31 * result + shipConfigHpMax;
        result = 31 * result + shipConfigEnergyRegen;
        result = 31 * result + shipConfigHpRegen;
        return result;
    }
}
