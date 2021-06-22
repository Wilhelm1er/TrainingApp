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
import com.sport.training.domain.model.Notation;
import com.sport.training.domain.service.CounterService;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.ObjectNotFoundException;
import com.sport.training.exception.RemoveException;
import com.sport.training.exception.UpdateException;

/**
 * This class tests the notationDAO class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
//@Transactional
public class NotationDAOTest {

	private static Logger logger = LogManager.getLogger(NotationDAOTest.class);

	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private NotationRepository notationRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DisciplineRepository disciplineRepository;
	@Autowired
	private ActivityRepository activityRepository;

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
	public void testDomainFindNotationWithInvalidValues() throws Exception {

		// Finds an object with a unknown identifier
		final Long id = notationRepository.findLastId().orElse(10000L) + 1;
		try {
			findNotation(id);
			fail("Object with unknonw id should not be found");
		} catch (NoSuchElementException e) {
		}

		// Finds an object with an empty identifier
		try {
			notationRepository.findById(random.nextLong() + 1L).get();
			fail("Object with empty id should not be found");
		} catch (Exception e) {
		}

		// Finds an object with a null identifier
		try {
			notationRepository.findById(null).get();
			fail("Object with null id should not be found");
		} catch (Exception e) {
		}
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testDomainFindAllNotations() throws Exception {
		final Long id = notationRepository.findLastId().orElse(10000L) + 1;

		// First findAll
		final int firstSize = findAllNotations();
		// Create an object
		final long notationId = createNotation(id);

		// Ensures that the object exists
		try {
			findNotation(notationId);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}
		// second findAll
		final int secondSize = findAllNotations();
		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Cleans the test environment
		removeNotation(notationId);

		try {
			findNotation(notationId);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testDomainFindAllNotationsForACoach() throws Exception {
		User newCoach = createNewCoach();
		final String coachId = newCoach.getUsername();

		// First findAll
		final int firstSize = findAllNotationsByCoach(coachId);

		// Checks that the collection is empty
		if (firstSize != 0)
			fail("The collection should be empty");

		// Create an object
		Notation notation1 = createNotationForCoach(newCoach);

		// Ensures that the object exists
		try {
			findNotation(notation1.getId());
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// second findAll
		final int secondSize = findAllNotationsByCoach(coachId);

		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Create an new object with a different id
		Notation notation2 = createNotationForCoach(newCoach);

		// Ensures that the new object exists
		try {
			findNotation(notation2.getId());
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// third findAll
		final int thirdSize = findAllNotationsByCoach(coachId);

		// Checks that the collection size has increase of one
		if (thirdSize != secondSize + 1)
			fail("The collection should have the same size");

		// Cleans the test environment
		notationRepository.delete(notation1);
		notationRepository.delete(notation2);
		removeUser(newCoach);

	}

	/**
	 * This method ensures that creating an object works. It first finds the object,
	 * makes sure it doesn't exist, creates it and checks it then exists.
	 */
	@Test
	public void testDomainCreateNotation() throws Exception {
		final Long id = notationRepository.findLastId().orElse(10000L) + 1;
		Notation notation = null;

		// Creates an object
		final long notationId = createNotation(id);
		// Ensures that the object doesn't exist
		try {
			notation = findNotation(notationId);
			fail("Object has not been created yet it shouldn't be found");
		} catch (NoSuchElementException e) {
		}

		// Checks that it's the right object
		checkNotation(notation, id);

		// Cleans the test environment
		removeNotation(notationId);

		try {
			findNotation(notationId);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test make sure that updating an object success
	 */
	@Test
	public void testDomainUpdateNotation() throws Exception {
		final Long id = notationRepository.findLastId().orElse(10000L) + 1;

		// Creates an object
		final long notationId = createNotation(id);

		// Ensures that the object exists
		Notation notation = null;
		try {
			notation = findNotation(id);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkNotation(notation, id);

		// Updates the object with new values
		updateNotation(notation, id + 1);

		// Ensures that the object still exists
		Notation notationUpdated = null;
		try {
			notationUpdated = findNotation(notationId);
		} catch (NoSuchElementException e) {
			fail("Object should be found");
		}

		// Checks that the object values have been updated
		checkNotation(notationUpdated, id + 1);

		// Cleans the test environment
		removeNotation(notationId);

		try {
			findNotation(notationId);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test ensures that the system cannont remove an unknown object
	 */
	@Test
	public void testDomainDeleteUnknownNotation() throws Exception {
		// Removes an unknown object
		try {
			notationRepository.delete(null);
			fail("Deleting an unknown object should break");
		} catch (Exception e) {
		}
	}

	// ==================================
	// = Private Methods =
	// ==================================
	private Notation findNotation(final Long id) throws NoSuchElementException {
		final Notation notation = notationRepository.findById(id).get();
		return notation;
	}

	private int findAllNotations() throws FinderException {
		try {
			return ((Collection<Notation>) notationRepository.findAll()).size();
		} catch (Exception e) {
			logger.info("Exception is ... " + e.getMessage());
			return 0;
		}
	}

	private int findAllNotationsByCoach(String coachId) throws FinderException {
		try {
			User coach = userRepository.findById(coachId).get();
			return notationRepository.findAllByCoach(coach).size();
		} catch (Exception e) {
			return 0;
		}
	}

	// Creates a discipline first and then a notation linked to this discipline
	private long createNotation(final Long id) throws CreateException, RemoveException, FinderException {
		// Create Athlete
		User athlete = createNewAthlete();

		// Create Coach
		User coach = createNewCoach();

		// Create Event
		Event event = createNewEvent();

		// Create notation
		final Notation notation = new Notation(5, "comments" + id, athlete, coach, event);

		notationRepository.save(notation);
		return notation.getId();
	}

	// Creates an event linked to an existing activity
	private Notation createNotationForCoach(final User coach) throws CreateException, RemoveException, FinderException {
		final String id = counterService.getUniqueId("Coach");

		// Create Athlete
		User athlete = createNewAthlete();

		// Create Athlete
		Event event = createNewEvent();

		final Notation notation = new Notation(2, "comments" + id, athlete, coach, event);
		notationRepository.save(notation);
		return notation;
	}

	// Creates a discipline and updates the notation with this new discipline
	private void updateNotation(final Notation notation, final long id)
			throws UpdateException, CreateException, RemoveException, FinderException {
		// get old activity
		User athl = notation.getAthlete();

		// Create Activity
		User athlete = createNewAthlete();

		// get old activity
		User coa = notation.getCoach();

		// Create Coach
		User coach = createNewCoach();

		// get old activity
		Event evt = notation.getEvent();

		// Create Coach
		Event event = createNewEvent();

		// Update notation with new discipline
		notation.setNote(3);
		notation.setComments("comments" + id);
		notation.setAthlete(athlete);
		notation.setCoach(coach);
		notation.setEvent(event);
		notationRepository.save(notation);

		// delete olds
		removeUser(athl);
		removeUser(coa);
		removeEvent(evt.getId());
	}

	private void checkNotation(final Notation notation, final Long id) {
		assertEquals("comments", "comments" + id, notation.getComments());
		assertNotNull("athlete", notation.getAthlete());
		assertNotNull("coach", notation.getCoach());
		assertNotNull("event", notation.getEvent());
	}

	private void removeNotation(final long notationId) throws RemoveException, ObjectNotFoundException {
		Notation notation = notationRepository.findById(notationId).get();
		User athlete = notation.getAthlete();
		User coach = notation.getCoach();
		Event event = notation.getEvent();
		notationRepository.deleteById(notationId);
		userRepository.delete(athlete);
		userRepository.delete(coach);
		eventRepository.delete(event);
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

	// Creates an athlete
	private User createNewAthlete() throws CreateException, FinderException {
		// Create Athlete
		final String newAthleteId = counterService.getUniqueId("Athlete");
		final User athlete = new User("user" + newAthleteId, "firstname" + newAthleteId, "lastname" + newAthleteId);
		athlete.setCity("city" + newAthleteId);
		athlete.setCountry("cnty" + newAthleteId);
		athlete.setState("state" + newAthleteId);
		athlete.setAddress1("address1" + newAthleteId);
		athlete.setAddress2("address2" + newAthleteId);
		athlete.setTelephone("phone" + newAthleteId);
		athlete.setEmail("email" + newAthleteId);
		athlete.setPassword("pwd" + newAthleteId);
		athlete.setZipcode("zip" + newAthleteId);
		athlete.setStatut("VALIDE");
		athlete.setPassword("pwd" + newAthleteId);
		athlete.setRole(roleService.findByRoleName("ROLE_ATHLETE"));
		userRepository.save(athlete);
		return athlete;
	}

	// Creates a discipline first, then a activity and then an event linked to this
	// activity
	private Event createNewEvent() throws CreateException, RemoveException, FinderException {

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

	private void removeUser(final User user) throws RemoveException, ObjectNotFoundException {
		userRepository.delete(user);
	}

	private void removeEvent(final long eventId) throws RemoveException, ObjectNotFoundException {
		Event event = eventRepository.findById(eventId).get();
		final String activityId = event.getActivity().getId();
		Activity activity = activityRepository.findById(activityId).get();
		final String disciplineId = activity.getDiscipline().getId();
		Discipline discipline = disciplineRepository.findById(disciplineId).get();
		final String userId = event.getCoach().getUsername();
		User user = userRepository.findById(userId).get();
		eventRepository.deleteById(eventId);
		disciplineRepository.delete(discipline);
		activityRepository.delete(activity);
		userRepository.delete(user);
	}
}
