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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.sport.training.authentication.domain.model.User;

/**
 * An event register represents the event that an athlete registered. This registration has one
 * registration event and is relevant for one athlete.
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_EVENT_REGISTRY") 
public class EventRegistry implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator="event_seq_gen")
	private Long id;
	
	@Column(name = "REGISTER_DATE")
	private Date registerDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EVENT_FK")
	@NotNull(message = "invalid Event")
	private Event event;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="USER_FK", nullable = false)
    @NotNull(message = "invalid Athlete")
    private User athlete;

    // ======================================
    // =            Constructors            =
    // ======================================
    public EventRegistry() {
    	this.registerDate=new Date();
    }

	public EventRegistry(final Event event,final User athlete) {
    	this.registerDate=new Date();
    	setRegisterDate(registerDate);
    	setEvent(event);
        setAthlete(athlete);
    }

    // ======================================
    // =         Getters and Setters        =
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

    public User getAthlete() {
        return athlete;
    }

    public void setAthlete(final User athlete) {
    	this.athlete = athlete;
    }

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("Event Registry{");
        buf.append("id=").append(getId());
        buf.append(",registerDate=").append(getRegisterDate());
        buf.append(",eventID=").append(getId());
        buf.append(",athleteID=").append(getAthlete().getUsername());
        buf.append('}');
        return buf.toString();
    }
}
