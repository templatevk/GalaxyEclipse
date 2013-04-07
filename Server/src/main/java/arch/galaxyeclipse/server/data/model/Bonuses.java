package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Entity
public class Bonuses {
    private int bonusId;

    @Column(name = "bonus_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getBonusId() {
        return bonusId;
    }

    public void setBonusId(int bonusId) {
        this.bonusId = bonusId;
    }

    private int bonusValue;

    @Column(name = "bonus_value", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getBonusValue() {
        return bonusValue;
    }

    public void setBonusValue(int bonusValue) {
        this.bonusValue = bonusValue;
    }

    private int bonusTypeId;

    @Column(name = "bonus_type_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Basic
    public int getBonusTypeId() {
        return bonusTypeId;
    }

    public void setBonusTypeId(int bonusTypeId) {
        this.bonusTypeId = bonusTypeId;
    }

    private int itemId;

    @Column(name = "item_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
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

        Bonuses bonuses = (Bonuses) o;

        if (bonusId != bonuses.bonusId) return false;
        if (bonusTypeId != bonuses.bonusTypeId) return false;
        if (bonusValue != bonuses.bonusValue) return false;
        if (itemId != bonuses.itemId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = bonusId;
        result = 31 * result + bonusValue;
        result = 31 * result + bonusTypeId;
        result = 31 * result + itemId;
        return result;
    }
}
