package com.sport.training.domain.dto;

import java.io.Serializable;
import java.sql.Date;

import com.sport.training.authentication.domain.dto.UserDTO;

/**
 * This class follows the Data Transfert Object design pattern. It is a client
 * view of an Event registration. This class only transfers data from a distant
 * service to a client.
 */
@SuppressWarnings("serial")
public class EventRegistryDTO implements Serializable {

	// ======================================
	// = Attributes =
	// ======================================
	private Long id;
	private Date registerDate;
	private UserDTO userDTO;
	private EventDTO eventDTO;

	// ======================================
	// = Constructors =
	// ======================================

	public EventRegistryDTO() {
		id = 0L;
		registerDate = null;
		userDTO = new UserDTO();
		eventDTO = new EventDTO();
	}

	public EventRegistryDTO(UserDTO userDTO, EventDTO eventDTO) {
		this.userDTO = userDTO;
		this.eventDTO = eventDTO;
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

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO athleteDTO) {
		this.userDTO = athleteDTO;
	}

	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append("Event RegistryDTO{");
		buf.append("id=").append(getId());
		buf.append(",registerDate=").append(getRegisterDate());
		buf.append(",eventID=").append(getEventDTO().getId());
		buf.append(",athleteID=").append(getUserDTO().getUsername());
		buf.append('}');
		return buf.toString();
	}
}
