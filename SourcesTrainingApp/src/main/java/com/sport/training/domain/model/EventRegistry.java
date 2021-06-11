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

import com.sport.training.authentication.domain.model.User;

/**
 * An event register represents the event that an athlete registered. This
 * registration has one registration event and is relevant for one athlete.
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_EVENT_REGISTRY")
public class EventRegistry implements Serializable {

	// ======================================
	// = Attributes =
	// ======================================
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "event_seq_gen")
	private Long id;

	@Column(name = "REGISTER_DATE")
	private Date registerDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EVENT_FK", nullable = false)
	@NotNull(message = "invalid Event")
	private Event event;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_FK", nullable = false)
	@NotNull(message = "invalid User")
	private User user;

	// ======================================
	// = Constructors =
	// ======================================

	public EventRegistry() {
		id = 0L;
		registerDate = null;
		user = new User();
		event = new Event();
	}

	public EventRegistry(User user, Event event) {
		this.registerDate = new Date();
		this.user = user;
		this.event = event;
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

	private void setRegisterDate(final Date registerDate) {
		this.registerDate = registerDate;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
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
		buf.append("Event Registry{");
		buf.append("id=").append(getId());
		buf.append(",registerDate=").append(getRegisterDate());
		buf.append(",eventID=").append(getEvent().getId());
		buf.append(",athleteID=").append(getUser().getUsername());
		buf.append('}');
		return buf.toString();
	}
}
