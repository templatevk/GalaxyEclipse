package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Table(name = "engine", schema = "", catalog = "ge")
@Entity
@DiscriminatorValue(value = GeItem.ENGINE_TYPE)
@PrimaryKeyJoinColumn(name = "item_id")
public class GeEngine extends GeItem {
    private float moveAccelerationBonus;

    @Column(name = "move_acceleration_bonus", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public float getMoveAccelerationBonus() {
        return moveAccelerationBonus;
    }

    public void setMoveAccelerationBonus(float moveAccelerationBonus) {
        this.moveAccelerationBonus = moveAccelerationBonus;
    }

    private float moveMaxSpeedBonus;

    @Column(name = "move_max_speed_bonus", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public float getMoveMaxSpeedBonus() {
        return moveMaxSpeedBonus;
    }

    public void setMoveMaxSpeedBonus(float moveMaxSpeedBonus) {
        this.moveMaxSpeedBonus = moveMaxSpeedBonus;
    }

    private float rotationAccelerationBonus;

    @Column(name = "rotation_acceleration_bonus", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public float getRotationAccelerationBonus() {
        return rotationAccelerationBonus;
    }

    public void setRotationAccelerationBonus(float rotationAccelerationBonus) {
        this.rotationAccelerationBonus = rotationAccelerationBonus;
    }

    private float rotationMaxSpeedBonus;

    @Column(name = "rotation_max_speed_bonus", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public float getRotationMaxSpeedBonus() {
        return rotationMaxSpeedBonus;
    }

    public void setRotationMaxSpeedBonus(float rotationMaxSpeedBonus) {
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

        GeEngine engine = (GeEngine) o;

        if (itemId != engine.itemId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return itemId;
    }
}
