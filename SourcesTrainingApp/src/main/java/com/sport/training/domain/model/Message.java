package com.sport.training.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.sport.training.authentication.domain.model.User;

/**
 * This class represents an Event the Sport system. The sport system
 * is divided into disciplines. Each one divided into activities and each activity
 * in event.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_MESSAGE")
public class Message implements Serializable {

	// ======================================
	// = Attributes =
	// ======================================
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "msg_seq_gen")
	private Long id;
	
	@Column(name = "DATE", nullable = false)
	private Date date;

	@NotBlank(message = "invalid texte")
	private String texte = "";

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SENDER")
	@NotNull(message = "invalid Sender")
	private User sender;
 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RECIPIENT")
	@NotNull(message = "invalid Recipient")
	private User recipient;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="DISCUSSION_FK")
	@NotNull(message = "invalid Discussion")
	private Discussion discussion;

	// ======================================
	// = Constructors =
	// ======================================

	public Message() {
		id=0L;
		this.date=new Date();
    	sender=new User();
    	recipient=new User();
    	discussion=new Discussion();
		
	}
	
	public Message(final String texte, final User sender, final User recipient, Discussion discussion) {
		this.date=new Date();
		setDate(date);
		this.texte = texte;
		setSender(sender);
		setRecipient(recipient);
		setDiscussion(discussion);
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

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getRecipient() {
		return recipient;
	}

	public void setRecipient(User recipient) {
		this.recipient = recipient;
	}
	
	public Discussion getDiscussion() {
		return discussion;
	}

	public void setDiscussion(Discussion discussion) {
		this.discussion = discussion;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", date=" + date + ", texte=" + texte + ", sender=" + sender + ", recipient="
				+ recipient + ", discussion=" + discussion + "]";
	}


}
