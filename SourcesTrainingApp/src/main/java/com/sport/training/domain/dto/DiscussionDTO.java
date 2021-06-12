package com.sport.training.domain.dto;

import java.io.Serializable;
import java.sql.Date;

import com.sport.training.authentication.domain.dto.UserDTO;

/**
 * This class follows the Data Transfert Object design pattern. It is a client
 * view of a discipline registration. This class only transfers data from a
 * distant service to a client.
 */
@SuppressWarnings("serial")
public class DiscussionDTO implements Serializable {

	// ======================================
	// = Attributes =
	// ======================================

	private Long id;
	private Date creationDate;
	private UserDTO userDTO;
	private String subject;

	// ======================================
	// = Constructors =
	// ======================================

	public DiscussionDTO() {

	}

	public DiscussionDTO(UserDTO userDTO, String subject) {
		setUserDTO(userDTO);
		setSubject(subject);

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

	public void setUserDTO(UserDTO coachDTO) {
		this.userDTO = coachDTO;

	}

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public String toString() {
		return "DiscussionDTO [id=" + id + ", creationDate=" + creationDate + ", userDTO=" + userDTO + ", subject="
				+ subject + "]";
	}

}
