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
@Table(name = "T_REGISTRATION") 
public class Registration implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator="order_seq_gen")
	private Long id;
	
	@Column(name = "REGISTRATION_DATE")
	private Date registrationDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EVENT_FK")
	@NotNull(message = "invalid Event")
	private Event event;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="USER_FK", nullable = false)
    @NotNull(message = "invalid Customer")
    private User athlete;

    // ======================================
    // =            Constructors            =
    // ======================================
    public Registration() {
    	this.registrationDate=new Date();
    }

    public Registration(final String firstname, final String lastname, final String street1, final String city, final String zipcode, final String country, final User customer) {
    	this.registrationDate=new Date();
        setRegistrationDate(registrationDate);
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

	public Date getRegistrationDate() {
        return registrationDate;
    }

    private void setRegistrationDate(final Date registrationDate) {
    	this.registrationDate = registrationDate;
    }

    public User getAthlete() {
        return athlete;
    }

    public void setAthlete(final User athlete) {
    	this.athlete = athlete;
    }

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("Registration{");
        buf.append("id=").append(getId());
        buf.append(",registrationDate=").append(getRegistrationDate());
        buf.append('}');
        return buf.toString();
    }
}
