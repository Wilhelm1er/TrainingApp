package com.sport.training.domain.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.sport.training.authentication.domain.model.User;

/**
 * This class represents an Event the Sport system. The sport system is divided
 * into disciplines. Each one divided into activities and each activity in
 * event.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_FILE")
public class File implements Serializable {
	// ======================================
	// = Attributes =
	// ======================================
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "file_seq_gen")
	private Long id;
	
	@Column(name = "DATE", nullable = false)
	private Date date;

	@NotBlank(message = "invalid name")
	private String name;

	@NotBlank(message = "invalid type")
	private String type;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_FK")
	@NotNull(message = "invalid User")
	private User user;

	@Lob
	private byte[] data;

	// ======================================
		// = Constructors =
		// ======================================
	public File() {
		id = 0L;
		this.date = new Date();
		user = new User();
	}

	public File(String name, String type, User user, byte[] data) {
		this.date = new Date();
		setDate(date);
		setName(name);
		setType(type);
		setData(data);
		setUser(user);
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "File [id=" + id + ", date=" + date + ", name=" + name + ", type=" + type + ", user=" + user + ", data="
				+ Arrays.toString(data) + "]";
	}

	
}
