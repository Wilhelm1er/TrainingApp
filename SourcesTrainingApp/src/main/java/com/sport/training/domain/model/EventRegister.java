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
 * A registration represents the event that an athlete registered. This registration has one
 * registration event and is relevant for one athlete.
 *
 * @see User
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_EVENT_REGISTER") 
public class EventRegister implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator="event_seq_gen")
	private Long id;
	
	@Column(name = "REGISTER_DATE")
	private Date registerDate;
	
	@NotBlank(message = "invalid event date")
	private Date eventDate;

	@Column(name = "CREDITCOST")
	private int creditcost;
	
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
    public EventRegister() {
    	this.registerDate=new Date();
    }

	public EventRegister(final Date eventDate, final Event event,final User athlete) {
    	this.registerDate=new Date();
    	setEventDate(eventDate);
    	setRegisterDate(registerDate);
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
	
	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

    public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public int getCreditcost() {
		return creditcost;
	}

	public void setCreditcost(int creditcost) {
		this.creditcost = creditcost;
	}


    public User getAthlete() {
        return athlete;
    }

    public void setAthlete(final User athlete) {
    	this.athlete = athlete;
    }

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("Event Register{");
        buf.append("id=").append(getId());
        buf.append(",registerDate=").append(getRegisterDate());
        buf.append(",eventDate=").append(getEvent().getDate());
        buf.append(",eventID=").append(getId());
        buf.append(",athleteID=").append(getAthlete().getUsername());
        buf.append('}');
        return buf.toString();
    }
}
