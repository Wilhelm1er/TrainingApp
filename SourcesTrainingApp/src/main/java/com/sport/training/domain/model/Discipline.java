package com.sport.training.domain.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * This class represents a Discipline in the sport system.
 * The sport system is divided into disciplines. Each one divided into activities
 * and each activity in event.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_DISCIPLINE")  
public class Discipline implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================
	@NotBlank(message = "id must be defined")
	@Id
	private String id;
	@NotBlank(message = "invalid Name")
    private String name;
    @NotBlank(message = "invalid Description")
    private String description;
    @NotBlank(message = "invalid Documents")
    private String documents;

	// ======================================
    // =            Constructors            =
    // ======================================
    public Discipline() {}

    public Discipline(final String id) {
        setId(id);
    }

    public Discipline(final String id, final String name, final String description) {
        setId(id);
        setName(name);
        setDescription(description);
    }

    // ======================================
    // =         Getters and Setters        =
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

}
