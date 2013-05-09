package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;
import java.util.Set;

/**
 *
 */
@Table(name = "bonus_type", schema = "", catalog = "ge")
@Entity
public class BonusType {
    private int bonusTypeId;

    @Column(name = "bonus_type_id", nullable = false, insertable = true, updatable = true, length = 10, precision = 0)
    @Id
    public int getBonusTypeId() {
        return bonusTypeId;
    }

    public void setBonusTypeId(int bonusTypeId) {
        this.bonusTypeId = bonusTypeId;
    }

    private String bonusTypeName;

    @Column(name = "bonus_type_name", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Basic
    public String getBonusTypeName() {
        return bonusTypeName;
    }

    public void setBonusTypeName(String bonusTypeName) {
        this.bonusTypeName = bonusTypeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BonusType bonusType = (BonusType) o;

        if (bonusTypeId != bonusType.bonusTypeId) return false;
        if (bonusTypeName != null ? !bonusTypeName.equals(bonusType.bonusTypeName) : bonusType.bonusTypeName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = bonusTypeId;
        result = 31 * result + (bonusTypeName != null ? bonusTypeName.hashCode() : 0);
        return result;
    }

    private Set<Bonus> bonuses;

    @OneToMany(mappedBy = "bonusType", fetch = FetchType.LAZY)
    public Set<Bonus> getBonuses() {
        return bonuses;
    }

    public void setBonuses(Set<Bonus> bonuses) {
        this.bonuses = bonuses;
    }
}
