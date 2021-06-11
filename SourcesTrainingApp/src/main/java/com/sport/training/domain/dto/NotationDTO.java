package com.sport.training.domain.dto;

import java.io.Serializable;
import java.sql.Date;

import com.sport.training.authentication.domain.dto.UserDTO;

/**
 * This class follows the Data Transfert Object design pattern. It is a client
 * view of a Notation. This class only transfers data from a distant service to
 * a client.
 */
@SuppressWarnings("serial")
public class NotationDTO implements Serializable {

	// ======================================
	// = Attributes =
	// ======================================
	private Long id;
	private Date notationDate;
	private int note;
	private String comments;
	private EventDTO eventDTO;
	private UserDTO coachDTO;
	private UserDTO athleteDTO;

	// ======================================
	// = Constructors =
	// ======================================
	public NotationDTO() {
	}

	public NotationDTO(final int note, UserDTO coachDTO, UserDTO athleteDTO, EventDTO eventDTO) {
		setNote(note);
		setCoachDTO(coachDTO);
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

	public Date getNotationDate() {
		return notationDate;
	}

	public double getNote() {
		return note;
	}

	public void setNote(final int note) {
		this.note = note;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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
		return "NotationDTO [id=" + id + ", notationDate=" + notationDate + ", note=" + note + ", comments=" + comments
				+ ", eventDTO=" + eventDTO + ", coachDTO=" + coachDTO + ", athleteDTO=" + athleteDTO + "]";
	}

}
