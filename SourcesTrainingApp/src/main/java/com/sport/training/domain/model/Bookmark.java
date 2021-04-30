package com.sport.training.domain.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.sport.training.authentication.domain.model.User;

/**
 * This class represents a Bookmark for a User.
 * in event.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_BOOKMARK")
public class Bookmark implements Serializable {

	// ======================================
	// = Attributes =
	// ======================================
	@NotBlank(message = "id must be defined")
	@Id
	private String id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COACH_FK")
	@NotNull(message = "invalid Coach")
	private User coach;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ATHLETE_FK")
	@NotNull(message = "invalid Athlete")
	private User athlete;

	// ======================================
	// = Constructors =
	// ======================================
	public Bookmark() {
	}

	public Bookmark(final String id) {
		setId(id);
	}

	public Bookmark(final String id, final User coach) {
		setId(id);
		setCoach(coach);
	}

	// ======================================
	// = Getters and Setters =
	// ======================================

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public User getCoach() {
		return coach;
	}

	public void setCoach(User coach) {
		this.coach = coach;
	}


	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append("Bookmark{");
		buf.append("id=").append(getId());
		buf.append(",coachFirstname=").append(getCoach().getFirstname());
		buf.append(",coachLastname=").append(getCoach().getLastname());
		buf.append('}');
		return buf.toString();
	}
}
