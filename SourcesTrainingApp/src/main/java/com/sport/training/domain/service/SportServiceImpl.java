package com.sport.training.domain.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.sport.training.domain.dao.DisciplineRepository;
import com.sport.training.domain.dao.EventRepository;
import com.sport.training.authentication.domain.dao.UserRepository;
import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.dao.ActivityRepository;
import com.sport.training.domain.dto.DisciplineDTO;
import com.sport.training.domain.dto.EventDTO;
import com.sport.training.domain.dto.ActivityDTO;
import com.sport.training.domain.model.Discipline;
import com.sport.training.domain.model.Event;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.DuplicateKeyException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.RemoveException;
import com.sport.training.exception.UpdateException;
import com.sport.training.domain.model.Activity;

/**
 * This class is a facade for all catalog services.
 */
@Service
@Validated
public class SportServiceImpl implements SportService {

	// ======================================
	// = Attributes =
	// ======================================
	private static final Logger LOGGER = LoggerFactory.getLogger(SportServiceImpl.class);

	@Autowired
	private DisciplineRepository disciplineRepository;

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper commonModelMapper, activityModelMapper, eventModelMapper;

	// =====================================
	// = Constructors =
	// =====================================
	public SportServiceImpl() {
	}

	// =====================================
	// =    Discipline Business methods    =
	// =====================================
	@Override
	@Transactional
	public DisciplineDTO createDiscipline(@Valid final DisciplineDTO disciplineDTO) throws CreateException {
		final String mname = "createDiscipline";
		LOGGER.debug("entering " + mname);

		if (disciplineDTO == null || disciplineDTO.getId() == null || disciplineDTO.getId().equals(""))
			throw new CreateException("Discipline object is null");

		try {
			if (findDiscipline(disciplineDTO.getId()) != null)
				throw new DuplicateKeyException();
		} catch (FinderException e) {
		}

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		Discipline discipline = commonModelMapper.map(disciplineDTO, Discipline.class);

		// Creates the object
		disciplineRepository.save(discipline);

		LOGGER.debug("exiting " + mname);
		return disciplineDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public DisciplineDTO findDiscipline(final String disciplineId) throws FinderException {
		final String mname = "findDiscipline";
		LOGGER.debug("entering " + mname + " for id " + disciplineId);

		checkStringId(disciplineId);

		Discipline discipline = null;
		if (!disciplineRepository.findById(disciplineId).isPresent())
			throw new FinderException("Discipline must exist to be found");
		else
			discipline = disciplineRepository.findById(disciplineId).get();

		LOGGER.debug("exiting " + mname);
		return commonModelMapper.map(discipline, DisciplineDTO.class);
	}
	
	@Override
	public Discipline findByDisciplineName(String disciplineName) throws FinderException {
		Discipline discipline;
		if( (discipline=disciplineRepository.findByName(disciplineName))==null)
			throw new FinderException("discipline unknown");
		return discipline;
	}

	@Override
	@Transactional
	public void deleteDiscipline(final String disciplineId) throws FinderException, RemoveException {
		final String mname = "deleteDiscipline";
		LOGGER.debug("entering " + mname + " for id " + disciplineId);

		checkStringId(disciplineId);

		Discipline discipline = null;
		if (!disciplineRepository.findById(disciplineId).isPresent())
			throw new RemoveException("Discipline must exist to be deleted");
		else
			discipline = disciplineRepository.findById(disciplineId).get();
		// Deletes the object
		disciplineRepository.delete(discipline);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional
	public void updateDiscipline(@Valid final DisciplineDTO updatedDisciplineDTO) throws UpdateException {
		final String mname = "updateDiscipline";
		LOGGER.debug("entering " + mname);

		if (updatedDisciplineDTO == null)
			throw new UpdateException("Discipline object is null");

		// Checks if the object exists
		if (!disciplineRepository.findById(updatedDisciplineDTO.getId()).isPresent())
			throw new UpdateException("Discipline must exist to be updated");

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		Discipline updatedDiscipline = commonModelMapper.map(updatedDisciplineDTO, Discipline.class);
		// Updates the object
		disciplineRepository.save(updatedDiscipline);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DisciplineDTO> findDisciplines() throws FinderException {
		final String mname = "findDisciplines";
		LOGGER.debug("entering " + mname);

		// Finds all the objects
		final Iterable<Discipline> disciplines = disciplineRepository.findAll();
		int size;
		if ((size = ((Collection<Discipline>) disciplines).size()) == 0) {
			throw new FinderException("No discipline in the database");
		}
		List<DisciplineDTO> disciplineDTOs = ((List<Discipline>) disciplines).stream()
				.map(discipline -> commonModelMapper.map(discipline, DisciplineDTO.class)).collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return disciplineDTOs;
	}

	// =====================================
	// =     Activity Business methods     =
	// =====================================
	@Override
	@Transactional
	public ActivityDTO createActivity(@Valid final ActivityDTO activityDTO) throws CreateException {
		final String mname = "createActivity";
		LOGGER.debug("entering " + mname);

		if (activityDTO == null || activityDTO.getId()==null || activityDTO.getId().equals("") || activityDTO.getDisciplineDTO()==null)
			throw new CreateException("Activity object is invalid");

		try {
			if (findActivity(activityDTO.getId()) != null)
				throw new DuplicateKeyException();
		} catch (FinderException e) {
		}
		try {
			findDiscipline(activityDTO.getDisciplineDTO().getId());
		} catch (FinderException e) {
			throw new CreateException("Discipline must exist to create an activity");
		}
		// Checks if the object exists
		if (!disciplineRepository.findById(activityDTO.getDisciplineDTO().getId()).isPresent())
			throw new CreateException("Discipline must exist to update an activity");

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		Activity activity = activityModelMapper.map(activityDTO, Activity.class);
		LOGGER.debug("activity " + activity);
		// Creates the object
		activityRepository.save(activity);

		LOGGER.debug("exiting " + mname);
		return activityDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public ActivityDTO findActivity(final String activityId) throws FinderException {
		final String mname = "findActivity";
		LOGGER.debug("entering : " + mname + " for id " + activityId);

		checkStringId(activityId);

		Activity activity = null;
		if (!activityRepository.findById(activityId).isPresent())
			throw new FinderException("activity must exist to be found");
		else
			activity = activityRepository.findById(activityId).get();

		LOGGER.debug("exiting " + mname);
		return activityModelMapper.map(activity, ActivityDTO.class);
	}

	@Override
	@Transactional
	public void deleteActivity(final String activityId) throws FinderException, RemoveException {
		final String mname = "deleteActivity";
		LOGGER.debug("entering : " + mname + " with id" + activityId);

		checkStringId(activityId);

		Activity activity = null;
		if (!activityRepository.findById(activityId).isPresent())
			throw new RemoveException("Activity must exist to be deleted");
		else
			activity = activityRepository.findById(activityId).get();
		activityRepository.delete(activity);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional
	public void updateActivity(@Valid final ActivityDTO updatedActivityDTO) throws UpdateException {
		final String mname = "updateActivity";
		LOGGER.debug("entering " + mname);

		if (updatedActivityDTO == null)
			throw new UpdateException("Activity object is null");

		// Checks if the object exists
		if (!activityRepository.findById(updatedActivityDTO.getId()).isPresent())
			throw new UpdateException("Activity must exist to be updated");

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		Activity updatedActivity = activityModelMapper.map(updatedActivityDTO, Activity.class);

		// Updates the object
		activityRepository.save(updatedActivity);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActivityDTO> findActivities() throws FinderException {
		final String mname = "findActivities";
		LOGGER.debug("entering " + mname);

		// Finds all the objects
		final Iterable<Activity> activities = activityRepository.findAll();
		int size;
		if ((size = ((Collection<Activity>) activities).size()) == 0) {
			throw new FinderException("No Activity in the database");
		}
		List<ActivityDTO> activityDTOs = ((List<Activity>) activities)
										.stream()
										.map(activity -> activityModelMapper.map(activity, ActivityDTO.class))
										.collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return activityDTOs;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActivityDTO> findActivities(String disciplineId) throws FinderException {
		final String mname = "findActivitiesByDiscipline";
		LOGGER.debug("entering " + mname);

		checkStringId(disciplineId);

		Discipline discipline = null;
		if (!disciplineRepository.findById(disciplineId).isPresent())
			throw new FinderException("Discipline must exist to be found");
		else
			discipline = disciplineRepository.findById(disciplineId).get();

		// Finds all the objects
		final Iterable<Activity> activitiesByDiscipline = activityRepository.findAllByDiscipline(discipline);
		
		int size;
		if ((size = ((Collection<Activity>) activitiesByDiscipline).size()) == 0) {
			throw new FinderException("No Activity in the database");
		}

		List<ActivityDTO> activityDTOs = ((List<Activity>) activitiesByDiscipline)
										.stream()
										.map(activity -> activityModelMapper.map(activity, ActivityDTO.class))
										.collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return activityDTOs;
	}

	// ======================================
	// =       Event Business methods       =
	// ======================================
	@Override
	@Transactional
	public EventDTO createEvent(@Valid final EventDTO eventDTO) throws CreateException {
		final String mname = "createEvent";
		LOGGER.debug("entering " + mname);

		if (eventDTO == null ||  eventDTO.getActivityDTO()==null)
			throw new CreateException("Event object is invalid");

		try {
			findActivity(eventDTO.getActivityDTO().getId());
		} catch (FinderException e) {
			throw new CreateException("Activity must exist to create an Event");
		}

		// Finds the linked object
		if (!activityRepository.findById(eventDTO.getActivityDTO().getId()).isPresent())
			throw new CreateException("Activity must exist to create an event");

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		Event event = eventModelMapper.map(eventDTO, Event.class);

		// Creates the object
		eventRepository.save(event);

		LOGGER.debug("exiting " + mname);
		return eventDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public EventDTO findEvent(final Long eventId) throws FinderException {
		final String mname = "findEvent";
		LOGGER.debug("entering : " + mname + " for id" + eventId);

		checkId(eventId);

		Event event = null;
		if (!eventRepository.findById(eventId).isPresent())
			throw new FinderException("Event must exist to be found");
		else
			event = eventRepository.findById(eventId).get();

		LOGGER.debug("exiting " + mname);
		return eventModelMapper.map(event, EventDTO.class);
	}

	@Override
	@Transactional
	public void deleteEvent(final Long eventId) throws FinderException, RemoveException {
		final String mname = "deleteEvent";
		LOGGER.debug("entering : " + mname + " with id" + eventId);

		checkId(eventId);

		Event event = null;
		if (!eventRepository.findById(eventId).isPresent())
			throw new RemoveException("Item must exist to be deleted");
		else
			event = eventRepository.findById(eventId).get();
		eventRepository.delete(event);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional
	public void updateEvent(@Valid final EventDTO updatedEventDTO) throws UpdateException {
		final String mname = "updateEvent";
		LOGGER.debug("entering " + mname);

		if (updatedEventDTO == null)
			throw new UpdateException("Event object is null");

		// Checks if the object exists
		if (!eventRepository.findById(updatedEventDTO.getId()).isPresent())
			throw new UpdateException("Event must exist to be updated");

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		Event updatedEvent = eventModelMapper.map(updatedEventDTO, Event.class);

		// Updates the object
		eventRepository.save(updatedEvent);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EventDTO> findEvents() throws FinderException {
		final String mname = "findEvents";
		LOGGER.debug("entering " + mname);

		// Finds all the objects
		final Iterable<Event> events = eventRepository.findAll();

		int size;
		if ((size = ((Collection<Event>) events).size()) == 0) {
			throw new FinderException("No Event in the database");
		}
		List<EventDTO> eventDTOs = ((List<Event>) events)
								.stream()
								.map(event -> eventModelMapper.map(event, EventDTO.class))
								.collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return eventDTOs;
	}

	@Override
	@Transactional(readOnly = true)
	public List<EventDTO> findEvents(String coachId) throws FinderException {
		final String mname = "findEventsByCoach";
		LOGGER.debug("entering " + mname);

		checkStringId(coachId);

		User coach = null;
		if (!userRepository.findById(coachId).isPresent())
			throw new FinderException("Coach must exist to be found");
		else
			coach = userRepository.findById(coachId).get();

		// Finds all the objects
		final Iterable<Event> eventsByCoach = eventRepository.findAllByCoach(coach);
		
		int size;
		if ((size = ((Collection<Event>) eventsByCoach).size()) == 0) {
			throw new FinderException("No Event in the database");
		}
		
		List<EventDTO> eventDTOs = ((List<Event>) eventsByCoach)
									.stream()
									.map(event -> eventModelMapper.map(event, EventDTO.class))
									.collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return eventDTOs;
	}
	

	@Override
	@Transactional(readOnly = true)
	public List<EventDTO> findEventsByActivity(String activityId) throws FinderException {
		final String mname = "findEventsByActivity";
		LOGGER.debug("entering " + mname);

		checkStringId(activityId);

		Activity activity = null;
		if (!activityRepository.findById(activityId).isPresent())
			throw new FinderException("Activity must exist to be found");
		else
			activity = activityRepository.findById(activityId).get();

		// Finds all the objects
		final Iterable<Event> eventsByActivity = eventRepository.findAllByActivity(activity);
		
		int size;
		if ((size = ((Collection<Event>) eventsByActivity).size()) == 0) {
			throw new FinderException("No Event in the database");
		}
		
		List<EventDTO> eventDTOs = ((List<Event>) eventsByActivity)
									.stream()
									.map(event -> eventModelMapper.map(event, EventDTO.class))
									.collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return eventDTOs;
	}
	
	@Override
    @Transactional(readOnly=true)
    public List<EventDTO> searchEvents(String keyword) throws FinderException {
    	final String mname = "searchItems";
    	LOGGER.debug("entering "+mname);
        
     // retrieves the objects from the database
        Iterable<Event> eventsSearchedFor = eventRepository.findByIdOrNameContaining(keyword);    

        int size;
		if ((size = ((Collection<Event>) eventsSearchedFor).size()) == 0) {
			throw new FinderException("No Item for this search");
		}
		// model to DTO
		List<EventDTO> eventDTOsSearchedFor = ((List<Event>) eventsSearchedFor)
									.stream()
									.map(event -> eventModelMapper.map(event, EventDTO.class))
									.collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of the search : " + size);
        return eventDTOsSearchedFor;
    }
    
	// ======================================
    // =          Private Methods           =
    // ======================================
	

	private void checkStringId(final String id) throws FinderException {
		if (id == null || id.equals(""))
			throw new FinderException(id + " should not be null or empty");
	}
	private void checkId(final long l) throws FinderException {
		if (l == 0)
			throw new FinderException("Id should not be 0");
	}

}
