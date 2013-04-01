package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Table(name = "ship_states", schema = "", catalog = "ge")
@Entity
public class ShipStates {
    private int shipStateId;

    @Column(name = "ship_state_id")
    @Id
    public int getShipStateId() {
        return shipStateId;
    }

    public void setShipStateId(int shipStateId) {
        this.shipStateId = shipStateId;
    }

    private int shipStateMoveSpeed;

    @Column(name = "ship_state_move_speed")
    @Basic
    public int getShipStateMoveSpeed() {
        return shipStateMoveSpeed;
    }

    public void setShipStateMoveSpeed(int shipStateMoveSpeed) {
        this.shipStateMoveSpeed = shipStateMoveSpeed;
    }

    private int shipStateRotationSpeed;

    @Column(name = "ship_state_rotation_speed")
    @Basic
    public int getShipStateRotationSpeed() {
        return shipStateRotationSpeed;
    }

    public void setShipStateRotationSpeed(int shipStateRotationSpeed) {
        this.shipStateRotationSpeed = shipStateRotationSpeed;
    }

    private int shipStateHp;

    @Column(name = "ship_state_hp")
    @Basic
    public int getShipStateHp() {
        return shipStateHp;
    }

    public void setShipStateHp(int shipStateHp) {
        this.shipStateHp = shipStateHp;
    }

    private int shipStateArmorDurability;

    @Column(name = "ship_state_armor_durability")
    @Basic
    public int getShipStateArmorDurability() {
        return shipStateArmorDurability;
    }

    public void setShipStateArmorDurability(int shipStateArmorDurability) {
        this.shipStateArmorDurability = shipStateArmorDurability;
    }

    private int shipStateRotationAngle;

    @Column(name = "ship_state_rotation_angle")
    @Basic
    public int getShipStateRotationAngle() {
        return shipStateRotationAngle;
    }

    public void setShipStateRotationAngle(int shipStateRotationAngle) {
        this.shipStateRotationAngle = shipStateRotationAngle;
    }

    private int playerMoney;

    @Column(name = "player_money")
    @Basic
    public int getPlayerMoney() {
        return playerMoney;
    }

    public void setPlayerMoney(int playerMoney) {
        this.playerMoney = playerMoney;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShipStates that = (ShipStates) o;

        if (playerMoney != that.playerMoney) return false;
        if (shipStateArmorDurability != that.shipStateArmorDurability) return false;
        if (shipStateHp != that.shipStateHp) return false;
        if (shipStateId != that.shipStateId) return false;
        if (shipStateMoveSpeed != that.shipStateMoveSpeed) return false;
        if (shipStateRotationAngle != that.shipStateRotationAngle) return false;
        if (shipStateRotationSpeed != that.shipStateRotationSpeed) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = shipStateId;
        result = 31 * result + shipStateMoveSpeed;
        result = 31 * result + shipStateRotationSpeed;
        result = 31 * result + shipStateHp;
        result = 31 * result + shipStateArmorDurability;
        result = 31 * result + shipStateRotationAngle;
        result = 31 * result + playerMoney;
        return result;
    }
}
