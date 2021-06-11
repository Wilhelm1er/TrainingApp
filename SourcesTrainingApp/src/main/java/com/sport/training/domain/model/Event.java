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
import javax.validation.constraints.Positive;

import com.sport.training.authentication.domain.model.User;

/**
 * This class represents an Event the Sport system. The sport system is divided
 * into disciplines. Each one divided into activities and each activity in
 * event.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_EVENT")
public class Event implements Serializable {

	// ======================================
	// = Attributes =
	// ======================================
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "event_seq_gen")
	private Long id;

	@NotBlank(message = "invalid Name")
	private String name;

	@Column(name = "DATE")
	private LocalDateTime datetime;

	@Column(name = "CREDITCOST")
	@Positive(message = "invalid Creditcost")
	private int creditCost;

	@Column(name = "DURATION")
	@Positive(message = "invalid Duration")
	private int duration;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "INTENSITY")
	private int intensity;

	@Column(name = "EQUIPMENT")
	private String equipment;

	@Column(name = "VOIDABLE")
	private int voidable;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COACH_FK")
	@NotNull(message = "invalid Coach")
	private User coach;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACTIVITY_FK")
	@NotNull(message = "invalid Activity")
	private Activity activity;

	// ======================================
	// = Constructors =
	// ======================================
	public Event() {
	}

	public Event(final String name, final LocalDateTime datetime, final Date time, final int creditCost,
			final User coach, final Activity activity) {
		setVoidable(0);
		setName(name);
		setDateTime(datetime);
		setCreditCost(creditCost);
		setCoach(coach);
		setActivity(activity);
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

	public void setName(final String name) {
		this.name = name;
	}

	public LocalDateTime getDateTime() {
		return datetime;
	}

	public void setDateTime(LocalDateTime date) {
		this.datetime = date;
	}

	public int getVoidable() {
		return voidable;
	}

	public void setVoidable(int voidable) {
		this.voidable = voidable;
	}

	public int getCreditCost() {
		return creditCost;
	}

	public void setCreditCost(int creditCost) {
		this.creditCost = creditCost;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getIntensity() {
		return intensity;
	}

	public void setIntensity(int intensity) {
		this.intensity = intensity;
	}

	public String getEquipment() {
		return equipment;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

	public User getCoach() {
		return coach;
	}

	public void setCoach(User coach) {
		this.coach = coach;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", name=" + name + ", datetime=" + datetime + ", creditCost=" + creditCost
				+ ", duration=" + duration + ", description=" + description + ", intensity=" + intensity
				+ ", equipment=" + equipment + ", voidable=" + voidable + ", coach=" + coach + ", activity=" + activity
				+ "]";
	}

}
