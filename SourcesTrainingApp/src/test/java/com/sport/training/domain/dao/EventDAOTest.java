package com.sport.training.domain.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sport.training.authentication.domain.dao.UserRepository;
import com.sport.training.authentication.domain.model.User;
import com.sport.training.authentication.domain.service.RoleService;
import com.sport.training.domain.model.Activity;
import com.sport.training.domain.model.Discipline;
import com.sport.training.domain.model.Event;
import com.sport.training.domain.service.CounterService;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.ObjectNotFoundException;
import com.sport.training.exception.RemoveException;
import com.sport.training.exception.UpdateException;

/**
 * This class tests the ItemDAO class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
//@Transactional
public class EventDAOTest {

	private static Logger logger = LogManager.getLogger(EventDAOTest.class);

	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private DisciplineRepository disciplineRepository;
	@Autowired
	private ActivityRepository activityRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CounterService counterService;
	@Autowired
	private RoleService roleService;

	private Random random = new Random();

	private final Double _defaultCreditCost = 1.0;
	private final LocalDateTime _defaultDate = LocalDateTime.of(2021, 7, 12, 20, 15, 50, 345678900);

	// ==================================
	// = Test cases =
	// ==================================
	/**
	 * This test tries to find an object with a invalid identifier.
	 */
	@Test
	public void testDomainFindEventWithInvalidValues() throws Exception {

		// Finds an object with a unknown identifier
		final Long id = eventRepository.findLastId().orElse(10000L) + 1;

		try {
			findEvent(id);
			fail("Object with unknonw id should not be found");
		} catch (NoSuchElementException e) {
		}

		// Finds an object with an empty identifier
		try {
			eventRepository.findById(random.nextLong() + 1L).get();
			fail("Object with empty id should not be found");
		} catch (Exception e) {
		}

		// Finds an object with a null identifier
		try {
			eventRepository.findById(null).get();
			fail("Object with null id should not be found");
		} catch (Exception e) {
		}
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testDomainFindAllEvents() throws Exception {

		// First findAll
		final int firstSize = findAllEvents();
		// Create an object
		final long eventId = createEvent();

		// Ensures that the object exists
		try {
			findEvent(eventId);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// second findAll
		final int secondSize = findAllEvents();

		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Cleans the test environment
		removeEvent(eventId);

		try {
			findEvent(eventId);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testDomainFindAllEventsForACoach() throws Exception {
		User newCoach = createNewCoach();
		final String coachId = newCoach.getUsername();

		// First findAll
		final int firstSize = findAllEventsByCoach(coachId);

		// Checks that the collection is empty
		if (firstSize != 0)
			fail("The collection should be empty");

		// Create an object
		Event event1 = createEventForCoach(newCoach);

		// Ensures that the object exists
		try {
			findEvent(event1.getId());
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// second findAll
		final int secondSize = findAllEventsByCoach(coachId);

		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Create an new object with a different id
		Event event2 = createEventForCoach(newCoach);

		// Ensures that the new object exists
		try {
			findEvent(event2.getId());
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// third findAll
		final int thirdSize = findAllEventsByCoach(coachId);

		// Checks that the collection size has increase of one
		if (thirdSize != secondSize + 1)
			fail("The collection should have the same size");

		// Cleans the test environment
		eventRepository.delete(event1);
		eventRepository.delete(event2);
		removeCoach(newCoach);
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testDomainFindAllEventsForAnActivity() throws Exception {
		Activity newActivity = createNewActivity();
		final String activityId = newActivity.getId();

		// First findAll
		final int firstSize = findAllEventsByActivity(activityId);

		// Checks that the collection is empty
		if (firstSize != 0)
			fail("The collection should be empty");

		// Create an object
		Event event1 = createEventForActivity(newActivity);

		// Ensures that the object exists
		try {
			findEvent(event1.getId());
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// second findAll
		final int secondSize = findAllEventsByActivity(activityId);

		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Create an new object with a different id
		Event event2 = createEventForActivity(newActivity);

		// Ensures that the new object exists
		try {
			findEvent(event2.getId());
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// third findAll
		final int thirdSize = findAllEventsByActivity(activityId);

		// Checks that the collection size has increase of one
		if (thirdSize != secondSize + 1)
			fail("The collection should have the same size");

		// Cleans the test environment
		eventRepository.delete(event1);
		eventRepository.delete(event2);
		removeActivity(newActivity);
	}

	/**
	 * This method ensures that creating an object works. It first finds the object,
	 * makes sure it doesn't exist, creates it and checks it then exists.
	 */
	@Test
	public void testDomainCreateEvent() throws Exception {
		Event event = null;

		// Creates an object
		final long eventId = createEvent();

		// Ensures that the object exists
		try {
			event = findEvent(eventId);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkEvent(event);

		// Cleans the test environment
		removeEvent(eventId);

		try {
			findEvent(eventId);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test make sure that updating an object success
	 */
	@Test
	public void testDomainUpdateEvent() throws Exception {
		// Creates an object
		final long eventId = createEvent();

		// Ensures that the object exists
		Event event = null;
		try {
			event = findEvent(eventId);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkEvent(event);

		// Updates the object with new values
		updateEvent(event);

		// Ensures that the object still exists
		Event eventUpdated = null;
		try {
			eventUpdated = findEvent(eventId);
		} catch (NoSuchElementException e) {
			fail("Object should be found");
		}

		// Checks that the object values have been updated
		checkEvent(eventUpdated);

		// Cleans the test environment
		removeEvent(eventId);

		try {
			findEvent(eventId);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test ensures that the system cannont remove an unknown object
	 */
	@Test
	public void testDomainDeleteUnknownEvent() throws Exception {
		// Removes an unknown object
		try {
			eventRepository.delete(null);
			fail("Deleting an unknown object should break");
		} catch (Exception e) {
		}
	}

	// ==================================
	// = Private Methods =
	// ==================================
	private Event findEvent(final Long id) throws NoSuchElementException {
		final Event event = eventRepository.findById(id).get();
		return event;
	}

	private int findAllEvents() throws FinderException {
		try {
			return ((Collection<Event>) eventRepository.findAll()).size();
		} catch (Exception e) {
			logger.info("Exception is ... " + e.getMessage());
			return 0;
		}
	}

	private int findAllEventsByActivity(String activityId) throws FinderException {
		try {
			Activity activity = activityRepository.findById(activityId).get();
			return ((Collection<Event>) eventRepository.findAllByActivity(activity)).size();
		} catch (Exception e) {
			return 0;
		}
	}

	private int findAllEventsByCoach(String coachId) throws FinderException {
		try {
			User user = userRepository.findById(coachId).get();
			return ((Collection<Event>) eventRepository.findAllByCoach(user)).size();
		} catch (Exception e) {
			return 0;
		}
	}

	// Creates a coach first, then a activity and then an event linked to this
	// activity and coach
	private long createEvent() throws CreateException, RemoveException, FinderException {
		// Create user
		String newCoachId = counterService.getUniqueId("User");
		final User coach = new User(newCoachId, "firstname" + newCoachId, "lastname" + newCoachId);
		coach.setPassword("pwd" + newCoachId);
		coach.setRole(roleService.findByRoleName("ROLE_COACH"));
		coach.setStatut("VALIDE");
		userRepository.save(coach);

		// Create Activity
		final Activity activity = createNewActivity();

		// Create Event
		final Event event = new Event("name" + newCoachId, _defaultDate, _defaultCreditCost, coach, activity);

		event.setDuration(50);
		event.setIntensity(3);
		event.setEquipment("equipment" + newCoachId);
		event.setDescription("description" + newCoachId);

		eventRepository.save(event);
		return event.getId();
	}

	// Creates a discipline, an activity and a coach, updates the event with this
	// new
	// activity and coach
	private void updateEvent(final Event event)
			throws UpdateException, CreateException, RemoveException, FinderException {
		// get old activity
		User coa = event.getCoach();

		// Create user
		final String newCoachId = counterService.getUniqueId("User");
		final User coach = new User(newCoachId, "firstname" + newCoachId, "lastname" + newCoachId);
		coach.setPassword("pwd" + newCoachId);
		coach.setStatut("VALIDE");
		userRepository.save(coach);

		// get old activity
		Activity activ = event.getActivity();

		// Create Activity
		Activity activity = createNewActivity();

		// Updates the event
		event.setName("name" + newCoachId);
		event.setActivity(activity);
		event.setCoach(coach);
		eventRepository.save(event);
		// delete old activity et discipline
		activityRepository.delete(activ);
		// delete old user
		userRepository.delete(coa);
	}

	private void checkEvent(final Event event) {
		assertEquals("name", "name" + event.getCoach().getUsername(), event.getName());
		assertNotNull("activity", event.getActivity());
		assertNotNull("coach", event.getCoach());
	}

	private void removeEvent(final long eventId) throws RemoveException, ObjectNotFoundException {
		Event event = eventRepository.findById(eventId).get();
		final Activity activity = event.getActivity();
		final User user = event.getCoach();
		eventRepository.deleteById(eventId);
		userRepository.delete(user);
		activityRepository.delete(activity);
	}

	// Creates a discipline first, then an activity and return it
	private Activity createNewActivity() throws CreateException, ObjectNotFoundException {
		// Create discipline
		final String newdisciplineId = counterService.getUniqueId("discipline");
		final Discipline discipline = new Discipline(newdisciplineId, "name" + newdisciplineId,
				"description" + newdisciplineId);
		discipline.setDocuments("documents" + newdisciplineId);
		disciplineRepository.save(discipline);
		// Create activity
		final String newactivityId = counterService.getUniqueId("activity");
		final Activity activity = new Activity(newactivityId, "name" + newactivityId, "description" + newactivityId,
				discipline);
		activity.setCreditcostMax(4.0);
		activity.setCreditcostMin(1.0);

		try {
			activityRepository.save(activity);
		} catch (Exception e) {
			// remove the added discipline object
			disciplineRepository.deleteById(newdisciplineId);
			// rethrow the exception
			throw e;
		}
		return activity;
	}

	// Creates a coach
	private User createNewCoach() throws CreateException, FinderException {
		// Create Coach
		final String newCoachId = counterService.getUniqueId("Coach");
		final User coach = new User("user" + newCoachId, "firstname" + newCoachId, "lastname" + newCoachId);
		coach.setStatut("VALIDE");
		coach.setPassword("pwd" + newCoachId);
		coach.setRole(roleService.findByRoleName("ROLE_COACH"));
		userRepository.save(coach);
		return coach;
	}

	// Creates an event linked to an existing activity
	private Event createEventForCoach(final User coach) throws CreateException, ObjectNotFoundException {

		final String newUserId = coach.getUsername();
		// Create Activity
		final Activity activity = createNewActivity();

		// Create Event
		final Event event = new Event("name" + newUserId, _defaultDate, _defaultCreditCost, coach, activity);

		event.setDuration(50);
		event.setIntensity(3);
		event.setEquipment("equipment" + newUserId);
		event.setDescription("description" + newUserId);

		eventRepository.save(event);
		return event;
	}

	// Creates an event linked to an existing activity
	private Event createEventForActivity(final Activity activity) throws CreateException, FinderException {
		// Create user
		String newCoachId = counterService.getUniqueId("User");
		final User coach = new User(newCoachId, "firstname" + newCoachId, "lastname" + newCoachId);
		coach.setPassword("pwd" + newCoachId);
		coach.setRole(roleService.findByRoleName("ROLE_COACH"));
		coach.setStatut("VALIDE");
		userRepository.save(coach);

		// Create Event
		final Event event = new Event("name" + newCoachId, _defaultDate, _defaultCreditCost, coach, activity);

		event.setDuration(50);
		event.setIntensity(3);
		event.setEquipment("equipment" + newCoachId);
		event.setDescription("description" + newCoachId);

		eventRepository.save(event);
		return event;
	}

	private void removeActivity(final Activity activity) throws RemoveException, ObjectNotFoundException {
		final String categoryId = activity.getDiscipline().getId();
		Discipline discipline = disciplineRepository.findById(categoryId).get();
		activityRepository.delete(activity);
		disciplineRepository.delete(discipline);
	}

	private void removeCoach(final User coach) throws RemoveException, ObjectNotFoundException {
		userRepository.delete(coach);
	}
}
