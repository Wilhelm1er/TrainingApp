package com.sport.training.domain.dto;

import java.io.Serializable;
import java.util.Date;

import com.sport.training.authentication.domain.dto.UserDTO;

/**
 * This class follows the Data Transfert Object design pattern. It is a client
 * view of a discipline registration. This class only transfers data from a distant service to a
 * client.
 */
@SuppressWarnings("serial")
public class DisciplineRegistryDTO implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================

	private Long id;
	private Date registerDate;
	private UserDTO coachDTO;
	private DisciplineDTO disciplineDTO;

	// ======================================
    // =            Constructors            =
    // ======================================
    public DisciplineRegistryDTO(DisciplineDTO disciplineDTO, UserDTO coachDTO) {
    	setCoachDTO(coachDTO);
    	setDisciplineDTO(disciplineDTO);
    	
    }

    // ======================================
    // =         Getters and Setters        =
    // ======================================
    
	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}
	
	public Date getRegisterDate() {
		return registerDate;
	}
    
	public void setCoachDTO(UserDTO coachDTO) {
    	this.coachDTO = coachDTO;
		
	}
	
    public DisciplineDTO getDisciplineDTO() {
		return disciplineDTO;
	}

	public void setDisciplineDTO(DisciplineDTO disciplineDTO) {
		this.disciplineDTO = disciplineDTO;
	}
	
	public UserDTO getCoachDTO() {
		return coachDTO;
	}
	
	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append("Discipline RegisterDTO{");
		buf.append("id=").append(getId());
		buf.append(",registerDate=").append(getRegisterDate());
	        buf.append(",disciplineID=").append(getDisciplineDTO().getId());
	        buf.append(",coachID=").append(getCoachDTO().getUsername());
		buf.append('}');
		return buf.toString();
	}

}
