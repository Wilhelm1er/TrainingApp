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
 * This class represents a Notation from the Sport system. The sport system is
 * divided into disciplines. Each one divided into activities and each activity
 * in event which athlete can provide a notation.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_NOTATION")
public class Notation implements Serializable {

	// ======================================
	// = Attributes =
	// ======================================
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "note_seq_gen")
	private Long id;

	@Column(name = "NOTATION_DATE")
	private Date notationDate;

	@Column(name = "NOTE")
	@Positive(message = "invalid Note")
	private int note;

	@Column(name = "COMMENTS")
	@Positive(message = "invalid Comments")
	private String comments;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EVENT_FK")
	@NotNull(message = "invalid Event")
	private Event event;

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
	public Notation() {
		this.notationDate = new Date();
	}

	public Notation(final int note, final User coach, final Event event) {
		this.notationDate = new Date();
		setNotationDate(notationDate);
		setNote(note);
		setCoach(coach);
		setEvent(event);
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

	public void setNotationDate(Date notationDate) {
		this.notationDate = notationDate;

	}

	public int getNote() {
		return note;
	}

	public void setNote(int note) {
		this.note = note;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
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
		return "Notation [id=" + id + ", date=" + notationDate + ", note=" + note + ", comments=" + comments
				+ ", event=" + event + ", coach=" + coach + ", athlete=" + athlete + "]";
	}

}
