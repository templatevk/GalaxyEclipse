package arch.galaxyeclipse.server.data.generated;

// Generated Mar 30, 2013 4:40:47 PM by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * BonusTypes generated by hbm2java
 */
@Entity
@Table(name = "bonus_types", catalog = "ge", uniqueConstraints = @UniqueConstraint(columnNames = "bonus_type_name"))
public class BonusTypes implements java.io.Serializable {

	private Integer bonusTypeId;
	private String bonusTypeName;

	public BonusTypes() {
	}

	public BonusTypes(String bonusTypeName) {
		this.bonusTypeName = bonusTypeName;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "bonus_type_id", unique = true, nullable = false)
	public Integer getBonusTypeId() {
		return this.bonusTypeId;
	}

	public void setBonusTypeId(Integer bonusTypeId) {
		this.bonusTypeId = bonusTypeId;
	}

	@Column(name = "bonus_type_name", unique = true, nullable = false, length = 16)
	public String getBonusTypeName() {
		return this.bonusTypeName;
	}

	public void setBonusTypeName(String bonusTypeName) {
		this.bonusTypeName = bonusTypeName;
	}

}
