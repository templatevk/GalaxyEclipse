package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Table(name = "bonus", schema = "", catalog = "ge")
@Entity
@DiscriminatorValue(value = GeItem.BONUS_TYPE)
@PrimaryKeyJoinColumn(name = "item_id")
public class GeBonus extends GeItem {

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

        GeBonus bonus = (GeBonus) o;

        if (bonusTypeId != bonus.bonusTypeId) return false;
        if (bonusValue != bonus.bonusValue) return false;
        if (itemId != bonus.itemId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = itemId;
        result = 31 * result + bonusValue;
        result = 31 * result + bonusTypeId;
        result = 31 * result + itemId;
        return result;
    }

    private GeItem item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", referencedColumnName = "item_id", nullable = false, insertable = false, updatable = false)
    public GeItem getItem() {
        return item;
    }

    public void setItem(GeItem item) {
        this.item = item;
    }

    private GeBonusType bonusType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bonus_type_id", referencedColumnName = "bonus_type_id", nullable = false, insertable = false, updatable = false)
    public GeBonusType getBonusType() {
        return bonusType;
    }

    public void setBonusType(GeBonusType bonusType) {
        this.bonusType = bonusType;
    }
}
