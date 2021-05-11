package com.sport.training.domain.service;

import java.util.List;

import javax.validation.Valid;

import com.sport.training.domain.dto.DisciplineDTO;
import com.sport.training.domain.dto.DisciplineRegistryDTO;
import com.sport.training.domain.dto.EventRegistryDTO;
import com.sport.training.domain.model.Discipline;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.RemoveException;
import com.sport.training.exception.UpdateException;
import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.model.User;


public interface RegistryService  {
	// ======================================
    // = DisciplineRegistry Business methods=
    // ======================================
	public DisciplineRegistryDTO createDisciplineRegistry(@Valid final DisciplineRegistryDTO disciplineRegistryDTO) throws CreateException ;
    public DisciplineRegistryDTO findDisciplineRegistry(final Long disciplineRegistryId) throws FinderException ;
    public void deleteDisciplineRegistry(final Long disciplineRegistryId) throws FinderException, RemoveException ;
    public void updateDisciplineRegistry(@Valid final DisciplineRegistryDTO disciplineRegistryDTO) throws UpdateException ;
    public List<DisciplineRegistryDTO> findDisciplineRegistries() throws FinderException ;
    public List<UserDTO> findCoachsByDiscipline(final String disciplineId) throws FinderException ;
    public List<DisciplineDTO> findDisciplinesByCoach(String coachId) throws FinderException;
    
    // ======================================
    // =   EventRegistry Business methods   =
    // ======================================
	public EventRegistryDTO createEventRegistry(@Valid final EventRegistryDTO eventRegistryDTO) throws CreateException ;
    public EventRegistryDTO findEventRegistry(final Long eventRegistryId) throws FinderException ;
    public void deleteEventRegistry(final Long eventRegistryId) throws FinderException, RemoveException ;
    public void updateEventRegistry(@Valid final EventRegistryDTO eventRegistryDTO) throws UpdateException ;
    public List<EventRegistryDTO> findEventRegistries() throws FinderException ;
	
	
    
}