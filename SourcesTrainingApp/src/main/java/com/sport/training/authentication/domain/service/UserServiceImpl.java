package com.sport.training.authentication.domain.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.sport.training.authentication.domain.dao.UserRepository;
import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.model.Role;
import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.dao.CreditUserRepository;
import com.sport.training.domain.dao.DisciplineUserRepository;
import com.sport.training.domain.dao.DisciplineRepository;
import com.sport.training.domain.dao.EventUserRepository;
import com.sport.training.domain.dao.EventRepository;
import com.sport.training.domain.dto.CreditUserDTO;
import com.sport.training.domain.dto.DisciplineDTO;
import com.sport.training.domain.dto.DisciplineUserDTO;
import com.sport.training.domain.dto.EventDTO;
import com.sport.training.domain.dto.EventUserDTO;
import com.sport.training.domain.model.CreditUser;
import com.sport.training.domain.model.Discipline;
import com.sport.training.domain.model.DisciplineUser;
import com.sport.training.domain.model.Event;
import com.sport.training.domain.model.EventUser;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.DuplicateKeyException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.RemoveException;
import com.sport.training.exception.UpdateException;

/**
 * This class is a facade for all user services.
 */

@Service
@Validated
public class UserServiceImpl implements UserService {

	// ======================================
	// = Attributes =
	// ======================================

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	private ModelMapper commonModelMapper,userModelMapper, userDTOModelMapper, eventUserModelMapper, eventModelMapper,
			disciplineUserModelMapper;

	@Autowired
	private DisciplineUserRepository disciplineUserRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private CreditUserRepository creditUserRepository;

	@Autowired
	private EventUserRepository eventUserRepository;

	@Autowired
	private DisciplineRepository disciplineRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;


	// ======================================
	// = Constructors =
	// ======================================
	public UserServiceImpl() {
	}

	// ======================================
	// = Business methods =
	// ======================================
	@Override
	@Transactional
	public UserDTO createUser(@Valid UserDTO userDTO) throws CreateException, FinderException {
		final String mname = "createUser";
		LOGGER.debug("entering " + mname);

		if (userDTO == null)
			throw new CreateException("User object is null");

		try {
			if (findUser(userDTO.getUsername()) != null)
				throw new DuplicateKeyException();
		} catch (FinderException e) {
		}

		if (userDTO.getPassword().length() < 4)
			throw new CreateException("password's length exception (mini. of 4 char. required)");
		
		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		User user = userModelMapper.map(userDTO, User.class);

		// Encrypting the password before storing it in the database
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

		// make sure that the user has a role
		if (user.getRole() == null || user.getRole().getName() == null) {
			try {
				user.setRole(roleService.findByRoleName("ROLE_ATHLETE"));
			} catch (FinderException e) {
				LOGGER.error("role missing for athlete");
			}
		} else if (user.getRole().getId() == null || user.getRole().getId() == 0) {
			try {
				user.setRole(roleService.findByRoleName(user.getRole().getName()));
			} catch (FinderException e) {
				LOGGER.error("role missing for athlete");
			}
		}
		
		// set Statut by default for coach and athlete
		if (user.getRole().getId() ==  2 || user.getRole().getName() == "ROLE_COACH") {
				user.setStatut("INVALIDE");
			} 
		if (user.getRole().getId() ==  3 || user.getRole().getName() == "ROLE_ATHLETE") {
			user.setStatut("VALIDE");
		} 
		
		// Creates the object
		userRepository.save(user);
		
		UserDTO result = userDTOModelMapper.map(user, UserDTO.class);
		LOGGER.debug("exiting " + mname);
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDTO findUser(final String username) throws FinderException {
		final String mname = "findUser";
		LOGGER.debug("entering " + mname + " with " + username);

		checkStringId(username);
		// Finds the object
		User user = null;
		if (!userRepository.findById(username).isPresent())
			throw new FinderException("User must exist to be found");
		else
			user = userRepository.findById(username).get();

		UserDTO result = userModelMapper.map(user, UserDTO.class);
		LOGGER.debug("exiting " + mname);
		return result;
	}

	@Override
	@Transactional
	public void deleteUser(final String username) throws RemoveException, FinderException {
		final String mname = "deleteUser";
		LOGGER.debug("entering " + mname + " with " + username);
		User user;
		checkStringId(username);
		if (!userRepository.findById(username).isPresent())
			throw new RemoveException("User must exist to be deleted");
		else
			user = userRepository.findById(username).get();

		userRepository.delete(user);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional
	public void updateUser(@Valid UserDTO userDTO) throws UpdateException, FinderException {
		final String mname = "updateUser";
		LOGGER.debug("entering " + mname);

		if (userDTO == null)
			throw new UpdateException("User object is null");
		if (userDTO.getPassword().length() < 4)
			throw new UpdateException("password's length exception (mini. of 4 char. required)");

		// Checks if the object exists
		if (!userRepository.findById(userDTO.getUsername()).isPresent())
			throw new UpdateException("User must exist to be updated");

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		User user = userModelMapper.map(userDTO, User.class);
		// Role must be set
		User userToUpdate = userRepository.findById(userDTO.getUsername()).get();
		user.setRole(userToUpdate.getRole());

		// Encrypting the password before storing it in the database
		user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

		// Updates the object
		userRepository.save(user);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDTO> findUsers() throws FinderException {
		final String mname = "findUsers";
		LOGGER.debug("entering " + mname);

		// Finds all the objects
		final Iterable<User> users = userRepository.findAll();
		int size;
		if ((size = ((Collection<User>) users).size()) == 0) {
			throw new FinderException("No user in the database");
		}
		List<UserDTO> usersDTO = ((List<User>) users)
								.stream()
								.map(user -> userDTOModelMapper.map(user, UserDTO.class))
								.collect(Collectors.toList());
		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return usersDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDTO> findUsersByRole(final Role role) throws FinderException {
		final String mname = "findUsersByRole";
		LOGGER.debug("entering " + mname);

		// Finds all the objects
		final Iterable<User> usersByRole = userRepository.findAllByRole(role);
		int size;
		if ((size = ((Collection<User>) usersByRole).size()) == 0) {
			throw new FinderException("No user in the database for role " + role.getName());
		}
		List<UserDTO> usersDTOByRole = ((List<User>) usersByRole)
										.stream()
										.map(user -> userDTOModelMapper.map(user, UserDTO.class))
										.collect(Collectors.toList());
		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return usersDTOByRole;
	}

	// =====================================
	// = DisciplineUser Business methods =
	// =====================================
	@Override
	@Transactional
	public DisciplineUserDTO createDisciplineUser(@Valid DisciplineUserDTO disciplineUserDTO)
			throws CreateException {
		final String mname = "createDisciplineUser";
		LOGGER.debug("entering " + mname);

		if (disciplineUserDTO == null)
			throw new CreateException("DisciplineUser object is null");

		if (disciplineUserDTO.getDisciplineDTO() == null || disciplineUserDTO.getDisciplineDTO().getId() == null
				|| !disciplineRepository.findById(disciplineUserDTO.getDisciplineDTO().getId()).isPresent())
			throw new CreateException("Discipline must exist to create a discipline registry");

		try {
			findUser(disciplineUserDTO.getCoachDTO().getUsername());
		} catch (NullPointerException | FinderException e) {
			throw new CreateException("User must exist to create a DisciplineUser");
		}

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		DisciplineUser disciplineUser = disciplineUserModelMapper.map(disciplineUserDTO,
				DisciplineUser.class);

		if (disciplineUserRepository.findByCoachAndDiscipline(disciplineUser.getCoach(),
				disciplineUser.getDiscipline()) != null) {
			throw new CreateException("DisciplineUser object already exist for this discipline: "
					+ disciplineUser.getDiscipline().getName());
		}
		// Creates the object
		disciplineUserRepository.save(disciplineUser);

		LOGGER.debug("exiting " + mname);
		return disciplineUserDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public DisciplineUserDTO findDisciplineUser(final Long disciplineUserId) throws FinderException {
		final String mname = "findDiscipline";
		LOGGER.debug("entering " + mname + " for id " + disciplineUserId);

		checkId(disciplineUserId);

		DisciplineUser disciplineUser = null;
		if (!disciplineUserRepository.findById(disciplineUserId).isPresent())
			throw new FinderException("Discipline must exist to be found");
		else
			disciplineUser = disciplineUserRepository.findById(disciplineUserId).get();

		LOGGER.debug("exiting " + mname);
		return commonModelMapper.map(disciplineUser, DisciplineUserDTO.class);
	}

	@Override
	@Transactional
	public void deleteDisciplineUser(final Long disciplineUserId) throws FinderException, RemoveException {
		final String mname = "deleteDisciplineUser";
		LOGGER.debug("entering " + mname + " for id " + disciplineUserId);

		checkId(disciplineUserId);

		DisciplineUser disciplineUser = null;
		if (!disciplineUserRepository.findById(disciplineUserId).isPresent())
			throw new RemoveException("Discipline registry must exist to be deleted");
		else
			disciplineUser = disciplineUserRepository.findById(disciplineUserId).get();
		// Deletes the object
		disciplineUserRepository.delete(disciplineUser);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional
	public void updateDisciplineUser(@Valid final DisciplineUserDTO updatedDisciplineUserDTO)
			throws UpdateException {
		final String mname = "updateDisciplineUser";
		LOGGER.debug("entering " + mname);

		if (updatedDisciplineUserDTO == null)
			throw new UpdateException("Discipline registry object is null");

		// Checks if the object exists
		if (!disciplineUserRepository.findById(updatedDisciplineUserDTO.getId()).isPresent())
			throw new UpdateException("Discipline registry must exist to be updated");

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		DisciplineUser updatedDisciplineUser = commonModelMapper.map(updatedDisciplineUserDTO,
				DisciplineUser.class);
		// Updates the object
		disciplineUserRepository.save(updatedDisciplineUser);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DisciplineUserDTO> findDisciplineUsers() throws FinderException {
		final String mname = "findDisciplineUsers";
		LOGGER.debug("entering " + mname);

		// Finds all the objects
		final Iterable<DisciplineUser> disciplineUsers = disciplineUserRepository.findAll();
		int size;
		if ((size = ((Collection<DisciplineUser>) disciplineUsers).size()) == 0) {
			throw new FinderException("No discipline in the database");
		}
		List<DisciplineUserDTO> disciplineUserDTOs = ((List<DisciplineUser>) disciplineUsers).stream()
				.map(disciplineUser -> commonModelMapper.map(disciplineUser, DisciplineUserDTO.class))
				.collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return disciplineUserDTOs;
	}

	@Override
	@Transactional(readOnly = true)
	public Set<UserDTO> findCoachsByDiscipline(String disciplineId) throws FinderException {
		final String mname = "findCoachsDiscipline";
		LOGGER.debug("entering " + mname);

		checkStringId(disciplineId);

		Discipline discipline = null;
		if (!disciplineRepository.findById(disciplineId).isPresent())
			throw new FinderException("Discipline must exist to be found");
		else
			discipline = disciplineRepository.findById(disciplineId).get();

		// Finds all the objects
		final Iterable<DisciplineUser> disciplineUsersByDiscipline = disciplineUserRepository
				.findAllByDiscipline(discipline);

		int size;
		if ((size = ((Collection<DisciplineUser>) disciplineUsersByDiscipline).size()) == 0) {
			throw new FinderException("No Coach in the database");
		}

		Set<User> coachList = new HashSet<User>();

		for (DisciplineUser discReg : disciplineUsersByDiscipline) {
			if (discReg.getCoach().getStatut().equals("VALIDE") && discReg.getDocStatut().equals("ok")) {
				coachList.add(discReg.getCoach());
			}
		}
		int size2;
		if ((size2 = ((Collection<User>) coachList).size()) == 0) {
			throw new FinderException("No Coach in the database");
		}

		Set<UserDTO> coachDTOs = (coachList.stream().map(coach -> userDTOModelMapper.map(coach, UserDTO.class))
				.collect(Collectors.toSet()));

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return coachDTOs;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DisciplineDTO> findDisciplinesByCoach(String coachId) throws FinderException {
		final String mname = "findDisciplinesByCoach";
		LOGGER.debug("entering " + mname);

		checkStringId(coachId);

		User coach = null;
		if (!userRepository.findById(coachId).isPresent())
			throw new FinderException("Coach must exist to be found");
		else
			coach = userRepository.findById(coachId).get();

		// Finds all the objects
		final Iterable<Discipline> disciplinesByCoach = disciplineUserRepository.findDisciplinesByCoach(coach);

		int size;
		if ((size = ((Collection<Discipline>) disciplinesByCoach).size()) == 0) {
			throw new FinderException("No Discipline in the database for this coach");
		}

		List<Discipline> disciplineList = new ArrayList<Discipline>();

		for (Discipline disc : disciplinesByCoach) {
			disciplineList.add(disc);
		}

		List<DisciplineDTO> disciplineDTOs = (disciplineList).stream()
				.map(discipline -> commonModelMapper.map(discipline, DisciplineDTO.class)).collect(Collectors.toList());

		LOGGER.debug("exiting " + mname);
		return disciplineDTOs;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DisciplineDTO> findDisciplineOkByCoach(String coachId) throws FinderException {
		final String mname = "findDisciplineOkByCoach";
		LOGGER.debug("entering " + mname);

		checkStringId(coachId);

		User coach = null;
		if (!userRepository.findById(coachId).isPresent())
			throw new FinderException("Coach must exist to be found");
		else
			coach = userRepository.findById(coachId).get();

		// Finds all the objects
		final Iterable<DisciplineUser> disciplineUsersByDiscipline = disciplineUserRepository
				.findAllByCoach(coach);

		int size;
		if ((size = ((Collection<DisciplineUser>) disciplineUsersByDiscipline).size()) == 0) {
			throw new FinderException("No Discipline in the database for this coach");
		}

		List<Discipline> disciplineList = new ArrayList<Discipline>();

		for (DisciplineUser discReg : disciplineUsersByDiscipline) {
			if (discReg.getDocStatut().equals("ok")) {
				disciplineList.add(discReg.getDiscipline());
			}
		}

		List<DisciplineDTO> disciplineDTOs = (disciplineList).stream()
				.map(discipline -> commonModelMapper.map(discipline, DisciplineDTO.class)).collect(Collectors.toList());

		LOGGER.debug("exiting " + mname);
		return disciplineDTOs;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DisciplineDTO> findDisciplineToCheckByCoach(String coachId) throws FinderException {
		final String mname = "findDisciplineToCheckByCoach";
		LOGGER.debug("entering " + mname);

		checkStringId(coachId);

		User coach = null;
		if (!userRepository.findById(coachId).isPresent())
			throw new FinderException("Coach must exist to be found");
		else
			coach = userRepository.findById(coachId).get();

		// Finds all the objects
		final Iterable<DisciplineUser> disciplineUsersByDiscipline = disciplineUserRepository
				.findAllByCoach(coach);

		int size;
		if ((size = ((Collection<DisciplineUser>) disciplineUsersByDiscipline).size()) == 0) {
			throw new FinderException("No Discipline in the database for this coach");
		}

		List<Discipline> disciplineList = new ArrayList<Discipline>();

		for (DisciplineUser discReg : disciplineUsersByDiscipline) {
			if (discReg.getDocStatut().equals("no")) {
				disciplineList.add(discReg.getDiscipline());
			}
		}

		List<DisciplineDTO> disciplineDTOs = (disciplineList).stream()
				.map(discipline -> commonModelMapper.map(discipline, DisciplineDTO.class)).collect(Collectors.toList());

		LOGGER.debug("exiting " + mname);
		return disciplineDTOs;
	}

	// =====================================
	// = EventUser Business methods =
	// =====================================
	@Override
	@Transactional
	public EventUserDTO createEventUser(@Valid EventUserDTO eventUserDTO) throws CreateException {
		final String mname = "createEventUser";
		LOGGER.debug("entering " + mname);

		if (eventUserDTO == null)
			throw new CreateException("EventUserDTO object is null");

		if (eventUserDTO.getEventDTO() == null || eventUserDTO.getEventDTO().getId() == null
				|| !eventRepository.findById(eventUserDTO.getEventDTO().getId()).isPresent())
			throw new CreateException("Event must exist to create an event registry");

		try {
			findUser(eventUserDTO.getUserDTO().getUsername());
		} catch (NullPointerException | FinderException e) {
			throw new CreateException("Athlete must exist to create an EventUser");
		}

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		EventUser eventUser = eventUserModelMapper.map(eventUserDTO, EventUser.class);

		if (eventUserRepository.findByUserAndEvent(eventUser.getUser(), eventUser.getEvent()) != null)
			throw new CreateException("EventUser object already exist");

		// Creates the object
		eventUserRepository.save(eventUser);

		LOGGER.debug("exiting " + mname);
		return eventUserDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public EventUserDTO findEventUser(final Long eventUserId) throws FinderException {
		final String mname = "findDiscipline";
		LOGGER.debug("entering " + mname + " for id " + eventUserId);

		checkId(eventUserId);

		EventUser eventUser = null;
		if (!eventUserRepository.findById(eventUserId).isPresent())
			throw new FinderException("Event must exist to be found");
		else
			eventUser = eventUserRepository.findById(eventUserId).get();

		LOGGER.debug("exiting " + mname);
		return commonModelMapper.map(eventUser, EventUserDTO.class);
	}

	@Override
	@Transactional
	public void deleteEventUser(final Long eventUserId) throws FinderException, RemoveException {
		final String mname = "deleteEventUser";
		LOGGER.debug("entering " + mname + " for id " + eventUserId);

		checkId(eventUserId);

		EventUser eventUser = null;
		if (!eventUserRepository.findById(eventUserId).isPresent())
			throw new RemoveException("Event registry must exist to be deleted");
		else
			eventUser = eventUserRepository.findById(eventUserId).get();
		// Deletes the object
		eventUserRepository.delete(eventUser);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional
	public void updateEventUser(@Valid final EventUserDTO updatedEventUserDTO) throws UpdateException {
		final String mname = "updateEventUser";
		LOGGER.debug("entering " + mname);

		if (updatedEventUserDTO == null)
			throw new UpdateException("Event registry object is null");

		// Checks if the object exists
		if (!eventUserRepository.findById(updatedEventUserDTO.getId()).isPresent())
			throw new UpdateException("Event registry must exist to be updated");

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		EventUser updatedEventUser = eventUserModelMapper.map(updatedEventUserDTO, EventUser.class);
		// Updates the object
		eventUserRepository.save(updatedEventUser);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EventUserDTO> findEventUsers() throws FinderException {
		final String mname = "findEventUsers";
		LOGGER.debug("entering " + mname);

		// Finds all the objects
		final Iterable<EventUser> eventUsers = eventUserRepository.findAll();
		int size;
		if ((size = ((Collection<EventUser>) eventUsers).size()) == 0) {
			throw new FinderException("No event in the database");
		}
		List<EventUserDTO> eventUserDTOs = ((List<EventUser>) eventUsers).stream()
				.map(eventUser -> eventUserModelMapper.map(eventUser, EventUserDTO.class))
				.collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return eventUserDTOs;
	}

	@Override
	@Transactional(readOnly = true)
	public List<EventDTO> findEventsByAthlete(String athleteId) throws FinderException {
		final String mname = "findEventsByAthlete";
		LOGGER.debug("entering " + mname);

		checkStringId(athleteId);

		User athlete = null;
		if (!userRepository.findById(athleteId).isPresent())
			throw new FinderException("Athlete must exist to be found");
		else
			athlete = userRepository.findById(athleteId).get();

		// Finds all the objects
		final Iterable<EventUser> eventUsersByAthlete = eventUserRepository.findAllByUser(athlete);

		int size;
		if ((size = ((Collection<EventUser>) eventUsersByAthlete).size()) == 0) {
			throw new FinderException("No Event in the database");
		}

		List<Event> eventList = new ArrayList<Event>();

		for (EventUser eventReg : eventUsersByAthlete) {
			eventList.add(eventReg.getEvent());

		}
		int size2;
		if ((size2 = ((Collection<Event>) eventList).size()) == 0) {
			throw new FinderException("No Event in the database");
		}

		List<EventDTO> eventDTOs = (eventList.stream().map(event -> eventModelMapper.map(event, EventDTO.class))
				.collect(Collectors.toList()));

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return eventDTOs;
	}

	@Override
	@Transactional(readOnly = true)
	public EventUserDTO findEventUserByAthleteAndEvent(String coachId, Long eventId) throws FinderException {
		final String mname = "findEventUserByAthleteAndEvent";
		LOGGER.debug("entering " + mname);

		checkStringId(coachId);
		checkId(eventId);

		User coach = null;
		if (!userRepository.findById(coachId).isPresent())
			throw new FinderException("Coach must exist to be found");
		else
			coach = userRepository.findById(coachId).get();

		Event event = null;
		if (!eventRepository.findById(eventId).isPresent())
			throw new FinderException("Event must exist to be found");
		else
			event = eventRepository.findById(eventId).get();

		// Finds all the objects
		final EventUser eventRegistriyByUserAndEvent = eventUserRepository.findByUserAndEvent(coach, event);

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		EventUserDTO EventUserDTO = eventUserModelMapper.map(eventRegistriyByUserAndEvent,
				EventUserDTO.class);

		LOGGER.debug("exiting " + mname);
		return EventUserDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDTO> findAthleteByEvent(Long eventId) throws FinderException {
		final String mname = "findAthleteByEvent";
		LOGGER.debug("entering " + mname);

		checkId(eventId);

		Event event = null;
		if (!eventRepository.findById(eventId).isPresent())
			throw new FinderException("Event must exist to be found");
		else
			event = eventRepository.findById(eventId).get();

		// Finds all the objects
		final Iterable<EventUser> eventUsersByEvent = eventUserRepository.findAllByEvent(event);

		int size;
		if ((size = ((Collection<EventUser>) eventUsersByEvent).size()) == 0) {
			throw new FinderException("No Event in the database");
		}

		List<User> userList = new ArrayList<User>();

		for (EventUser eventReg : eventUsersByEvent) {
			userList.add(eventReg.getUser());

		}
		int size2;
		if ((size2 = ((Collection<User>) userList).size()) == 0) {
			throw new FinderException("No User in the database");
		}

		List<UserDTO> userDTOs = (userList.stream().map(user -> userDTOModelMapper.map(user, UserDTO.class))
				.collect(Collectors.toList()));

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return userDTOs;
	}

	// ======================================
	// = Credit Business methods =
	// ======================================

	@Override
	@Transactional
	public CreditUserDTO createCreditUser(@Valid CreditUserDTO creditUserDTO) throws CreateException {
		final String mname = "createCreditUser";
		LOGGER.debug("entering " + mname);

		if (creditUserDTO == null)
			throw new CreateException("CreditUserDTO object is null");

		try {
			findUser(creditUserDTO.getUserDTO().getUsername());
		} catch (NullPointerException | FinderException e) {
			throw new CreateException("User must exist to create a CreditUser");
		}

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		CreditUser creditUser = commonModelMapper.map(creditUserDTO, CreditUser.class);

		// Creates the object
		creditUserRepository.save(creditUser);

		LOGGER.debug("exiting " + mname);
		return creditUserDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public CreditUserDTO findCreditUser(Long creditUserId) throws FinderException {
		final String mname = "findCreditUser";
		LOGGER.debug("entering " + mname + " for id " + creditUserId);

		checkId(creditUserId);

		CreditUser creditUser = null;
		if (!creditUserRepository.findById(creditUserId).isPresent())
			throw new FinderException("Credit must exist to be found");
		else
			creditUser = creditUserRepository.findById(creditUserId).get();

		LOGGER.debug("exiting " + mname);
		return commonModelMapper.map(creditUser, CreditUserDTO.class);
	}

	@Override
	@Transactional
	public void deleteCreditUser(Long creditUserId) throws FinderException, RemoveException {
		final String mname = "deleteCreditUser";
		LOGGER.debug("entering " + mname + " for id " + creditUserId);

		checkId(creditUserId);

		CreditUser creditUser = null;
		if (!creditUserRepository.findById(creditUserId).isPresent())
			throw new RemoveException("Credit registry must exist to be deleted");
		else
			creditUser = creditUserRepository.findById(creditUserId).get();
		// Deletes the object
		creditUserRepository.delete(creditUser);
		LOGGER.debug("exiting " + mname);

	}

	@Override
	@Transactional
	public void updateCreditUser(@Valid CreditUserDTO updatedCreditUserDTO) throws UpdateException {
		final String mname = "updateCreditUser";
		LOGGER.debug("entering " + mname);

		if (updatedCreditUserDTO == null)
			throw new UpdateException("Credit registry object is null");

		// Checks if the object exists
		if (!creditUserRepository.findById(updatedCreditUserDTO.getId()).isPresent())
			throw new UpdateException("Credit registry must exist to be updated");

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		CreditUser updatedCreditUser = commonModelMapper.map(updatedCreditUserDTO, CreditUser.class);
		// Updates the object
		creditUserRepository.save(updatedCreditUser);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CreditUserDTO> findCreditUsers() throws FinderException {
		final String mname = "findCreditUsers";
		LOGGER.debug("entering " + mname);

		// Finds all the objects
		final Iterable<CreditUser> creditUsers = creditUserRepository.findAll();
		int size;
		if ((size = ((Collection<CreditUser>) creditUsers).size()) == 0) {
			throw new FinderException("No credit in the database");
		}
		List<CreditUserDTO> creditUserDTOs = ((List<CreditUser>) creditUsers).stream()
				.map(creditUser -> commonModelMapper.map(creditUser, CreditUserDTO.class))
				.collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return creditUserDTOs;
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Date, Integer> findDateAndCreditByUser(String userId) throws FinderException {
		final String mname = "findDateAndCreditByUser";
		LOGGER.debug("entering " + mname);

		checkStringId(userId);

		User user = null;
		if (!userRepository.findById(userId).isPresent())
			throw new FinderException("User must exist to be found");
		else
			user = userRepository.findById(userId).get();

		// Finds all the objects
		final Iterable<CreditUser> creditUsersByUser = creditUserRepository.findAllByUser(user);

		Map<Date, Integer> creditUserList = new HashMap<Date, Integer>();

		for (CreditUser creditReg : creditUsersByUser) {

			creditUserList.put(creditReg.getMouvementDate(), creditReg.getCredit());
		}

		LOGGER.debug("exiting " + mname);
		return creditUserList;
	}

	// ======================================
	// = Private Methods =
	// ======================================

	private void checkId(final Long id) throws FinderException {
		if (id == null || id.equals(""))
			throw new FinderException("Id should not be null or empty");
	}
	private void checkStringId(final String id) throws FinderException {
		if (id == null || id.equals(""))
			throw new FinderException(id + " should not be null or empty");
	}

}
