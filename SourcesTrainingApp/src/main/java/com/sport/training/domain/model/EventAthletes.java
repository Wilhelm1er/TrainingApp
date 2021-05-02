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
 * A registration represents the event that an athlete registered. This registration has one
 * registration event and is relevant for one athlete.
 *
 * @see User
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_EVENT_ATHLETES") 
public class EventAthletes implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator="event_seq_gen")
	private Long id;
	
	@Column(name = "EVENT_DATE")
	private Date eventDate;
	
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
    public EventAthletes() {
    	this.eventDate=new Date();
    }

    public EventAthletes(final User athlete) {
    	this.eventDate=new Date();
    	setEventDate(eventDate);
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

	public Date getEventDate() {
        return eventDate;
    }

    private void setEventDate(final Date eventDate) {
    	this.eventDate = eventDate;
    }

    public User getAthlete() {
        return athlete;
    }

    public void setAthlete(final User athlete) {
    	this.athlete = athlete;
    }

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("Event Athletes{");
        buf.append("id=").append(getId());
        buf.append(",eventDate=").append(getEventDate());
        buf.append(",athleteID=").append(getAthlete().getUsername());
        buf.append('}');
        return buf.toString();
    }
}
