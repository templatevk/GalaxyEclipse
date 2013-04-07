package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Table(name = "ship_states", schema = "", catalog = "ge")
@Entity
public class ShipStates {
    private int shipStateId;

    @Column(name = "ship_state_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getShipStateId() {
        return shipStateId;
    }

    public void setShipStateId(int shipStateId) {
        this.shipStateId = shipStateId;
    }

    private int shipStateMoveSpeed;

    @Column(name = "ship_state_move_speed", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipStateMoveSpeed() {
        return shipStateMoveSpeed;
    }

    public void setShipStateMoveSpeed(int shipStateMoveSpeed) {
        this.shipStateMoveSpeed = shipStateMoveSpeed;
    }

    private int shipStateRotationSpeed;

    @Column(name = "ship_state_rotation_speed", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipStateRotationSpeed() {
        return shipStateRotationSpeed;
    }

    public void setShipStateRotationSpeed(int shipStateRotationSpeed) {
        this.shipStateRotationSpeed = shipStateRotationSpeed;
    }

    private int shipStateHp;

    @Column(name = "ship_state_hp", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipStateHp() {
        return shipStateHp;
    }

    public void setShipStateHp(int shipStateHp) {
        this.shipStateHp = shipStateHp;
    }

    private int shipStateArmorDurability;

    @Column(name = "ship_state_armor_durability", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipStateArmorDurability() {
        return shipStateArmorDurability;
    }

    public void setShipStateArmorDurability(int shipStateArmorDurability) {
        this.shipStateArmorDurability = shipStateArmorDurability;
    }

    private int shipStateRotationAngle;

    @Column(name = "ship_state_rotation_angle", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipStateRotationAngle() {
        return shipStateRotationAngle;
    }

    public void setShipStateRotationAngle(int shipStateRotationAngle) {
        this.shipStateRotationAngle = shipStateRotationAngle;
    }

    private int playerMoney;

    @Column(name = "player_money", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getPlayerMoney() {
        return playerMoney;
    }

    public void setPlayerMoney(int playerMoney) {
        this.playerMoney = playerMoney;
    }

    private int playerId;

    @Column(name = "player_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    private int locationDynamicObjectId;

    @Column(name = "location_dynamic_object_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getLocationDynamicObjectId() {
        return locationDynamicObjectId;
    }

    public void setLocationDynamicObjectId(int locationDynamicObjectId) {
        this.locationDynamicObjectId = locationDynamicObjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShipStates that = (ShipStates) o;

        if (locationDynamicObjectId != that.locationDynamicObjectId) return false;
        if (playerId != that.playerId) return false;
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
        result = 31 * result + playerId;
        result = 31 * result + locationDynamicObjectId;
        return result;
    }
}
