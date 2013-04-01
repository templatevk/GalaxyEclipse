package arch.galaxyeclipse.server.data.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 */
@Entity
public class Engines {
    private int engineId;

    @Column(name = "engine_id")
    @Id
    public int getEngineId() {
        return engineId;
    }

    public void setEngineId(int engineId) {
        this.engineId = engineId;
    }

    private int moveAccelerationBonus;

    @Column(name = "move_acceleration_bonus")
    @Basic
    public int getMoveAccelerationBonus() {
        return moveAccelerationBonus;
    }

    public void setMoveAccelerationBonus(int moveAccelerationBonus) {
        this.moveAccelerationBonus = moveAccelerationBonus;
    }

    private int moveMaxSpeedBonus;

    @Column(name = "move_max_speed_bonus")
    @Basic
    public int getMoveMaxSpeedBonus() {
        return moveMaxSpeedBonus;
    }

    public void setMoveMaxSpeedBonus(int moveMaxSpeedBonus) {
        this.moveMaxSpeedBonus = moveMaxSpeedBonus;
    }

    private int rotationAccelerationBonus;

    @Column(name = "rotation_acceleration_bonus")
    @Basic
    public int getRotationAccelerationBonus() {
        return rotationAccelerationBonus;
    }

    public void setRotationAccelerationBonus(int rotationAccelerationBonus) {
        this.rotationAccelerationBonus = rotationAccelerationBonus;
    }

    private int rotationMaxSpeedBonus;

    @Column(name = "rotation_max_speed_bonus")
    @Basic
    public int getRotationMaxSpeedBonus() {
        return rotationMaxSpeedBonus;
    }

    public void setRotationMaxSpeedBonus(int rotationMaxSpeedBonus) {
        this.rotationMaxSpeedBonus = rotationMaxSpeedBonus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Engines engines = (Engines) o;

        if (engineId != engines.engineId) return false;
        if (moveAccelerationBonus != engines.moveAccelerationBonus) return false;
        if (moveMaxSpeedBonus != engines.moveMaxSpeedBonus) return false;
        if (rotationAccelerationBonus != engines.rotationAccelerationBonus) return false;
        if (rotationMaxSpeedBonus != engines.rotationMaxSpeedBonus) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = engineId;
        result = 31 * result + moveAccelerationBonus;
        result = 31 * result + moveMaxSpeedBonus;
        result = 31 * result + rotationAccelerationBonus;
        result = 31 * result + rotationMaxSpeedBonus;
        return result;
    }
}
