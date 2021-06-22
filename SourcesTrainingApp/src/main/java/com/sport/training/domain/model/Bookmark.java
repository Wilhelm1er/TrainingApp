package com.sport.training.domain.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.sport.training.authentication.domain.model.User;

/**
 * This class represents a Bookmark for a User. in event.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_BOOKMARK")
public class Bookmark implements Serializable {

	// ======================================
	// = Attributes =
	// ======================================
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "book_seq_gen")
	private Long id;
 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COACH_FK", nullable = false)
	@NotNull(message = "invalid Coach")
	private User coach;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ATHLETE_FK", nullable = false)
	@NotNull(message = "invalid Athlete")
	private User athlete;
 
	// ======================================
	// = Constructors =
	// ======================================
	public Bookmark() {
	}

	public Bookmark(final User athlete, final User coach) {
		setAthlete(athlete);
		setCoach(coach);
	}

	// ======================================
	// = Getters and Setters =
	// ======================================

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public User getCoach() {
		return coach;
	}

	public void setCoach(User coach) {
		this.coach = coach;
	}

	public User getAthlete() {
		return athlete;
	}

	public void setAthlete(User athlete) {
		this.athlete = athlete;
	}

	@Override
	public String toString() {
		return "Bookmark [id=" + id + ", coach=" + coach + ", athlete=" + athlete + "]";
	}

}
