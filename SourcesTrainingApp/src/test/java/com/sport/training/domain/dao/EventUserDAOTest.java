package com.sport.training.domain.dao;

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
import com.sport.training.domain.model.EventUser;
import com.sport.training.domain.service.CounterService;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.ObjectNotFoundException;
import com.sport.training.exception.RemoveException;
import com.sport.training.exception.UpdateException;

/**
 * This class tests the activityDAO class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
//@Transactional
public class EventUserDAOTest {

	private static Logger logger = LogManager.getLogger(EventUserDAOTest.class);

	@Autowired
	private EventUserRepository eventUserRepository;
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
	public void testDomainFindEventUserWithInvalidValues() throws Exception {

		// Finds an object with a unknown identifier
		final Long id = eventUserRepository.findLastId().orElse(10000L) + 1;

		try {
			findEventUser(id);
			fail("Object with unknonw id should not be found");
		} catch (NoSuchElementException e) {
		}

		// Finds an object with an empty identifier
		try {
			eventUserRepository.findById(random.nextLong() + 1L).get();
			fail("Object with empty id should not be found");
		} catch (Exception e) {
		}

		// Finds an object with a null identifier
		try {
			eventUserRepository.findById(null).get();
			fail("Object with null id should not be found");
		} catch (Exception e) {
		}
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testDomainFindAllEventRegistries() throws Exception {

		// First findAll
		final int firstSize = findAllEventRegistries();
		// Create an object
		final long eventRegId = createEventUser();

		// Ensures that the object exists
		try {
			findEventUser(eventRegId);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// second findAll
		final int secondSize = findAllEventRegistries();

		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Cleans the test environment
		removeEventUser(eventRegId);

		try {
			findEventUser(eventRegId);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testDomainFindAllEventRegistriesForAUser() throws Exception {
		User newUser = createNewUser();
		final String userId = newUser.getUsername();

		// First findAll
		final int firstSize = findAllEventRegistriesByUser(userId);

		// Checks that the collection is empty
		if (firstSize != 0)
			fail("The collection should be empty");

		// Create an object
		EventUser eventReg1 = createEventUserForUser(newUser);

		// Ensures that the object exists
		try {
			findEventUser(eventReg1.getId());
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// second findAll
		final int secondSize = findAllEventRegistriesByUser(userId);

		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Create an new object with a different id
		EventUser eventReg2 = createEventUserForUser(newUser);

		// Ensures that the new object exists
		try {
			findEventUser(eventReg2.getId());
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// third findAll
		final int thirdSize = findAllEventRegistriesByUser(userId);

		// Checks that the collection size has increase of one
		if (thirdSize != secondSize + 1)
			fail("The collection should have the same size");

		// Cleans the test environment
		eventUserRepository.delete(eventReg1);
		eventUserRepository.delete(eventReg2);
		removeUser(newUser);
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testDomainFindAllEventRegistriesForAnEvent() throws Exception {

		Event newEvent = createNewEvent();
		final Long eventId = newEvent.getId();

		// First findAll
		final int firstSize = findAllEventRegistriesByEvent(eventId);

		// Checks that the collection is empty
		if (firstSize != 0)
			fail("The collection should be empty");

		// Create an object
		EventUser eventReg1 = createEventUserForEvent(newEvent);

		// Ensures that the object exists
		try {
			findEventUser(eventReg1.getId());
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// second findAll
		final int secondSize = findAllEventRegistriesByEvent(eventId);

		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Create an new object with a different id
		EventUser eventReg2 = createEventUserForEvent(newEvent);

		// Ensures that the new object exists
		try {
			findEventUser(eventReg2.getId());
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// third findAll
		final int thirdSize = findAllEventRegistriesByEvent(eventId);

		// Checks that the collection size has increase of one
		if (thirdSize != secondSize + 1)
			fail("The collection should have the same size");

		// Cleans the test environment
		eventUserRepository.delete(eventReg1);
		eventUserRepository.delete(eventReg2);
		removeEvent(newEvent.getId());
	}

	/**
	 * This method ensures that creating an object works. It first finds the object,
	 * makes sure it doesn't exist, creates it and checks it then exists.
	 */
	@Test
	public void testDomainCreateEventUser() throws Exception {
		EventUser eventUser = null;

		// Creates an object
		final long eventUserId = createEventUser();

		// Ensures that the object exists
		try {
			eventUser = findEventUser(eventUserId);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkEventUser(eventUser);

		// Cleans the test environment
		removeEventUser(eventUserId);

		try {
			findEventUser(eventUserId);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test make sure that updating an object success
	 */
	@Test
	public void testDomainUpdateEventUser() throws Exception {
		// Creates an object
		final long eventUserId = createEventUser();

		// Ensures that the object exists
		EventUser eventUser = null;
		try {
			eventUser = findEventUser(eventUserId);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkEventUser(eventUser);

		// Updates the object with new values
		updateEventUser(eventUser);

		// Ensures that the object still exists
		EventUser eventUserUpdated = null;
		try {
			eventUserUpdated = findEventUser(eventUserId);
		} catch (NoSuchElementException e) {
			fail("Object should be found");
		}

		// Checks that the object values have been updated
		checkEventUser(eventUserUpdated);

		// Cleans the test environment
		removeEventUser(eventUserId);

		try {
			findEventUser(eventUserId);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test ensures that the system cannont remove an unknown object
	 */
	@Test
	public void testDomainDeleteUnknownEventUser() throws Exception {
		// Removes an unknown object
		try {
			eventUserRepository.delete(null);
			fail("Deleting an unknown object should break");
		} catch (Exception e) {
		}
	}

	// ==================================
	// = Private Methods =
	// ==================================
	private EventUser findEventUser(final Long id) throws NoSuchElementException {
		final EventUser eventUser = eventUserRepository.findById(id).get();
		return eventUser;
	}

	private int findAllEventRegistries() throws FinderException {
		try {
			return ((Collection<EventUser>) eventUserRepository.findAll()).size();
		} catch (Exception e) {
			logger.info("Exception is ... " + e.getMessage());
			return 0;
		}
	}

	private int findAllEventRegistriesByEvent(Long eventId) throws FinderException {
		try {
			Event event = eventRepository.findById(eventId).get();
			return ((Collection<EventUser>) eventUserRepository.findAllByEvent(event)).size();
		} catch (Exception e) {
			return 0;
		}
	}

	private int findAllEventRegistriesByUser(String userId) throws FinderException {
		try {
			User user = userRepository.findById(userId).get();
			return ((Collection<EventUser>) eventUserRepository.findAllByUser(user)).size();
		} catch (Exception e) {
			return 0;
		}
	}

	// Creates a discipline first, then a activity and then an event linked to this
	// activity
	private long createEventUser() throws CreateException, RemoveException, FinderException {
		// Create new user
		final String userId = counterService.getUniqueId("User");
		final User user = new User(userId, "firstname" + userId, "lastname" + userId);
		user.setPassword("pwd" + userId);
		user.setStatut("VALIDE");
		userRepository.save(user);

		// Create Activity
		Event event = createNewEvent();

		// Create Event
		final EventUser eventUser = new EventUser(user, event);

		eventUserRepository.save(eventUser);
		return eventUser.getId();
	}

	// Creates a discipline, an activity and updates the event with this new
	// activity
	private void updateEventUser(final EventUser eventUser)
			throws UpdateException, CreateException, RemoveException, FinderException {
		// get old user
		User usr = eventUser.getUser();

		// Create new user
		final String userId = counterService.getUniqueId("User");
		final User user = new User(userId, "firstname" + userId, "lastname" + userId);
		user.setPassword("pwd" + userId);
		user.setStatut("VALIDE");
		userRepository.save(user);

		// get old event
		Event evt = eventUser.getEvent();

		// Create Event
		Event event = createNewEvent();
		// Updates the event
		eventUser.setUser(user);
		eventUser.setEvent(event);
		eventUserRepository.save(eventUser);

		// delete old athlete
		userRepository.delete(usr);
		// delete old coach
		eventRepository.delete(evt);
	}

	private void checkEventUser(final EventUser eventUser) {
		assertNotNull("user", eventUser.getUser());
		assertNotNull("event", eventUser.getEvent());
	}

	private void removeEventUser(final long eventRegId) throws RemoveException, ObjectNotFoundException {
		EventUser eventUser = eventUserRepository.findById(eventRegId).get();
		final User user = eventUser.getUser();
		final Event event = eventUser.getEvent();
		eventUserRepository.deleteById(eventRegId);
		eventRepository.delete(event);
		userRepository.delete(user);

	}

	// Creates a discipline first, then an activity and return it
	private Event createNewEvent() throws CreateException, FinderException {
		// Finds an object with a unknown identifier
		final Long id = eventRepository.findLastId().orElse(10000L) + 1;

		// Create Coach
		User coach = createNewCoach();

		// Create Activity
		Activity activity = createNewActivity();

		// Create Event
		final Event event = new Event("name" + id, _defaultDate, _defaultCreditCost, coach, activity);

		event.setDuration(50);
		event.setIntensity(3);
		event.setEquipment("equipment" + String.valueOf(id));
		event.setDescription("description" + String.valueOf(id));

		eventRepository.save(event);
		return event;
	}

	// Creates a coach
	private User createNewCoach() throws CreateException, FinderException {
		// Create Coach
		final String newCoachId = counterService.getUniqueId("Coach");
		final User coach = new User("user" + newCoachId, "firstname" + newCoachId, "lastname" + newCoachId);
		coach.setCity("city" + newCoachId);
		coach.setCountry("cnty" + newCoachId);
		coach.setState("state" + newCoachId);
		coach.setAddress1("address1" + newCoachId);
		coach.setAddress2("address2" + newCoachId);
		coach.setTelephone("phone" + newCoachId);
		coach.setEmail("email" + newCoachId);
		coach.setPassword("pwd" + newCoachId);
		coach.setZipcode("zip" + newCoachId);
		coach.setStatut("VALIDE");
		coach.setPassword("pwd" + newCoachId);
		coach.setRole(roleService.findByRoleName("ROLE_COACH"));
		userRepository.save(coach);
		return coach;
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
	private User createNewUser() throws CreateException, FinderException {
		// Create Coach
		final String newUserId = counterService.getUniqueId("Coach");
		final User user = new User("user" + newUserId, "firstname" + newUserId, "lastname" + newUserId);
		user.setCity("city" + newUserId);
		user.setCountry("cnty" + newUserId);
		user.setState("state" + newUserId);
		user.setAddress1("address1" + newUserId);
		user.setAddress2("address2" + newUserId);
		user.setTelephone("phone" + newUserId);
		user.setEmail("email" + newUserId);
		user.setPassword("pwd" + newUserId);
		user.setZipcode("zip" + newUserId);
		user.setStatut("VALIDE");
		user.setPassword("pwd" + newUserId);
		userRepository.save(user);
		return user;
	}

	// Creates an event linked to an existing activity
	private EventUser createEventUserForUser(final User user) throws CreateException, FinderException {

		// Create Event
		Event event = createNewEvent();

		final EventUser eventReg = new EventUser(user, event);
		eventUserRepository.save(eventReg);
		return eventReg;
	}

	// Creates an event linked to an existing activity
	private EventUser createEventUserForEvent(final Event event) throws CreateException, FinderException {
		// Create User
		User user = createNewUser();
		final EventUser eventUser = new EventUser(user, event);
		eventUserRepository.save(eventUser);
		return eventUser;
	}

	private void removeEvent(final long eventId) throws RemoveException, ObjectNotFoundException {
		Event event = eventRepository.findById(eventId).get();
		final Activity activity = event.getActivity();
		final User user = event.getCoach();
		eventRepository.deleteById(eventId);
		activityRepository.delete(activity);
		userRepository.delete(user);

	}

	private void removeUser(final User coach) throws RemoveException, ObjectNotFoundException {
		userRepository.delete(coach);
	}
}
