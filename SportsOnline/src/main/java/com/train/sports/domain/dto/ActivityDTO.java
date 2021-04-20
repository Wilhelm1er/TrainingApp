package com.train.sports.domain.dto;

import java.io.Serializable;

/**
 * This class follows the Data Transfert Object design pattern.
 * It is a client view of an Activity. This class only
 * transfers data from a distant service to a client.
 */
@SuppressWarnings("serial")
public class ActivityDTO implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================
    private String id;
    private String name;
    private String description;
    private DisciplineDTO disciplineDTO;

    // ======================================
    // =            Constructors            =
    // ======================================
    public ActivityDTO() {
    }

    public ActivityDTO(final String id, final String name, final String description) {
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

    public void setDescription(final String documents) {
    	this.description = documents;
    }

    public DisciplineDTO getDisciplineDTO() {
		return disciplineDTO;
	}

	public void setDisciplineDTO(DisciplineDTO disciplineDTO) {
		this.disciplineDTO = disciplineDTO;
	}

    @Override
	public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("ProductDTO{");
        buf.append("id=").append(getId());
        buf.append(",name=").append(getName());
        buf.append(",description=").append(getDescription());
        buf.append(",disciplineId=").append(getDisciplineDTO().getId());
        buf.append(",disciplineName=").append(getDisciplineDTO().getName());
        buf.append('}');
        return buf.toString();
    }
}
