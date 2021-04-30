package com.sport.training.domain.model;

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

import com.sport.training.authentication.domain.model.User;

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
 
	@Column(name = "DATE")
	private Date date;

	@Column(name = "CREDITCOST")
	@Positive(message = "invalid Creditcost")
	private int creditCost;
	
	@Column(name = "DURATION")
	@Positive(message = "invalid Duration")
	private int duration;

	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "INTENSITY")
	private String intensity;
	
	@Column(name = "EQUIPMENT")
	private String equipment;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_FK")
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

	public Event(final String id) {
		setId(id);
	}
	
	public Event(final String id, final String name,  final int creditCost, final User coach,
			final Activity activity) {
		setId(id);
		setName(name);
		setCreditCost(creditCost);
		setCoach(coach);
		setActivity(activity);
	}

	public Event(final String id, final String name, final Date date, final int creditCost, final User coach,
			final Activity activity) {
		setId(id);
		setName(name);
		setDate(date);
		setCreditCost(creditCost);
		setCoach(coach);
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
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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
	
	public String getIntensity() {
		return intensity;
	}

	public void setIntensity(String intensity) {
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
		final StringBuffer buf = new StringBuffer();
		buf.append("Event{");
		buf.append("id=").append(getId());
		buf.append(",name=").append(getName());
		buf.append(",duration=").append(getDuration());
		buf.append(",date=").append(getDate());
		buf.append(",intensity=").append(getIntensity());
		buf.append(",equipment=").append(getEquipment());
		buf.append(",creditCost=").append(getCreditCost());
		buf.append(",coachFirstname=").append(getCoach().getFirstname());
		buf.append(",coachLastname=").append(getCoach().getLastname());
		buf.append(",activityId=").append(getActivity().getId());
		buf.append(",activityName=").append(getActivity().getName());
		buf.append(",activityDescription=").append(getActivity().getDescription());
		buf.append('}');
		return buf.toString();
	}
}
