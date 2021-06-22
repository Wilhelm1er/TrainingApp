package com.sport.training.domain.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.sport.training.authentication.domain.dao.UserRepository;
import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.model.User;
import com.sport.training.authentication.domain.service.UserService;
import com.sport.training.authentication.domain.service.UserServiceImpl;
import com.sport.training.domain.dao.CreditRegistryRepository;
import com.sport.training.domain.dao.DisciplineRegistryRepository;
import com.sport.training.domain.dao.DisciplineRepository;
import com.sport.training.domain.dao.EventRegistryRepository;
import com.sport.training.domain.dao.EventRepository;
import com.sport.training.domain.dto.CreditRegistryDTO;
import com.sport.training.domain.dto.DisciplineDTO;
import com.sport.training.domain.dto.DisciplineRegistryDTO;
import com.sport.training.domain.dto.EventDTO;
import com.sport.training.domain.dto.EventRegistryDTO;
import com.sport.training.domain.model.CreditRegistry;
import com.sport.training.domain.model.Discipline;
import com.sport.training.domain.model.DisciplineRegistry;
import com.sport.training.domain.model.Event;
import com.sport.training.domain.model.EventRegistry;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.RemoveException;
import com.sport.training.exception.UpdateException;

/**
 * This class is a facade for all user services.
 */

@Service
@Validated
public class RegistryServiceImpl implements RegistryService {

	// ======================================
	// = Attributes =
	// ======================================

	@Autowired
	private ModelMapper commonModelMapper, userDTOModelMapper, eventRegistryModelMapper, eventModelMapper,
			disciplineRegistryModelMapper;

	@Autowired
	private DisciplineRegistryRepository disciplineRegistryRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private CreditRegistryRepository creditRegistryRepository;

	@Autowired
	private EventRegistryRepository eventRegistryRepository;

	@Autowired
	private DisciplineRepository disciplineRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	// =====================================
	// = Constructors =
	// =====================================
	public RegistryServiceImpl() {
	}

	// =====================================
	// = DisciplineRegistry Business methods =
	// =====================================
	@Override
	@Transactional
	public DisciplineRegistryDTO createDisciplineRegistry(@Valid DisciplineRegistryDTO disciplineRegistryDTO)
			throws CreateException {
		final String mname = "createDisciplineRegistry";
		LOGGER.debug("entering " + mname);

		if (disciplineRegistryDTO == null)
			throw new CreateException("DisciplineRegistry object is null");

		if (disciplineRegistryDTO.getDisciplineDTO() == null || disciplineRegistryDTO.getDisciplineDTO().getId() == null
				|| !disciplineRepository.findById(disciplineRegistryDTO.getDisciplineDTO().getId()).isPresent())
			throw new CreateException("Discipline must exist to create a discipline registry");

		try {
			userService.findUser(disciplineRegistryDTO.getCoachDTO().getUsername());
		} catch (NullPointerException | FinderException e) {
			throw new CreateException("User must exist to create a DisciplineRegistry");
		}

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		DisciplineRegistry disciplineRegistry = disciplineRegistryModelMapper.map(disciplineRegistryDTO,
				DisciplineRegistry.class);

		if (disciplineRegistryRepository.findByCoachAndDiscipline(disciplineRegistry.getCoach(),
				disciplineRegistry.getDiscipline()) != null) {
			throw new CreateException("DisciplineRegistry object already exist for this discipline: "
					+ disciplineRegistry.getDiscipline().getName());
		}
		// Creates the object
		disciplineRegistryRepository.save(disciplineRegistry);

		LOGGER.debug("exiting " + mname);
		return disciplineRegistryDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public DisciplineRegistryDTO findDisciplineRegistry(final Long disciplineRegistryId) throws FinderException {
		final String mname = "findDiscipline";
		LOGGER.debug("entering " + mname + " for id " + disciplineRegistryId);

		checkId(disciplineRegistryId);

		DisciplineRegistry disciplineRegistry = null;
		if (!disciplineRegistryRepository.findById(disciplineRegistryId).isPresent())
			throw new FinderException("Discipline must exist to be found");
		else
			disciplineRegistry = disciplineRegistryRepository.findById(disciplineRegistryId).get();

		LOGGER.debug("exiting " + mname);
		return commonModelMapper.map(disciplineRegistry, DisciplineRegistryDTO.class);
	}

	@Override
	@Transactional
	public void deleteDisciplineRegistry(final Long disciplineRegistryId) throws FinderException, RemoveException {
		final String mname = "deleteDisciplineRegistry";
		LOGGER.debug("entering " + mname + " for id " + disciplineRegistryId);

		checkId(disciplineRegistryId);

		DisciplineRegistry disciplineRegistry = null;
		if (!disciplineRegistryRepository.findById(disciplineRegistryId).isPresent())
			throw new RemoveException("Discipline registry must exist to be deleted");
		else
			disciplineRegistry = disciplineRegistryRepository.findById(disciplineRegistryId).get();
		// Deletes the object
		disciplineRegistryRepository.delete(disciplineRegistry);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional
	public void updateDisciplineRegistry(@Valid final DisciplineRegistryDTO updatedDisciplineRegistryDTO)
			throws UpdateException {
		final String mname = "updateDisciplineRegistry";
		LOGGER.debug("entering " + mname);

		if (updatedDisciplineRegistryDTO == null)
			throw new UpdateException("Discipline registry object is null");

		// Checks if the object exists
		if (!disciplineRegistryRepository.findById(updatedDisciplineRegistryDTO.getId()).isPresent())
			throw new UpdateException("Discipline registry must exist to be updated");

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		DisciplineRegistry updatedDisciplineRegistry = commonModelMapper.map(updatedDisciplineRegistryDTO,
				DisciplineRegistry.class);
		// Updates the object
		disciplineRegistryRepository.save(updatedDisciplineRegistry);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DisciplineRegistryDTO> findDisciplineRegistries() throws FinderException {
		final String mname = "findDisciplineRegistries";
		LOGGER.debug("entering " + mname);

		// Finds all the objects
		final Iterable<DisciplineRegistry> disciplineRegistries = disciplineRegistryRepository.findAll();
		int size;
		if ((size = ((Collection<DisciplineRegistry>) disciplineRegistries).size()) == 0) {
			throw new FinderException("No discipline in the database");
		}
		List<DisciplineRegistryDTO> disciplineRegistryDTOs = ((List<DisciplineRegistry>) disciplineRegistries).stream()
				.map(disciplineRegistry -> commonModelMapper.map(disciplineRegistry, DisciplineRegistryDTO.class))
				.collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return disciplineRegistryDTOs;
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
		final Iterable<DisciplineRegistry> disciplineRegistriesByDiscipline = disciplineRegistryRepository
				.findAllByDiscipline(discipline);

		int size;
		if ((size = ((Collection<DisciplineRegistry>) disciplineRegistriesByDiscipline).size()) == 0) {
			throw new FinderException("No Coach in the database");
		}

		Set<User> coachList = new HashSet<User>();

		for (DisciplineRegistry discReg : disciplineRegistriesByDiscipline) {
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
		final Iterable<Discipline> disciplinesByCoach = disciplineRegistryRepository.findDisciplinesByCoach(coach);

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
		final Iterable<DisciplineRegistry> disciplineRegistriesByDiscipline = disciplineRegistryRepository
				.findAllByCoach(coach);

		int size;
		if ((size = ((Collection<DisciplineRegistry>) disciplineRegistriesByDiscipline).size()) == 0) {
			throw new FinderException("No Discipline in the database for this coach");
		}

		List<Discipline> disciplineList = new ArrayList<Discipline>();

		for (DisciplineRegistry discReg : disciplineRegistriesByDiscipline) {
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
		final Iterable<DisciplineRegistry> disciplineRegistriesByDiscipline = disciplineRegistryRepository
				.findAllByCoach(coach);

		int size;
		if ((size = ((Collection<DisciplineRegistry>) disciplineRegistriesByDiscipline).size()) == 0) {
			throw new FinderException("No Discipline in the database for this coach");
		}

		List<Discipline> disciplineList = new ArrayList<Discipline>();

		for (DisciplineRegistry discReg : disciplineRegistriesByDiscipline) {
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
	// = EventRegistry Business methods =
	// =====================================
	@Override
	@Transactional
	public EventRegistryDTO createEventRegistry(@Valid EventRegistryDTO eventRegistryDTO) throws CreateException {
		final String mname = "createEventRegistry";
		LOGGER.debug("entering " + mname);

		if (eventRegistryDTO == null)
			throw new CreateException("EventRegistryDTO object is null");

		if (eventRegistryDTO.getEventDTO() == null || eventRegistryDTO.getEventDTO().getId() == null
				|| !eventRepository.findById(eventRegistryDTO.getEventDTO().getId()).isPresent())
			throw new CreateException("Event must exist to create an event registry");

		try {
			userService.findUser(eventRegistryDTO.getUserDTO().getUsername());
		} catch (NullPointerException | FinderException e) {
			throw new CreateException("Athlete must exist to create an EventRegistry");
		}

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		EventRegistry eventRegistry = eventRegistryModelMapper.map(eventRegistryDTO, EventRegistry.class);

		if (eventRegistryRepository.findByUserAndEvent(eventRegistry.getUser(), eventRegistry.getEvent()) != null)
			throw new CreateException("EventRegistry object already exist");

		// Creates the object
		eventRegistryRepository.save(eventRegistry);

		LOGGER.debug("exiting " + mname);
		return eventRegistryDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public EventRegistryDTO findEventRegistry(final Long eventRegistryId) throws FinderException {
		final String mname = "findDiscipline";
		LOGGER.debug("entering " + mname + " for id " + eventRegistryId);

		checkId(eventRegistryId);

		EventRegistry eventRegistry = null;
		if (!eventRegistryRepository.findById(eventRegistryId).isPresent())
			throw new FinderException("Event must exist to be found");
		else
			eventRegistry = eventRegistryRepository.findById(eventRegistryId).get();

		LOGGER.debug("exiting " + mname);
		return commonModelMapper.map(eventRegistry, EventRegistryDTO.class);
	}

	@Override
	@Transactional
	public void deleteEventRegistry(final Long eventRegistryId) throws FinderException, RemoveException {
		final String mname = "deleteEventRegistry";
		LOGGER.debug("entering " + mname + " for id " + eventRegistryId);

		checkId(eventRegistryId);

		EventRegistry eventRegistry = null;
		if (!eventRegistryRepository.findById(eventRegistryId).isPresent())
			throw new RemoveException("Event registry must exist to be deleted");
		else
			eventRegistry = eventRegistryRepository.findById(eventRegistryId).get();
		// Deletes the object
		eventRegistryRepository.delete(eventRegistry);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional
	public void updateEventRegistry(@Valid final EventRegistryDTO updatedEventRegistryDTO) throws UpdateException {
		final String mname = "updateEventRegistry";
		LOGGER.debug("entering " + mname);

		if (updatedEventRegistryDTO == null)
			throw new UpdateException("Event registry object is null");

		// Checks if the object exists
		if (!eventRegistryRepository.findById(updatedEventRegistryDTO.getId()).isPresent())
			throw new UpdateException("Event registry must exist to be updated");

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		EventRegistry updatedEventRegistry = eventRegistryModelMapper.map(updatedEventRegistryDTO, EventRegistry.class);
		// Updates the object
		eventRegistryRepository.save(updatedEventRegistry);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EventRegistryDTO> findEventRegistries() throws FinderException {
		final String mname = "findEventRegistries";
		LOGGER.debug("entering " + mname);

		// Finds all the objects
		final Iterable<EventRegistry> eventRegistries = eventRegistryRepository.findAll();
		int size;
		if ((size = ((Collection<EventRegistry>) eventRegistries).size()) == 0) {
			throw new FinderException("No event in the database");
		}
		List<EventRegistryDTO> eventRegistryDTOs = ((List<EventRegistry>) eventRegistries).stream()
				.map(eventRegistry -> eventRegistryModelMapper.map(eventRegistry, EventRegistryDTO.class))
				.collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return eventRegistryDTOs;
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
		final Iterable<EventRegistry> eventRegistriesByAthlete = eventRegistryRepository.findAllByUser(athlete);

		int size;
		if ((size = ((Collection<EventRegistry>) eventRegistriesByAthlete).size()) == 0) {
			throw new FinderException("No Event in the database");
		}

		List<Event> eventList = new ArrayList<Event>();

		for (EventRegistry eventReg : eventRegistriesByAthlete) {
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
	public EventRegistryDTO findEventRegistryByAthleteAndEvent(String coachId, Long eventId) throws FinderException {
		final String mname = "findEventRegistryByAthleteAndEvent";
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
		final EventRegistry eventRegistriyByUserAndEvent = eventRegistryRepository.findByUserAndEvent(coach, event);

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		EventRegistryDTO EventRegistryDTO = eventRegistryModelMapper.map(eventRegistriyByUserAndEvent,
				EventRegistryDTO.class);

		LOGGER.debug("exiting " + mname);
		return EventRegistryDTO;
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
		final Iterable<EventRegistry> eventRegistriesByEvent = eventRegistryRepository.findAllByEvent(event);

		int size;
		if ((size = ((Collection<EventRegistry>) eventRegistriesByEvent).size()) == 0) {
			throw new FinderException("No Event in the database");
		}

		List<User> userList = new ArrayList<User>();

		for (EventRegistry eventReg : eventRegistriesByEvent) {
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
	public CreditRegistryDTO createCreditRegistry(@Valid CreditRegistryDTO creditRegistryDTO) throws CreateException {
		final String mname = "createCreditRegistry";
		LOGGER.debug("entering " + mname);

		if (creditRegistryDTO == null)
			throw new CreateException("CreditRegistryDTO object is null");

		try {
			userService.findUser(creditRegistryDTO.getUserDTO().getUsername());
		} catch (NullPointerException | FinderException e) {
			throw new CreateException("User must exist to create a CreditRegistry");
		}

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		CreditRegistry creditRegistry = commonModelMapper.map(creditRegistryDTO, CreditRegistry.class);

		// Creates the object
		creditRegistryRepository.save(creditRegistry);

		LOGGER.debug("exiting " + mname);
		return creditRegistryDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public CreditRegistryDTO findCreditRegistry(Long creditRegistryId) throws FinderException {
		final String mname = "findCreditRegistry";
		LOGGER.debug("entering " + mname + " for id " + creditRegistryId);

		checkId(creditRegistryId);

		CreditRegistry creditRegistry = null;
		if (!creditRegistryRepository.findById(creditRegistryId).isPresent())
			throw new FinderException("Credit must exist to be found");
		else
			creditRegistry = creditRegistryRepository.findById(creditRegistryId).get();

		LOGGER.debug("exiting " + mname);
		return commonModelMapper.map(creditRegistry, CreditRegistryDTO.class);
	}

	@Override
	@Transactional
	public void deleteCreditRegistry(Long creditRegistryId) throws FinderException, RemoveException {
		final String mname = "deleteCreditRegistry";
		LOGGER.debug("entering " + mname + " for id " + creditRegistryId);

		checkId(creditRegistryId);

		CreditRegistry creditRegistry = null;
		if (!creditRegistryRepository.findById(creditRegistryId).isPresent())
			throw new RemoveException("Credit registry must exist to be deleted");
		else
			creditRegistry = creditRegistryRepository.findById(creditRegistryId).get();
		// Deletes the object
		creditRegistryRepository.delete(creditRegistry);
		LOGGER.debug("exiting " + mname);

	}

	@Override
	@Transactional
	public void updateCreditRegistry(@Valid CreditRegistryDTO updatedCreditRegistryDTO) throws UpdateException {
		final String mname = "updateCreditRegistry";
		LOGGER.debug("entering " + mname);

		if (updatedCreditRegistryDTO == null)
			throw new UpdateException("Credit registry object is null");

		// Checks if the object exists
		if (!creditRegistryRepository.findById(updatedCreditRegistryDTO.getId()).isPresent())
			throw new UpdateException("Credit registry must exist to be updated");

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		CreditRegistry updatedCreditRegistry = commonModelMapper.map(updatedCreditRegistryDTO, CreditRegistry.class);
		// Updates the object
		creditRegistryRepository.save(updatedCreditRegistry);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CreditRegistryDTO> findCreditRegistries() throws FinderException {
		final String mname = "findCreditRegistries";
		LOGGER.debug("entering " + mname);

		// Finds all the objects
		final Iterable<CreditRegistry> creditRegistries = creditRegistryRepository.findAll();
		int size;
		if ((size = ((Collection<CreditRegistry>) creditRegistries).size()) == 0) {
			throw new FinderException("No credit in the database");
		}
		List<CreditRegistryDTO> creditRegistryDTOs = ((List<CreditRegistry>) creditRegistries).stream()
				.map(creditRegistry -> commonModelMapper.map(creditRegistry, CreditRegistryDTO.class))
				.collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return creditRegistryDTOs;
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
		final Iterable<CreditRegistry> creditRegistriesByUser = creditRegistryRepository.findAllByUser(user);

		Map<Date, Integer> creditRegistryList = new HashMap<Date, Integer>();

		for (CreditRegistry creditReg : creditRegistriesByUser) {

			creditRegistryList.put(creditReg.getMouvementDate(), creditReg.getCredit());
		}

		LOGGER.debug("exiting " + mname);
		return creditRegistryList;
	}
	// ======================================
	// = Private Methods =
	// ======================================

	private void checkId(final long l) throws FinderException {
		if (l == 0)
			throw new FinderException("Id should not be 0");
	}

	private void checkStringId(final String id) throws FinderException {
		if (id == null || id.equals(""))
			throw new FinderException(id + " should not be null or empty");
	}

}
