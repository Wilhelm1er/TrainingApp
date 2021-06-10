package com.sport.training.domain.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import com.sport.training.domain.dto.CreditRegistryDTO;
import com.sport.training.domain.dto.DisciplineDTO;
import com.sport.training.domain.dto.DisciplineRegistryDTO;
import com.sport.training.domain.dto.EventDTO;
import com.sport.training.domain.dto.EventRegistryDTO;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.RemoveException;
import com.sport.training.exception.UpdateException;
import com.sport.training.authentication.domain.dto.UserDTO;

public interface RegistryService {
	// ======================================
	// = DisciplineRegistry Business methods=
	// ======================================
	public DisciplineRegistryDTO createDisciplineRegistry(@Valid final DisciplineRegistryDTO disciplineRegistryDTO)
			throws CreateException, FinderException;

	public DisciplineRegistryDTO findDisciplineRegistry(final Long disciplineRegistryId) throws FinderException;

	public void deleteDisciplineRegistry(final Long disciplineRegistryId) throws FinderException, RemoveException;

	public void updateDisciplineRegistry(@Valid final DisciplineRegistryDTO disciplineRegistryDTO)
			throws UpdateException;

	public List<DisciplineRegistryDTO> findDisciplineRegistries() throws FinderException;

	public Set<UserDTO> findCoachsByDiscipline(final String disciplineId) throws FinderException;
	
	public Set<DisciplineDTO> findDisciplinesByCoach(String coachId, String statutDoc) throws FinderException;
	
	public Set<DisciplineDTO> findAllDisciplinesByCoach(String coachId) throws FinderException;

	// ======================================
	// = EventRegistry Business methods =
	// ======================================
	public EventRegistryDTO createEventRegistry(@Valid final EventRegistryDTO eventRegistryDTO) throws CreateException;

	public EventRegistryDTO findEventRegistry(final Long eventRegistryId) throws FinderException;

	public void deleteEventRegistry(final Long eventRegistryId) throws FinderException, RemoveException;

	public void updateEventRegistry(@Valid final EventRegistryDTO eventRegistryDTO) throws UpdateException;

	public List<EventRegistryDTO> findEventRegistries() throws FinderException;
	
	List<EventDTO> findEventsByAthlete(String athleteId) throws FinderException;
	
	EventRegistryDTO findEventRegistryByAthleteAndEvent(String coachId, Long eventId) throws FinderException;
	
	public List<UserDTO> findAthleteByEvent(Long eventId) throws FinderException;
	

	// ======================================
	// = Credit Business methods =
	// ======================================
	public CreditRegistryDTO createCreditRegistry(@Valid final CreditRegistryDTO creditRegistryDTO)
			throws CreateException;

	public CreditRegistryDTO findCreditRegistry(final Long creditRegistryId) throws FinderException;

	public void deleteCreditRegistry(final Long creditRegistryId) throws FinderException, RemoveException;

	public void updateCreditRegistry(@Valid final CreditRegistryDTO creditRegistryDTO) throws UpdateException;

	public List<CreditRegistryDTO> findCreditRegistries() throws FinderException;

	public Map<Date, Integer> findDateAndCreditByUser(final String userId) throws FinderException;

	
	
}