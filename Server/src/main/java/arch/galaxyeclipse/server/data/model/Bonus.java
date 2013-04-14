package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Table(name = "bonus", schema = "", catalog = "ge")
@Entity
@DiscriminatorValue(value = "3")
@PrimaryKeyJoinColumn(name = "item_id")
public class Bonus extends Item {
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

        Bonus bonus = (Bonus) o;

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

    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", referencedColumnName = "item_id", nullable = false, insertable = false, updatable = false)
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    private BonusType bonusType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bonus_type_id", referencedColumnName = "bonus_type_id", nullable = false, insertable = false, updatable = false)
    public BonusType getBonusType() {
        return bonusType;
    }

    public void setBonusType(BonusType bonusType) {
        this.bonusType = bonusType;
    }
}
