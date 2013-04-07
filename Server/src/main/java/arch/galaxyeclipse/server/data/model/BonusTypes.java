package arch.galaxyeclipse.server.data.model;

import javax.persistence.*;

/**
 *
 */
@Table(name = "bonus_types", schema = "", catalog = "ge")
@Entity
public class BonusTypes {
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

    @Column(name = "bonus_type_name", nullable = false, insertable = true, updatable = true, length = 16, precision = 0)
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

        BonusTypes that = (BonusTypes) o;

        if (bonusTypeId != that.bonusTypeId) return false;
        if (bonusTypeName != null ? !bonusTypeName.equals(that.bonusTypeName) : that.bonusTypeName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = bonusTypeId;
        result = 31 * result + (bonusTypeName != null ? bonusTypeName.hashCode() : 0);
        return result;
    }
}
