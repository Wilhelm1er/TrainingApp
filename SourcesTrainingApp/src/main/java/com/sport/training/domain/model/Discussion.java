package com.sport.training.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.sport.training.authentication.domain.model.User;

/**
 * This class represents a Discipline in the sport system. The sport system is
 * divided into disciplines. Each one divided into activities and each activity
 * in event.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_DISCUSSION")
public class Discussion implements Serializable {

	// ======================================
	// = Attributes =
	// ======================================
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "discussion_seq_gen")
	private Long id;
	
	@Column(name = "CREATION_DATE")
	private Date creationDate;

	@Column(name = "SUBJECT")
	private String subject;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_FK", nullable = false)
	@NotNull(message = "invalid User")
	private User user;

	// ======================================
	// = Constructors =
	// ======================================
	public Discussion() {
		id = 0L;
		this.creationDate = new Date();
		user = new User();
	}

	public Discussion(final User user, final String subject) {
		this.creationDate = new Date();
		setCreationDate(creationDate);
		setUser(user);
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

	public User getUser() {
		return user;
	}

	public void setUser(final User user) {
		this.user = user;
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
		return "Discussion [id=" + id + ", creationDate=" + creationDate + ", subject=" + subject + ", user=" + user
				+ "]";
	}

}
