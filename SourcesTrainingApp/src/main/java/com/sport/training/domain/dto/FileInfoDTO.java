package com.sport.training.domain.dto;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.model.User;

@SuppressWarnings("serial")
public class FileInfoDTO implements Serializable{
	// ======================================
	// = Attributes =
	// ======================================
	private Long id;
	private String name;
	private String url;
	private UserDTO coachDTO;
	
	// ======================================
	// = Constructors =
	// ======================================
	public FileInfoDTO() {
	}

	public FileInfoDTO(String name, String url, final UserDTO coach) {
	    this.name = name;
	    this.url = url;
	    setCoachDTO(coach);
	  }

	// ======================================
	// = Getters and Setters =
	// ======================================
	
	  public String getName() {
	    return this.name;
	  }

	  public void setName(String name) {
	    this.name = name;
	  }

	  public String getUrl() {
	    return this.url;
	  }

	  public void setUrl(String url) {
	    this.url = url;
	  }
		
	  public UserDTO getCoachDTO() {
		return coachDTO;
	}

	public void setCoachDTO(UserDTO coachDTO) {
		this.coachDTO = coachDTO;
	}

	@Override
	public String toString() {
		return "FileInfo [id=" + id + ", name=" + name + ", url=" + url + ", coach=" + coachDTO + "]";
	}
	

	}