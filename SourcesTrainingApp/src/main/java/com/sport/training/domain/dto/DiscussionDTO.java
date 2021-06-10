package com.sport.training.domain.dto;

import java.io.Serializable;
import java.util.Date;

import com.sport.training.authentication.domain.dto.UserDTO;

/**
 * This class follows the Data Transfert Object design pattern. It is a client
 * view of a discipline registration. This class only transfers data from a distant service to a
 * client.
 */
@SuppressWarnings("serial")
public class DiscussionDTO implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================

	private Long id;
	private UserDTO userDTO;

	// ======================================
    // =            Constructors            =
    // ======================================
	
	public DiscussionDTO() {
		id=0L;
		userDTO=new UserDTO();
    }
	
	
	
    public DiscussionDTO(UserDTO userDTO) {
    	setUserDTO(userDTO);
    	
    }

    // ======================================
    // =         Getters and Setters        =
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

	@Override
	public String toString() {
		return "DiscussionDTO [id=" + id + ", userDTO=" + userDTO + "]";
	}

}
