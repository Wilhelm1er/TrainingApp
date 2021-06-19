package com.sport.training.domain.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Random;

import javax.transaction.Transactional;

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
@Transactional
public class EventDAOTest {

	private static final String COUNTER_NAME = "Event";

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
		final Long id = eventRepository.findLastId().orElse(10000L) + 1;

		// First findAll
		final int firstSize = findAllEvents();
		// Create an object
		final long eventId = createEvent(id);
		
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
			System.out.println("size 1: "+firstSize + "size 2: "+secondSize);
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
		final Long id = eventRepository.findLastId().orElse(10000L) + 1;
		Event event = null;

		// Ensures that the object doesn't exist
		try {
			event = findEvent(id);
			fail("Object has not been created yet it shouldn't be found");
		} catch (NoSuchElementException e) {
		}

		// Creates an object
		createEvent(id);
System.out.println("event: "+event);
		// Ensures that the object exists
		try {
			event = findEvent(id);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkEvent(event, id);

		// Cleans the test environment
		removeEvent(id);

		try {
			findEvent(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test make sure that updating an object success
	 */
	@Test
	public void testDomainUpdateEvent() throws Exception {
		final Long id = eventRepository.findLastId().orElse(10000L) + 1;

		// Creates an object
		createEvent(id);

		// Ensures that the object exists
		Event event = null;
		try {
			event = findEvent(id);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkEvent(event, id);

		// Updates the object with new values
		updateEvent(event, id + 1);

		// Ensures that the object still exists
		Event eventUpdated = null;
		try {
			eventUpdated = findEvent(id);
		} catch (NoSuchElementException e) {
			fail("Object should be found");
		}

		// Checks that the object values have been updated
		checkEvent(eventUpdated, id + 1);

		// Cleans the test environment
		removeEvent(id);

		try {
			findEvent(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
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
			User user= userRepository.findById(coachId).get();
			return ((Collection<Event>) eventRepository.findAllByCoach(user)).size();
		} catch (Exception e) {
			return 0;
		}
	}

	// Creates a discipline first, then a activity and then an event linked to this
	// activity
	private long createEvent(final long id) throws CreateException, RemoveException, FinderException {
		
		  //Create Coach 
		User coach = createNewCoach();
		  
		  // Create Activity 
		Activity activity = createNewActivity();
		 
		
		// Create Event
		final Event event = new Event("name" + id, _defaultDate, _defaultCreditCost,
				coach,activity);
		
		  event.setDuration(50); event.setIntensity(3); event.setEquipment("equipment"
		  + String.valueOf(id)); event.setDescription("description" +
		  String.valueOf(id));
		 
	
		eventRepository.save(event);
		return event.getId();
	}

	// Creates a discipline, an activity and updates the event with this new
	// activity
	private void updateEvent(final Event event, final long id)
			throws UpdateException, CreateException, RemoveException, FinderException {
		// get old activity
		Activity activ = event.getActivity();

		// Create Activity
		Activity activity  = createNewActivity();

		// get old activity
		User coa = event.getCoach();
				
		// Create Coach
		User coach  = createNewCoach();
		// Updates the event
		event.setName("name" + id);
		event.setCreditCost(_defaultCreditCost);
		event.setActivity(activity);
		event.setCoach(coach);
		eventRepository.save(event);
		// delete old discipline & activity
		removeActivity(activ);
		removeCoach(coa);
	}

	private void checkEvent(final Event event, final Long id) {
		assertEquals("name", "name" + id, event.getName());
		assertTrue("creditCost", _defaultCreditCost == event.getCreditCost());
		assertNotNull("activity", event.getActivity());
		assertNotNull("coach", event.getCoach());
	}

	private void removeEvent(final long id) throws RemoveException, ObjectNotFoundException {
		Event event = eventRepository.findByName("name" + id);
		final String activityId = event.getActivity().getId();
		Activity activity = activityRepository.findById(activityId).get();
		final String disciplineId = activity.getDiscipline().getId();
		Discipline discipline = disciplineRepository.findById(disciplineId).get();
		final String userId = event.getCoach().getUsername();
		User user = userRepository.findById(userId).get();
		
		disciplineRepository.delete(discipline);
		activityRepository.delete(activity);
		userRepository.delete(user);
		eventRepository.deleteByName("name" +id);
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
			final Activity activity = new Activity(newactivityId, "name" + newactivityId, "description" + newactivityId, discipline);	
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
			System.out.println("activity:" +activity);
		return activity;
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

	// Creates an event linked to an existing activity
	private Event createEventForCoach(final User coach) throws CreateException, ObjectNotFoundException {
		final String id = counterService.getUniqueId(COUNTER_NAME);
		
		// Create Activity
		Activity activity  = createNewActivity();
		
		final Event event = new Event("name" + id, _defaultDate, _defaultCreditCost, coach, activity);
		eventRepository.save(event);
		return event;
	}
	
	// Creates an event linked to an existing activity
	private Event createEventForActivity(final Activity activity) throws CreateException, FinderException {
		final String id = counterService.getUniqueId(COUNTER_NAME);
		// Create Coach
		User coach  = createNewCoach();
		final Event event = new Event("name" + id, _defaultDate, _defaultCreditCost, coach, activity);
		eventRepository.save(event);
		return event;
	}

	private void removeActivity(final Activity activity) throws RemoveException, ObjectNotFoundException {
		final String categoryId = activity.getDiscipline().getId();
		Discipline discipline = disciplineRepository.findById(categoryId).get();
		disciplineRepository.delete(discipline);
		activityRepository.delete(activity);
	}

	private void removeCoach(final User coach) throws RemoveException, ObjectNotFoundException {
		userRepository.delete(coach);
	}
}
