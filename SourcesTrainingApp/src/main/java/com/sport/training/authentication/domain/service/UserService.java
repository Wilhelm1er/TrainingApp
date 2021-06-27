package com.sport.training.authentication.domain.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.model.Role;
import com.sport.training.domain.dto.CreditUserDTO;
import com.sport.training.domain.dto.DisciplineDTO;
import com.sport.training.domain.dto.DisciplineUserDTO;
import com.sport.training.domain.dto.EventDTO;
import com.sport.training.domain.dto.EventUserDTO;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.RemoveException;
import com.sport.training.exception.UpdateException;

public interface UserService {

	 public UserDTO createUser(@Valid final UserDTO userDTO) throws CreateException, FinderException;

	    public UserDTO findUser(final String userId) throws FinderException;

	    public void deleteUser(final String userId) throws RemoveException, FinderException;

	    public void updateUser(@Valid final UserDTO userDTO) throws UpdateException, FinderException;

	    public List<UserDTO> findUsers() throws FinderException;

		public List<UserDTO> findUsersByRole(Role role) throws FinderException;
		
		// ======================================
		// = DisciplineUser Business methods=
		// ======================================
		public DisciplineUserDTO createDisciplineUser(@Valid final DisciplineUserDTO disciplineUserDTO)
				throws CreateException, FinderException;

		public DisciplineUserDTO findDisciplineUser(final Long disciplineUserId) throws FinderException;

		public void deleteDisciplineUser(final Long disciplineUserId) throws FinderException, RemoveException;

		public void updateDisciplineUser(@Valid final DisciplineUserDTO disciplineUserDTO)
				throws UpdateException;

		public List<DisciplineUserDTO> findDisciplineUsers() throws FinderException;

		public Set<UserDTO> findCoachsByDiscipline(final String disciplineId) throws FinderException;

		public List<DisciplineDTO> findDisciplinesByCoach(String coachId) throws FinderException;

		public List<DisciplineDTO> findDisciplineToCheckByCoach(String coachId) throws FinderException;

		public List<DisciplineDTO> findDisciplineOkByCoach(String coachId) throws FinderException;

		// ======================================
		// = EventUser Business methods =
		// ======================================
		public EventUserDTO createEventUser(@Valid final EventUserDTO eventUserDTO) throws CreateException;

		public EventUserDTO findEventUser(final Long eventUserId) throws FinderException;

		public void deleteEventUser(final Long eventUserId) throws FinderException, RemoveException;

		public void updateEventUser(@Valid final EventUserDTO eventUserDTO) throws UpdateException;

		public List<EventUserDTO> findEventUsers() throws FinderException;

		List<EventDTO> findEventsByAthlete(String athleteId) throws FinderException;

		EventUserDTO findEventUserByAthleteAndEvent(String coachId, Long eventId) throws FinderException;

		public List<UserDTO> findAthleteByEvent(Long eventId) throws FinderException;

		// ======================================
		// = Credit User Business methods =
		// ======================================
		public CreditUserDTO createCreditUser(@Valid final CreditUserDTO creditUserDTO)
				throws CreateException;

		public CreditUserDTO findCreditUser(final Long creditUserId) throws FinderException;

		public void deleteCreditUser(final Long creditUserId) throws FinderException, RemoveException;

		public void updateCreditUser(@Valid final CreditUserDTO creditUserDTO) throws UpdateException;

		public List<CreditUserDTO> findCreditUsers() throws FinderException;

		public Map<Date, Integer> findDateAndCreditByUser(final String userId) throws FinderException;

}


