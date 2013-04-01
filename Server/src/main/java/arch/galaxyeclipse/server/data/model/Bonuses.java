package arch.galaxyeclipse.server.data.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 */
@Entity
public class Bonuses {
    private int bonusId;

    @Column(name = "bonus_id")
    @Id
    public int getBonusId() {
        return bonusId;
    }

    public void setBonusId(int bonusId) {
        this.bonusId = bonusId;
    }

    private int bonusValue;

    @Column(name = "bonus_value")
    @Basic
    public int getBonusValue() {
        return bonusValue;
    }

    public void setBonusValue(int bonusValue) {
        this.bonusValue = bonusValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bonuses bonuses = (Bonuses) o;

        if (bonusId != bonuses.bonusId) return false;
        if (bonusValue != bonuses.bonusValue) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = bonusId;
        result = 31 * result + bonusValue;
        return result;
    }
}
