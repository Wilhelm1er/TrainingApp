package com.sport.training.domain.dto;

import java.io.Serializable;

import com.sport.training.authentication.domain.dto.UserDTO;

/**
 * This class follows the Data Transfert Object design pattern. It is a client
 * view of a Bookmark. This class only transfers data from a distant service to a
 * client.
 */
@SuppressWarnings("serial")
public class BookmarkDTO implements Serializable {

	// ======================================
	// = Attributes =
	// ======================================
	private Long id;
	private UserDTO coachDTO;
	private UserDTO athleteDTO;

	// ======================================
	// = Constructors =
	// ======================================
	public BookmarkDTO() {
	}

	public BookmarkDTO(final UserDTO athleteDTO, final UserDTO coachDTO) {
		setAthleteDTO(athleteDTO);
		setCoachDTO(coachDTO);
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
		return "NotationDTO [id=" + id + ", coachDTO=" + coachDTO + ", athleteDTO=" + athleteDTO + "]";
	}

}
