package com.train.sports.domain.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * This class represents an Event the Sport system. The sport system
 * is divided into disciplines. Each one divided into activities and each activity
 * in event.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_EVENT")
public class Event implements Serializable {

	// ======================================
	// = Attributes =
	// ======================================
	@NotBlank(message = "id must be defined")
	@Id
	private String id;

	@NotBlank(message = "invalid Name")
	private String name;
 
	@Column(name = "TIMETABLE")
	private Date timetable;

	@Column(name = "CREDITCOST")
	@Positive(message = "invalid Creditcost")
	private int creditCost;
	
	@Column(name = "DURATION")
	@Positive(message = "invalid Duration")
	private int duration;

	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "EQUIPMENT")
	private String equipment;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACTIVITY_FK")
	@NotNull(message = "invalid Activity")
	private Activity activity;

	// ======================================
	// = Constructors =
	// ======================================
	public Event() {
	}

	public Event(final String id) {
		setId(id);
	}
	
	public Event(final String id, final String name,  final int creditCost,
			final Activity activity) {
		setId(id);
		setName(name);
		setCreditCost(creditCost);
		setActivity(activity);
	}

	public Event(final String id, final String name, final Date timetable, final int creditCost,
			final Activity activity) {
		setId(id);
		setName(name);
		setTimetable(timetable);
		setCreditCost(creditCost);
		setActivity(activity);
	}

	// ======================================
	// = Getters and Setters =
	// ======================================

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
	
	public Date getTimetable() {
		return timetable;
	}

	public void setTimetable(Date timetable) {
		this.timetable = timetable;
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

	public String getEquipment() {
		return equipment;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", name=" + name + ", timetable=" + timetable + ", creditCost=" + creditCost
				+ ", duration=" + duration + ", description=" + description + ", equipment=" + equipment + "]";
	}

	
}
