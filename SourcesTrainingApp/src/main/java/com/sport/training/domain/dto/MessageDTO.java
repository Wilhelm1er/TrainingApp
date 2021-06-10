package com.sport.training.domain.dto;

import java.io.Serializable;
import java.util.Date;

import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.model.Discussion;

/**
 * This class follows the Data Transfert Object design pattern. It is a client
 * view of an Event. This class only transfers data from a distant service to a
 * client.
 */
@SuppressWarnings("serial")
public class MessageDTO implements Serializable {

	// ======================================
	// = Attributes =
	// ======================================
	private Long id;
	private Date date;
	private String texte;
	private UserDTO senderDTO;
	private UserDTO recipientDTO;
	private DiscussionDTO discussionDTO;

	// ======================================
	// = Constructors =
	// ======================================
	public MessageDTO() {
		id=0L;
		this.date=new Date();
    	senderDTO=new UserDTO();
    	recipientDTO=new UserDTO();
    	discussionDTO=new DiscussionDTO();
	}

	public MessageDTO(final String texte, final UserDTO senderDTO, final UserDTO recipientDTO, final DiscussionDTO discussionDTO) {
		this.texte=texte;
		setSenderDTO(senderDTO);
		setRecipientDTO(recipientDTO);
		setDiscussionDTO(discussionDTO);
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTexte() {
		return texte;
	}

	public void setTexte(String texte) {
		this.texte = texte;
	}
	
	public UserDTO getSenderDTO() {
		return senderDTO;
	}

	public void setSenderDTO(UserDTO senderDTO) {
		this.senderDTO = senderDTO;
	}

	public UserDTO getRecipientDTO() {
		return recipientDTO;
	}

	public void setRecipientDTO(UserDTO recipientDTO) {
		this.recipientDTO = recipientDTO;
	}
	
	public DiscussionDTO getDiscussionDTO() {
		return discussionDTO;
	}

	public void setDiscussionDTO(DiscussionDTO discussionDTO) {
		this.discussionDTO = discussionDTO;
	}

	@Override
	public String toString() {
		return "MessageDTO [id=" + id + ", date=" + date + ", texte=" + texte + ", senderDTO=" + senderDTO
				+ ", recipientDTO=" + recipientDTO + ", discussionDTO=" + discussionDTO + "]";
	}
	

}
