package com.sport.training.domain.model;

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

import com.sport.training.authentication.domain.model.User;

@SuppressWarnings("serial")
@Entity
@Table(name = "T_FILE")
public class FileInfo implements Serializable{
	// ======================================
	// = Attributes =
	// ======================================
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator="file_seq_gen")
	private Long id;

	@NotBlank(message = "invalid Name")
	private String name;
 
	@NotBlank(message = "invalid Url")
	private String url;
 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COACH_FK")
	@NotNull(message = "invalid Coach")
	private User coach;

	public FileInfo(String name, String url, final User coach) {
	    this.name = name;
	    this.url = url;
	    setCoach(coach);
	  }

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
		
	  public User getCoach() {
		return coach;
	}

	public void setCoach(User coach) {
		this.coach = coach;
	}

	@Override
	public String toString() {
		return "FileInfo [id=" + id + ", name=" + name + ", url=" + url + ", coach=" + coach + "]";
	}
	

	}