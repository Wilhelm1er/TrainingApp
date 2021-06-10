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
	private String docStatut;

	// ======================================
    // =            Constructors            =
    // ======================================
    public DisciplineRegistryDTO(DisciplineDTO disciplineDTO, UserDTO coachDTO) {
    	docStatut="no";
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

	public String getDocStatut() {
		return docStatut;
	}

	public void setDocStatut(String docStatut) {
		this.docStatut = docStatut;
	}

	@Override
	public String toString() {
		return "DisciplineRegistryDTO [id=" + id + ", registerDate=" + registerDate + ", coachDTO=" + coachDTO
				+ ", disciplineDTO=" + disciplineDTO + ", docStatut=" + docStatut + "]";
	}

}
