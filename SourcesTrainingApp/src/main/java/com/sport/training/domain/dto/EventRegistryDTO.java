package com.sport.training.domain.dto;

import java.io.Serializable;
import java.sql.Date;

import com.sport.training.authentication.domain.dto.UserDTO;

/**
 * This class follows the Data Transfert Object design pattern. It is a client
 * view of an Event registration. This class only transfers data from a distant service to a
 * client.
 */
@SuppressWarnings("serial")
public class EventRegistryDTO implements Serializable {

	// ======================================
	// = Attributes =
	// ======================================
	private Long id;
	private Date registerDate;
	private Date eventDate;
	private int creditCost;
	private UserDTO athleteDTO;
	private EventDTO eventDTO;

	// ======================================
	// = Constructors =
	// ======================================
    public EventRegistryDTO() {
    }

    public EventRegistryDTO(final Date eventDate) {
    	setEventDate(eventDate);
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

    public void setRegisterDate(final Date registerDate) {
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

    public EventDTO getEventDTO() {
		return eventDTO;
	}

	public void setEventDTO(EventDTO eventDTO) {
		this.eventDTO = eventDTO;
	}

	public int getCreditCost() {
		return creditCost;
	}

	public void setCreditCost(final int creditCost) {
		this.creditCost = creditCost;
	}
	
	public UserDTO getAthleteDTO() {
		return athleteDTO;
	}

	public void setAthleteDTO(UserDTO athleteDTO) {
		this.athleteDTO = athleteDTO;
	}

	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append("Event RegisterDTO{");
		buf.append("id=").append(getId());
		buf.append(",registerDate=").append(getRegisterDate());
		 buf.append(",eventDate=").append(getEventDate());
	        buf.append(",eventID=").append(getId());
	        buf.append(",athleteID=").append(getAthleteDTO().getUsername());
		buf.append('}');
		return buf.toString();
	}
}
