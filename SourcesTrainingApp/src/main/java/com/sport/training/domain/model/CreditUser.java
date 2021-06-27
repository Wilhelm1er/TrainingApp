
package com.sport.training.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.sport.training.authentication.domain.model.User;

/**
 * A credit register represents the credit that an athlete registered. This
 * registration has one registration credit and is relevant for one athlete.
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_CREDIT_USER")
public class CreditUser implements Serializable {

	// ======================================
	// = Attributes =
	// ======================================
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "credit_seq_gen")
	private Long id;

	@Column(name = "MOUVEMENT_DATE")
	private Date mouvementDate;

	@Column(name = "CREDIT")
	@Positive(message = "invalid Credit")
	private int credit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_FK", nullable = false)
	@NotNull(message = "invalid Athlete")
	private User user;

	// ======================================
	// = Constructors =
	// ======================================
	public CreditUser() {
		this.mouvementDate = new Date();
	}

	public CreditUser(final int credit, final User user) {
		this.mouvementDate = new Date();
		setMouvementDate(mouvementDate);
		setCredit(credit);
		setUser(user);
	}

	// ======================================
	// = Getters and Setters =
	// ======================================

	public Long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	private void setMouvementDate(final Date mouvementDate) {
		this.mouvementDate = mouvementDate;
	}

	public Date getMouvementDate() {
		return mouvementDate;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public User getUser() {
		return user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append("Credit Registry{");
		buf.append("id=").append(getId());
		buf.append(",mouvementDate=").append(getMouvementDate());
		buf.append(",credit=").append(getCredit());
		buf.append(",userID=").append(getUser().getUsername());
		buf.append('}');
		return buf.toString();
	}
}
