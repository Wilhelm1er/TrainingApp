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
	private UserDTO athleteDTO;
	private EventDTO eventDTO;

	// ======================================
	// = Constructors =
	// ======================================
    public EventRegistryDTO(final UserDTO athleteDTO, final EventDTO eventDTO) {
    	setAthleteDTO(athleteDTO);
    	setEventDTO(eventDTO);
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

    public Date getRegisterDate() {
		return registerDate;
	}

    public EventDTO getEventDTO() {
		return eventDTO;
	}

	public void setEventDTO(EventDTO eventDTO) {
		this.eventDTO = eventDTO;
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
		buf.append("Event RegistryDTO{");
		buf.append("id=").append(getId());
		buf.append(",registerDate=").append(getRegisterDate());
	    buf.append(",eventID=").append(getId());
	    buf.append(",athleteID=").append(getAthleteDTO().getUsername());
		buf.append('}');
		return buf.toString();
	}
}
