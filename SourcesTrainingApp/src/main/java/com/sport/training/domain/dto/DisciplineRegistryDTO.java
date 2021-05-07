package com.sport.training.domain.dto;

import java.io.Serializable;
import java.util.Date;

import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.model.User;

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

    private UserDTO coachDTO;
    private DisciplineDTO disciplineDTO;
	private String id;
	private Date registerDate;

	// ======================================
    // =            Constructors            =
    // ======================================
    public DisciplineRegistryDTO() {
    	this.registerDate=new Date();
    }

    public DisciplineRegistryDTO(final DisciplineDTO disciplineDTO,final UserDTO coachDTO) {
    	this.registerDate=new Date();
    	setRegisterDate(registerDate);
    	setDisciplineDTO(disciplineDTO);
        setCoachDTO(coachDTO);
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
	
	private Object getRegisterDate() {
		return registerDate;
	}
    
    private void setCoachDTO(UserDTO coachDTO) {
    	this.coachDTO = coachDTO;
		
	}
    
	private void setDisciplineDTO(DisciplineDTO disciplineDTO) {
		this.disciplineDTO = disciplineDTO;
		
	}

	public DisciplineDTO getDisciplineDTO() {
		return disciplineDTO;
	}

	private void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
		
	}
	
	private UserDTO getCoachDTO() {
		return coachDTO;
	}
	
	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append("Discipline RegisterDTO{");
		buf.append("id=").append(getId());
		buf.append(",registerDate=").append(getRegisterDate());
	        buf.append(",disciplineID=").append(getId());
	        buf.append(",coachID=").append(getCoachDTO().getUsername());
		buf.append('}');
		return buf.toString();
	}




}
