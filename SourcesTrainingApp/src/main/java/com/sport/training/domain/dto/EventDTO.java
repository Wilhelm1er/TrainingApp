package com.sport.training.domain.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import com.sport.training.authentication.domain.dto.UserDTO;

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
	private Long id;
	private String name;
	private LocalDateTime datetime;
	private Double creditCost;
	private int duration;
	private int intensity;
	private int voidable;
	private String description;
	private String equipment;
	private UserDTO coachDTO;
	private ActivityDTO activityDTO;

	// ======================================
	// = Constructors =
	// ======================================
	public EventDTO() {
	}

	public EventDTO(final String name, final LocalDateTime datetime, final Double creditCost, final UserDTO coachDTO,
			final ActivityDTO activityDTO) {
		setVoidable(0);
		setName(name);
		setDateTime(datetime);
		setCreditCost(creditCost);
		setCoachDTO(coachDTO);
		setActivityDTO(activityDTO);
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

	public void setDateTime(LocalDateTime datetime) {
		this.datetime = datetime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
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

	public Double getCreditCost() {
		return creditCost;
	}

	public void setCreditCost(final Double creditCost) {
		this.creditCost = creditCost;
	}

	public int getVoidable() {
		return voidable;
	}

	public void setVoidable(int voidable) {
		this.voidable = voidable;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UserDTO getCoachDTO() {
		return coachDTO;
	}

	public void setCoachDTO(UserDTO coachDTO) {
		this.coachDTO = coachDTO;
	}

	public ActivityDTO getActivityDTO() {
		return activityDTO;
	}

	public void setActivityDTO(ActivityDTO activityDTO) {
		this.activityDTO = activityDTO;
	}

	@Override
	public String toString() {
		return "EventDTO [id=" + id + ", name=" + name + ", datetime=" + datetime + ", creditCost=" + creditCost
				+ ", duration=" + duration + ", intensity=" + intensity + ", voidable=" + voidable + ", description="
				+ description + ", equipment=" + equipment + ", coachDTO=" + coachDTO + ", activityDTO=" + activityDTO
				+ "]";
	}

}
