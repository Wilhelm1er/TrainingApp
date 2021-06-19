package com.sport.training.domain.dto;

import java.io.Serializable;

/**
 * This class follows the Data Transfert Object design pattern. It is a client
 * view of an Activity. This class only transfers data from a distant service to
 * a client.
 */
@SuppressWarnings("serial")
public class ActivityDTO implements Serializable {

	// ======================================
	// = Attributes =
	// ======================================
	private String id;
	private String name;
	private String description;
	private Double creditcostMin;
	private Double creditcostMax;
	private DisciplineDTO disciplineDTO;

	// ======================================
	// = Constructors =
	// ======================================
	public ActivityDTO() {
	}

	public ActivityDTO(final String id) {
		this.id = id;
	}

	public ActivityDTO(final String id, final String name, final String description) {
		setId(id);
		setName(name);
		setDescription(description);
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

	public void setDescription(final String documents) {
		this.description = documents;
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

	public DisciplineDTO getDisciplineDTO() {
		return disciplineDTO;
	}

	public void setDisciplineDTO(DisciplineDTO disciplineDTO) {
		this.disciplineDTO = disciplineDTO;
	}

	@Override
	public String toString() {
		return "ActivityDTO [id=" + id + ", name=" + name + ", description=" + description + ", creditcostMin="
				+ creditcostMin + ", creditcostMax=" + creditcostMax + ", disciplineDTO=" + disciplineDTO + "]";
	}

}
