package com.train.sports.domain.dto;

import java.io.Serializable;
import java.sql.Date;

import com.train.sports.authentication.domain.dto.UserDTO;

/**
 * This class follows the Data Transfert Object design pattern. It is a client
 * view of a Notation. This class only transfers data from a distant service to a
 * client.
 */
@SuppressWarnings("serial")
public class NotationDTO implements Serializable {

	// ======================================
	// = Attributes =
	// ======================================
	private String id;
	private Date date;
	private int note;
	private EventDTO eventDTO;
	private UserDTO coachDTO;
	private UserDTO athleteDTO;

	// ======================================
	// = Constructors =
	// ======================================
	public NotationDTO() {
	}

	public NotationDTO(final String id, final int note) {
		setId(id);
		setNote(note);
	}

	// ======================================
	// = Getters and Setters =
	// ======================================
	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}
	
	public double getNote() {
		return note;
	}

	public void setNote(final int note) {
		this.note = note;
	}

	public EventDTO getEventDTO() {
		return eventDTO;
	}

	public void setEventDTO(EventDTO eventDTO) {
		this.eventDTO = eventDTO;
	}

	public UserDTO getCoachDTO() {
		return coachDTO;
	}

	public void setCoachDTO(UserDTO coachDTO) {
		this.coachDTO = coachDTO;
	}

	public UserDTO getAthleteDTO() {
		return athleteDTO;
	}

	public void setAthleteDTO(UserDTO athleteDTO) {
		this.athleteDTO = athleteDTO;
	}

	@Override
	public String toString() {
		return "NotationDTO [id=" + id + ", date=" + date + ", note=" + note + ", eventDTO="
				+ eventDTO + ", coachDTO=" + coachDTO + ", athleteDTO=" + athleteDTO + "]";
	}

}
