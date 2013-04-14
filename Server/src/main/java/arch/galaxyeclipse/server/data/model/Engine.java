package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Table(name = "engine", schema = "", catalog = "ge")
@Entity
@DiscriminatorValue(value = "1")
@PrimaryKeyJoinColumn(name = "item_id")
public class Engine extends Item {
    private int moveAccelerationBonus;

    @Column(name = "move_acceleration_bonus", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getMoveAccelerationBonus() {
        return moveAccelerationBonus;
    }

    public void setMoveAccelerationBonus(int moveAccelerationBonus) {
        this.moveAccelerationBonus = moveAccelerationBonus;
    }

    private int moveMaxSpeedBonus;

    @Column(name = "move_max_speed_bonus", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getMoveMaxSpeedBonus() {
        return moveMaxSpeedBonus;
    }

    public void setMoveMaxSpeedBonus(int moveMaxSpeedBonus) {
        this.moveMaxSpeedBonus = moveMaxSpeedBonus;
    }

    private int rotationAccelerationBonus;

    @Column(name = "rotation_acceleration_bonus", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getRotationAccelerationBonus() {
        return rotationAccelerationBonus;
    }

    public void setRotationAccelerationBonus(int rotationAccelerationBonus) {
        this.rotationAccelerationBonus = rotationAccelerationBonus;
    }

    private int rotationMaxSpeedBonus;

    @Column(name = "rotation_max_speed_bonus", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getRotationMaxSpeedBonus() {
        return rotationMaxSpeedBonus;
    }

    public void setRotationMaxSpeedBonus(int rotationMaxSpeedBonus) {
        this.rotationMaxSpeedBonus = rotationMaxSpeedBonus;
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

        Engine engine = (Engine) o;

        if (itemId != engine.itemId) return false;
        if (moveAccelerationBonus != engine.moveAccelerationBonus) return false;
        if (moveMaxSpeedBonus != engine.moveMaxSpeedBonus) return false;
        if (rotationAccelerationBonus != engine.rotationAccelerationBonus) return false;
        if (rotationMaxSpeedBonus != engine.rotationMaxSpeedBonus) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = itemId;
        result = 31 * result + moveAccelerationBonus;
        result = 31 * result + moveMaxSpeedBonus;
        result = 31 * result + rotationAccelerationBonus;
        result = 31 * result + rotationMaxSpeedBonus;
        result = 31 * result + itemId;
        return result;
    }
}
