package com.sport.training.domain.dto;

import java.io.Serializable;

/**
 * This class follows the Data Transfert Object design pattern. It is a client
 * view of a Discipline. This class only transfers data from a distant service
 * to a client.
 */
@SuppressWarnings("serial")
public class DisciplineDTO implements Serializable {

	// ======================================
	// = Attributes =
	// ======================================
	private String id;
	private String name;
	private String description;
	private String documents;

	// ======================================
	// = Constructors =
	// ======================================
	public DisciplineDTO() {
	}

	public DisciplineDTO(final String id) {
		this.id = id;
	}

	public DisciplineDTO(final String id, final String name, final String description) {
		this.id = id;
		this.name = name;
		this.description = description;
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

	public String getDocuments() {
		return documents;
	}

	public void setDocuments(String documents) {
		this.documents = documents;
	}

	@Override
	public String toString() {
		return "DisciplineDTO [id=" + id + ", name=" + name + ", description=" + description + ", documents="
				+ documents + "]";
	}

}
