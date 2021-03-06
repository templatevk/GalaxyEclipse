package arch.galaxyeclipse.server.data.model;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Set;

/**
 *
 */
@Table(name = "ship_state", schema = "", catalog = "ge")
@Entity
@DynamicUpdate
public class GeShipState {

    private int shipStateId;

    @Column(name = "ship_state_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getShipStateId() {
        return shipStateId;
    }

    public void setShipStateId(int shipStateId) {
        this.shipStateId = shipStateId;
    }

    private float shipStateMoveSpeed;

    @Column(name = "ship_state_move_speed", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public float getShipStateMoveSpeed() {
        return shipStateMoveSpeed;
    }

    public void setShipStateMoveSpeed(float shipStateMoveSpeed) {
        this.shipStateMoveSpeed = shipStateMoveSpeed;
    }

    private float shipStateRotationSpeed;

    @Column(name = "ship_state_rotation_speed", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public float getShipStateRotationSpeed() {
        return shipStateRotationSpeed;
    }

    public void setShipStateRotationSpeed(float shipStateRotationSpeed) {
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

    private int shipStateEnergy;

    @Column(name = "ship_state_energy", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getShipStateEnergy() {
        return shipStateEnergy;
    }

    public void setShipStateEnergy(int shipStateEnergy) {
        this.shipStateEnergy = shipStateEnergy;
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

        GeShipState shipState = (GeShipState) o;

        if (shipStateId != shipState.shipStateId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return shipStateId;
    }

    private Set<GePlayer> players;

    @OneToMany(mappedBy = "shipState", fetch = FetchType.LAZY)
    public Set<GePlayer> getPlayers() {
        return players;
    }

    public void setPlayers(Set<GePlayer> players) {
        this.players = players;
    }
}
