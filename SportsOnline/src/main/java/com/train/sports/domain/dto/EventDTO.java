package com.train.sports.domain.dto;

import java.io.Serializable;
import java.sql.Date;

/**
 * This class follows the Data Transfert Object design pattern. It is a client
 * view of an Event. This class only transfers data from a distant service to a
 * client.
 */
@SuppressWarnings("serial")
public class EventDTO implements Serializable {

	// ======================================
	// = Attributes =
	// ======================================
	private String id;
	private String name;
	private Date timetable;
	private int creditCost;
	private int duration;
	private String equipment;
	private ActivityDTO activityDTO;

	// ======================================
	// = Constructors =
	// ======================================
	public EventDTO() {
	}

	public EventDTO(final String id, final String name, final int creditCost) {
		setId(id);
		setName(name);
		setCreditCost(creditCost);
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

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getEquipment() {
		return equipment;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

	public int getCreditCost() {
		return creditCost;
	}

	public void setCreditCost(final int creditCost) {
		this.creditCost = creditCost;
	}

	public ActivityDTO getActivityDTO() {
		return activityDTO;
	}

	public void setActivityDTO(ActivityDTO activityDTO) {
		this.activityDTO = activityDTO;
	}

	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append("ItemDTO{");
		buf.append("id=").append(getId());
		buf.append(",name=").append(getName());
		buf.append(",duration=").append(getDuration());
		buf.append(",timetable=").append(getTimetable());
		buf.append(",equipment=").append(getEquipment());
		buf.append(",creditCost=").append(getCreditCost());
		buf.append(",activityId=").append(getActivityDTO().getId());
		buf.append(",activityName=").append(getActivityDTO().getName());
		buf.append(",activityDescription=").append(getActivityDTO().getDescription());
		buf.append('}');
		return buf.toString();
	}
}
