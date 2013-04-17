package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;
import java.util.*;

/**
 *
 */
@Table(name = "ship_state", schema = "", catalog = "ge")
@Entity
public class ShipState {
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

    private int shipStateRotationAngle;

    @Column(name = "ship_state_rotation_angle", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipStateRotationAngle() {
        return shipStateRotationAngle;
    }

    public void setShipStateRotationAngle(int shipStateRotationAngle) {
        this.shipStateRotationAngle = shipStateRotationAngle;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShipState shipState = (ShipState) o;

        if (shipStateArmorDurability != shipState.shipStateArmorDurability) return false;
        if (shipStateHp != shipState.shipStateHp) return false;
        if (shipStateId != shipState.shipStateId) return false;
        if (shipStateMoveSpeed != shipState.shipStateMoveSpeed) return false;
        if (shipStateRotationAngle != shipState.shipStateRotationAngle) return false;
        if (shipStateRotationSpeed != shipState.shipStateRotationSpeed) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = shipStateId;
        result = 31 * result + shipStateMoveSpeed;
        result = 31 * result + shipStateRotationSpeed;
        result = 31 * result + shipStateRotationAngle;
        result = 31 * result + shipStateHp;
        result = 31 * result + shipStateArmorDurability;
        return result;
    }

    private Set<Player> players;

    @OneToMany(mappedBy = "shipState", fetch = FetchType.LAZY)
    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }
}
