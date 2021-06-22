package com.sport.training.domain.model;

import java.io.Serializable;

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
 * This class represents an Activity in the sport system. The sport system is
 * divided into disciplines. Each one divided into activities and each activity
 * in event.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_ACTIVITY")
public class Activity implements Serializable {

	// ======================================
	// = Attributes =
	// ======================================
	@NotBlank(message = "id must be defined")
	@Id
	private String id;

	@NotBlank(message = "invalid Name")
	private String name;

	@NotBlank(message = "invalid Description")
	private String description;

	@Column(name = "CREDITCOSTMIN")
	@Positive(message = "invalid Creditcost min")
	private Double creditcostMin;

	@Column(name = "CREDITCOSTMAX")
	@Positive(message = "invalid Creditcost max")
	private Double creditcostMax;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DISCIPLINE_FK")
	@NotNull(message = "invalid Discipline")
	private Discipline discipline;

	// ======================================
	// = Constructors =
	// ======================================
	public Activity() {
	}

	public Activity(final String id) {
		setId(id);
	}

	public Activity(final String id, final String name, final String description, final Discipline discipline) {
		setId(id);
		setName(name);
		setDescription(description);
		setDiscipline(discipline);
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

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public Double getCreditcostMin() {
		return creditcostMin;
	}

	public void setCreditcostMin(Double creditcostMin) {
		this.creditcostMin = creditcostMin;
	}

	public Double getCreditcostMax() {
		return creditcostMax;
	}

	public void setCreditcostMax(Double creditcostMax) {
		this.creditcostMax = creditcostMax;
	}

	public Discipline getDiscipline() {
		return discipline;
	}

	public void setDiscipline(final Discipline discipline) {
		this.discipline = discipline;
	}

	@Override
	public String toString() {
		return "Activity [id=" + id + ", name=" + name + ", description=" + description + ", creditcostMin="
				+ creditcostMin + ", creditcostMax=" + creditcostMax + ", discipline=" + discipline + "]";
	}

}
