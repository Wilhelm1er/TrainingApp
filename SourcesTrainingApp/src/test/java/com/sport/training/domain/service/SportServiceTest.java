package com.sport.training.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit4.SpringRunner;

import com.sport.training.authentication.domain.dao.UserRepository;
import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.model.User;
import com.sport.training.authentication.domain.service.RoleService;
import com.sport.training.authentication.domain.service.UserService;
import com.sport.training.domain.dao.EventRepository;
import com.sport.training.domain.dto.ActivityDTO;
import com.sport.training.domain.dto.DisciplineDTO;
import com.sport.training.domain.dto.EventDTO;
import com.sport.training.domain.model.Activity;
import com.sport.training.domain.model.Event;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.DuplicateKeyException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.RemoveException;
import com.sport.training.exception.UpdateException;

import junit.framework.TestCase;

/**
 * This class tests the CatalogService class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
//@Transactional
public class SportServiceTest extends TestCase {

	private final Double _defaultCreditCost = 1.0;
	private final LocalDateTime _defaultDate = LocalDateTime.of(2021, 7, 12, 20, 15, 50, 345678900);

	@Autowired
	private SportService sportService;
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CounterService counterService;
	@Autowired
	private UserService userService;

	private Random random = new Random();

	// ==================================
	// = Test cases for Discipline =
	// ==================================
	/**
	 * This test tries to find an object with a invalid identifier.
	 */
	@Test
	public void testServiceFindDisciplineWithInvalidValues() throws Exception {

		// Finds an object with a unknown identifier
		final String id = counterService.getUniqueId("Discipline");
		try {
			sportService.findDiscipline(id);
			fail("Object with unknonw id should not be found");
		} catch (FinderException e) {
		}

		// Finds an object with an empty identifier
		try {
			sportService.findDiscipline(new String());
			fail("Object with empty id should not be found");
		} catch (FinderException e) {
		}

		// Finds an object with a null identifier
		try {
			sportService.findDiscipline(null);
			fail("Object with null id should not be found");
		} catch (FinderException e) {
		}
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testServiceFindAllDisciplines() throws Exception {
		final String id = counterService.getUniqueId("Discipline");

		// First findAll
		final int firstSize = findAllDisciplines();

		// Creates an object
		createDiscipline(id);

		// Ensures that the object exists
		try {
			findDiscipline(id);
		} catch (FinderException e) {
			fail("Object has been created it should be found");
		}

		// Second findAll
		final int secondSize = findAllDisciplines();

		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Cleans the test environment
		deleteDiscipline(id);

		try {
			findDiscipline(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (FinderException e) {
		}
	}

	/**
	 * This method ensures that creating an object works. It first finds the object,
	 * makes sure it doesn't exist, creates it and checks it then exists.
	 */
	@Test
	public void testServiceCreateDiscipline() throws Exception {
		final String id = counterService.getUniqueId("Discipline");
		DisciplineDTO disciplineDTO = null;

		// Ensures that the object doesn't exist
		try {
			findDiscipline(id);
			fail("Object has not been created yet it shouldn't be found");
		} catch (FinderException e) {
		}

		// Creates an object
		createDiscipline(id);

		// Ensures that the object exists
		try {
			disciplineDTO = findDiscipline(id);
		} catch (FinderException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkDiscipline(disciplineDTO, id);

		// Creates an object with the same identifier. An exception has to be thrown
		try {
			createDiscipline(id);
			fail("An object with the same id has already been created");
		} catch (DuplicateKeyException e) {
		}

		// Cleans the test environment
		deleteDiscipline(id);

		try {
			findDiscipline(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (FinderException e) {
		}
	}

	/**
	 * This test tries to create an object with a invalid values.
	 */
	@Test
	public void testServiceCreateDisciplineWithInvalidValues() throws Exception {
		DisciplineDTO disciplineDTO;

		// Creates an object with a null parameter
		try {
			sportService.createDiscipline(null);
			fail("Object with null parameter should not be created");
		} catch (CreateException e) {
		}

		// Creates an object with empty values
		try {
			disciplineDTO = new DisciplineDTO(new String(), new String(), new String());
			sportService.createDiscipline(disciplineDTO);
			fail("Object with empty values should not be created");
		} catch (Exception e) {
			assertTrue("Il devrait s'agir d'une  CreateException", e instanceof CreateException);
		}

		// Creates an object with null values
		try {
			disciplineDTO = new DisciplineDTO(null, null, null);
			sportService.createDiscipline(disciplineDTO);
			fail("Object with null values should not be created");
		} catch (Exception e) {
			assertTrue("Il devrait s'agir d'une  CreateException", e instanceof CreateException);
		}
	}

	/**
	 * This test make sure that updating an object success
	 */
	@Test
	public void testServiceUpdateDiscipline() throws Exception {
		final String id = counterService.getUniqueId("Discipline");
		final String updatePattern = id + "_updated";

		// Creates an object
		createDiscipline(id);

		// Ensures that the object exists
		DisciplineDTO disciplineDTO = null;
		try {
			disciplineDTO = findDiscipline(id);
		} catch (FinderException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkDiscipline(disciplineDTO, id);

		// Updates the object with new values
		updateDiscipline(disciplineDTO, updatePattern);

		// Ensures that the object still exists
		DisciplineDTO disciplineUpdated = null;
		try {
			disciplineUpdated = findDiscipline(id);
		} catch (FinderException e) {
			fail("Object should be found");
		}

		// Checks that the object values have been updated
		checkDiscipline(disciplineUpdated, updatePattern);

		// Cleans the test environment
		deleteDiscipline(id);

		try {
			findDiscipline(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (FinderException e) {
		}
	}

	/**
	 * This test tries to update an object with a invalid values.
	 */
	@Test
	public void testServiceUpdateDisciplineWithInvalidValues() throws Exception {
		DisciplineDTO disciplineDTO;

		// Updates an object with a null parameter
		try {
			sportService.updateDiscipline(null);
			fail("Object with null parameter should not be updated");
		} catch (UpdateException e) {
		}

		// Updates an object with empty values
		try {
			disciplineDTO = new DisciplineDTO(new String(), new String(), new String());
			sportService.updateDiscipline(disciplineDTO);
			fail("Object with empty values should not be updated");
		} catch (Exception e) {
			assertTrue("Il devrait s'agir d'une  UpdateException", e instanceof UpdateException);
		}

		// Updates an object with null values
		try {
			disciplineDTO = new DisciplineDTO(null, null, null);
			sportService.updateDiscipline(disciplineDTO);
			fail("Object with null values should not be updated");
		} catch (Exception e) {
			assertTrue("Il devrait s'agir d'une  InvalidDataAccessApiUsageException",
					e instanceof InvalidDataAccessApiUsageException);
		}
	}

	/**
	 * This test ensures that the system cannont remove an unknown object
	 */
	@Test
	public void testServiceDeleteUnknownDiscipline() throws Exception {
		final String id = counterService.getUniqueId("Discipline");

		// Ensures that the object doesn't exist
		try {
			findDiscipline(id);
			fail("Object has not been created it shouldn't be found");
		} catch (FinderException e) {
		}

		// Delete the unknown object
		try {
			deleteDiscipline(id);
			fail("Deleting an unknown object should break");
		} catch (RemoveException e) {
		}
	}

	// ==================================
	// = Test cases for activity =
	// ==================================
	/**
	 * This test tries to find an object with a invalid identifier.
	 */
	@Test
	public void testServiceFindActivityWithInvalidValues() throws Exception {

		// Finds an object with a unknown identifier
		final String id = counterService.getUniqueId("Activity");
		try {
			sportService.findActivity(id);
			fail("Object with unknonw id should not be found");
		} catch (FinderException e) {
		}

		// Finds an object with an empty identifier
		try {
			sportService.findActivity(new String());
			fail("Object with empty id should not be found");
		} catch (FinderException e) {
		}

		// Finds an object with a null identifier
		try {
			sportService.findActivity(null);
			fail("Object with null id should not be found");
		} catch (FinderException e) {
		}
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testServiceFindAllActivities() throws Exception {
		final String id = counterService.getUniqueId("Activity");

		// First findAll
		final int firstSize = findAllActivities();

		// Creates an object
		createActivity(id);

		// Ensures that the object exists
		try {
			findActivity(id);
		} catch (FinderException e) {
			fail("Object has been created it should be found");
		}

		// Second findAll
		final int secondSize = findAllActivities();

		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Cleans the test environment
		deleteActivity(id);

		try {
			findActivity(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (FinderException e) {
		}
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testServiceFindAllActivitiesForADiscipline() throws Exception {
		DisciplineDTO newDisciplineDTO = createNewDiscipline();
		final String disciplineId = newDisciplineDTO.getId();

		// First findAll
		final int firstSize = findAllActivities(disciplineId);

		// Checks that the collection is empty
		if (firstSize != 0)
			fail("The collection should be empty");

		// Create an object
		ActivityDTO activityDTO = createActivityForDiscipline(newDisciplineDTO);

		// Ensures that the object exists
		try {
			findActivity(activityDTO.getId());
		} catch (FinderException e) {
			fail("Object has been created it should be found");
		}

		// second findAll
		final int secondSize = findAllActivities(disciplineId);

		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Cleans the test environment
		deleteActivity(activityDTO.getId());
	}

	/**
	 * This method ensures that creating an object works. It first finds the object,
	 * makes sure it doesn't exist, creates it and checks it then exists.
	 */
	@Test
	public void testServiceCreateActivity() throws Exception {
		final String id = counterService.getUniqueId("Activity");
		ActivityDTO activityDTO = null;

		// Ensures that the object doesn't exist
		try {
			findActivity(id);
			fail("Object has not been created yet it shouldn't be found");
		} catch (FinderException e) {
		}

		// Creates an object
		createActivity(id);

		// Ensures that the object exists
		try {
			activityDTO = findActivity(id);
		} catch (FinderException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkActivity(activityDTO, id);

		// Creates an object with the same identifier. An exception has to be thrown
		try {
			createActivity(id);
			fail("An object with the same id has already been created");
		} catch (DuplicateKeyException e) {
		}

		// Cleans the test environment
		deleteActivity(id);

		try {
			findActivity(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (FinderException e) {
		}
	}

	/**
	 * This test tries to create an object with a invalid values.
	 */
	@Test
	public void testServiceCreateActivityWithInvalidValues() throws Exception {
		ActivityDTO activityDTO;

		// Creates an object with a null parameter
		try {
			sportService.createActivity(null);
			fail("Object with null parameter should not be created");
		} catch (CreateException e) {
		}

		// Creates an object with empty values
		try {
			activityDTO = new ActivityDTO(new String(), new String(), new String());
			sportService.createActivity(activityDTO);
			fail("Object with empty values should not be created");
		} catch (Exception e) {
			assertTrue("Il devrait s'agir d'une  CreateException", e instanceof CreateException);
		}

		// Creates an object with null values
		try {
			activityDTO = new ActivityDTO(null, null, null);
			sportService.createActivity(activityDTO);
			fail("Object with null values should not be created");
		} catch (Exception e) {
			assertTrue("Il devrait s'agir d'une  CreateException", e instanceof CreateException);
		}
	}

	/**
	 * This test tries to create an object with a invalid linked object.
	 */
	@Test
	public void testServiceCreateActivityWithInvalidDiscipline() throws Exception {
		final String id = counterService.getUniqueId("Activity");
		ActivityDTO activityDTO;

		// Creates an object with no object linked
		try {
			activityDTO = new ActivityDTO(id, "name" + id, "description" + id);
			sportService.createActivity(activityDTO);
			fail("Object with no object linked should not be created");
		} catch (CreateException e) {
		}

		// Creates an object with a null linked object
		try {
			activityDTO = new ActivityDTO(id, "name" + id, "description" + id);
			sportService.createActivity(activityDTO);
			fail("Object with null object linked should not be created");
		} catch (Exception e) {
			assertTrue("Il devrait s'agir d'une  CreateException", e instanceof CreateException);
		}

		// Creates an object with an empty linked object
		try {
			activityDTO = new ActivityDTO(id, "name" + id, "description" + id);
			sportService.createActivity(activityDTO);
			fail("Object with an empty object linked should not be created");
		} catch (CreateException e) {
		}

		// Creates an object with an unknown linked object
		try {
			activityDTO = new ActivityDTO(id, "name" + id, "description" + id);
			sportService.createActivity(activityDTO);
			fail("Object with an unknown object linked should not be created");
		} catch (CreateException e) {
		}
	}

	/**
	 * This test make sure that updating an object success
	 */
	@Test
	public void testServiceUpdateActivity() throws Exception {
		final String id = counterService.getUniqueId("Activity");
		final String updatePattern = id + "_updated";

		// Creates an object
		createActivity(id);

		// Ensures that the object exists
		ActivityDTO activityDTO = null;
		try {
			activityDTO = findActivity(id);
		} catch (FinderException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkActivity(activityDTO, id);

		// Updates the object with new values
		updateActivity(activityDTO, updatePattern);

		// Ensures that the object still exists
		ActivityDTO activityDTOUpdated = null;
		try {
			activityDTOUpdated = findActivity(id);
		} catch (FinderException e) {
			fail("Object should be found");
		}

		// Checks that the object values have been updated
		checkActivity(activityDTOUpdated, updatePattern);

		// Cleans the test environment
		deleteActivity(id);

		try {
			findActivity(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (FinderException e) {
		}
	}

	/**
	 * This test tries to update an object with a invalid values.
	 */
	@Test
	public void testServiceUpdateActivityWithInvalidValues() throws Exception {
		ActivityDTO activityDTO;

		// Updates an object with a null parameter
		try {
			sportService.updateActivity(null);
			fail("Object with null parameter should not be updated");
		} catch (UpdateException e) {
		}

		// Updates an object with empty values
		try {
			activityDTO = new ActivityDTO(new String(), new String(), new String());
			sportService.updateActivity(activityDTO);
			fail("Object with empty values should not be updated");
		} catch (Exception e) {
			assertTrue("Il devrait s'agir d'une  UpdateException", e instanceof UpdateException);
		}

		// Updates an object with null values
		try {
			activityDTO = new ActivityDTO(null, null, null);
			sportService.updateActivity(activityDTO);
			fail("Object with null values should not be updated");
		} catch (Exception e) {
			assertTrue("Il devrait s'agir d'une  InvalidDataAccessApiUsageException",
					e instanceof InvalidDataAccessApiUsageException);
		}
	}

	/**
	 * This test ensures that the system cannont remove an unknown object
	 */
	@Test
	public void testServiceDeleteUnknownActivity() throws Exception {
		final String id = counterService.getUniqueId("Activity");

		// Ensures that the object doesn't exist
		try {
			findActivity(id);
			fail("Object has not been created it shouldn't be found");
		} catch (FinderException e) {
		}

		// Delete the unknown object
		try {
			deleteActivity(id);
			fail("Deleting an unknown object should break");
		} catch (FinderException | RemoveException e) {
		}
	}

	// ==================================
	// = Test cases for event =
	// ==================================
	/**
	 * This test tries to find an object with a invalid identifier.
	 */
	@Test
	public void testServiceFindEventWithInvalidValues() throws Exception {

		// Finds an object with a unknown identifier
		final Long id = eventRepository.findLastId().orElse(10000L) + 1;

		try {
			sportService.findEvent(id);
			fail("Object with unknonw id should not be found");
		} catch (FinderException e) {
		}

		// Finds an object with an empty identifier
		try {
			sportService.findEvent(random.nextLong() + 1L);
			fail("Object with empty id should not be found");
		} catch (FinderException e) {
		}

	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testServiceFindAllEvents() throws Exception {

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
		deleteEvent(eventId);

		try {
			findEvent(eventId);
			fail("Object has been deleted it shouldn't be found");
		} catch (FinderException e) {
		}
	}

	@Test
	public void testServiceFindAllEventsForCoach1() throws Exception {
		final String coachId = "coach1";
		final String firstname = "andrew";
		final String lastname = "wiggin";
		UserDTO coachDTO = findUser(coachId);
		ActivityDTO activityDTO = findActivity("ABDOS");
		// Create Event
		final EventDTO eventAddDTO = new EventDTO("name" + coachId, _defaultDate, _defaultCreditCost, coachDTO,
				activityDTO);
		eventAddDTO.setActivityDTO(activityDTO);
		eventAddDTO.setDuration(50);
		eventAddDTO.setIntensity(3);
		eventAddDTO.setEquipment("equipment" + coachId);
		eventAddDTO.setDescription("description" + coachId);
		sportService.createEvent(eventAddDTO);

		assertEquals(firstname, coachDTO.getFirstname());
		assertEquals(lastname, coachDTO.getLastname());
		List<EventDTO> eventDTOs = getAllEventsForActivity(coachId);
		for (EventDTO eventDTO : eventDTOs) {
			assertNotNull(eventDTO.getCoachDTO().getUsername());
		}
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testServiceFindAllEventsForAnActivity() throws Exception {
		ActivityDTO newActivityDTO = findActivity("BODY");
		final String activityId = newActivityDTO.getId();

		// First findAll
		final int firstSize = findAllEvents(activityId);

		// Checks that the collection is empty
		if (firstSize != 0)
			fail("The collection should be empty");

		// Create an object
		EventDTO eventDTO1 = createEventForActivity(newActivityDTO);

		// Ensures that the object exists
		try {
			findEvent(eventDTO1.getId());
		} catch (FinderException e) {
			fail("Object has been created it should be found");
		}

		// second findAll
		final int secondSize = findAllEvents(activityId);
		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Create an object
		EventDTO eventDTO2 = createEventForActivity(newActivityDTO);

		// Ensures that the object exists
		try {
			findEvent(eventDTO2.getId());
		} catch (FinderException e) {
			fail("Object has been created it should be found");
		}

		// third findAll
		final int thirdSize = findAllEvents(activityId);

		// Checks that the collection size has increase of one
		if (thirdSize != secondSize + 1)
			fail("The collection should have the same size");

		// Cleans the test environment
		deleteEvent(eventDTO1.getId());
		deleteEvent(eventDTO2.getId());
	}

	/**
	 * This method ensures that creating an object works. It first finds the object,
	 * makes sure it doesn't exist, creates it and checks it then exists.
	 */
	@Test
	public void testServiceCreateEvent() throws Exception {
		EventDTO eventDTO = null;

		// Creates an object
		final long eventId = createEvent();
		
		// Creates an object
		createEvent();

		// Ensures that the object exists
		try {
			eventDTO = findEvent(eventId);
		} catch (FinderException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkEvent(eventDTO, String.valueOf(eventId));

		// Cleans the test environment
		deleteEvent(eventId);

		try {
			findEvent(eventId);
			fail("Object has been deleted it shouldn't be found");
		} catch (FinderException e) {
		}
	}

	/**
	 * This test tries to create an object with a invalid values.
	 */
	@Test
	public void testServiceCreateEventWithInvalidValues() throws Exception {
		EventDTO eventDTO;

		// Creates an object with a null parameter
		try {
			sportService.createEvent(null);
			fail("Object with null parameter should not be created");
		} catch (CreateException e) {
		}

		// Creates an object with empty values
		try {
			eventDTO = new EventDTO(new String(), _defaultDate, null, new UserDTO(), new ActivityDTO());
			sportService.createEvent(eventDTO);
			fail("Object with empty values should not be created");
		} catch (Exception e) {
			assertTrue("Il devrait s'agir d'une  CreateException", e instanceof CreateException);
		}

		// Creates an object with null values
		try {
			eventDTO = new EventDTO(null, null, null, null, null);
			sportService.createEvent(eventDTO);
			fail("Object with null values should not be created");
		} catch (Exception e) {
			assertTrue("Il devrait s'agir d'une  CreateException", e instanceof CreateException);
		}
	}

	/**
	 * This test tries to create an object with a invalid linked object.
	 */
	@Test
	public void testServiceCreateEventWithInvalidActivity() throws Exception {
		final String id = counterService.getUniqueId("Event");
		EventDTO eventDTO;

		// Creates an object with no object linked
		try {
			eventDTO = new EventDTO("name" + id, _defaultDate, 0.0, new UserDTO(), new ActivityDTO());
			sportService.createEvent(eventDTO);
			fail("Object with no object linked should not be created");
		} catch (Exception e) {
			assertTrue("Il devrait s'agir d'une  CreateException", e instanceof CreateException);
		}

		// Creates an object with a null linked object
		try {
			eventDTO = new EventDTO("name" + id, _defaultDate, 0.0, new UserDTO(), new ActivityDTO());
			sportService.createEvent(eventDTO);
			fail("Object with null object linked should not be created");
		} catch (Exception e) {
			assertTrue("Il devrait s'agir d'une  CreateException", e instanceof CreateException);
		}

		// Creates an object with an empty linked object
		try {
			eventDTO = new EventDTO("name" + id, _defaultDate, 0.0, new UserDTO(), new ActivityDTO());
			sportService.createEvent(eventDTO);
			fail("Object with an empty object linked should not be created");
		} catch (Exception e) {
			assertTrue("Il devrait s'agir d'une  CreateException", e instanceof CreateException);
		}

		// Creates an object with an unknown linked object
		try {
			eventDTO = new EventDTO("name" + id, _defaultDate, 0.0, new UserDTO(), new ActivityDTO());
			sportService.createEvent(eventDTO);
			fail("Object with an unknown object linked should not be created");
		} catch (Exception e) {
			assertTrue("Il devrait s'agir d'une  CreateException", e instanceof CreateException);
		}
	}

	/**
	 * This test make sure that updating an object success
	 */
	@Test
	public void testServiceUpdateEvent() throws Exception {
		

		// Creates an object
		final long eventId = createEvent();
		final String updatePattern = String.valueOf(eventId) + "_updated";

		// Ensures that the object exists
		EventDTO eventDTO = null;
		try {
			eventDTO = findEvent(eventId);
		} catch (FinderException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkEvent(eventDTO, String.valueOf(eventId));

		// Updates the object with new values
		updateEvent(eventDTO, updatePattern);

		// Ensures that the object still exists
		EventDTO eventDTOUpdated = null;
		try {
			eventDTOUpdated = findEvent(eventId);
		} catch (FinderException e) {
			fail("Object should be found");
		}

		// Checks that the object values have been updated
		checkEvent(eventDTOUpdated, updatePattern);

		// Cleans the test environment
		deleteEvent(eventId);

		try {
			findEvent(eventId);
			fail("Object has been deleted it shouldn't be found");
		} catch (FinderException e) {
		}
	}

	/**
	 * This test tries to update an object with a invalid values.
	 */
	@Test
	public void testServiceUpdateEventWithInvalidValues() throws Exception {
		EventDTO eventDTO;

		// Updates an object with a null parameter
		try {
			sportService.updateEvent(null);
			fail("Object with null parameter should not be updated");
		} catch (UpdateException e) {
		}

		// Updates an object with empty values
		try {
			eventDTO = null;
			sportService.updateEvent(eventDTO);
			fail("Object with empty values should not be updated");
		} catch (Exception e) {
			assertTrue("Il devrait s'agir d'une UpdateException", e instanceof UpdateException);
		}

		// Updates an object with null values
		try {
			eventDTO = new EventDTO(new String(), _defaultDate, 0.0, new UserDTO(), new ActivityDTO());
			sportService.updateEvent(eventDTO);
			fail("Object with null values should not be updated");
		} catch (Exception e) {
			assertTrue("Il devrait s'agir d'une InvalidDataAccessApiUsageException",
					e instanceof InvalidDataAccessApiUsageException);
		}
	}

	/**
	 * This test ensures that the system cannont remove an unknown object
	 */
	@Test
	public void testServiceDeleteUnknownEvent() throws Exception {
		// Finds an object with a unknown identifier
		final Long id = eventRepository.findLastId().orElse(10000L) + 1;

		// Ensures that the object doesn't exist
		try {
			findEvent(id);
			fail("Object has not been created it shouldn't be found");
		} catch (FinderException e) {
		}

		// Delete the unknown object
		try {
			deleteEvent(id);
			fail("Deleting an unknown object should break");
		} catch (FinderException e) {
		} catch (RemoveException e) {
		}
	}

	// ==================================
	// = Private Methods =
	// ==================================

	// ==================================
	// = Private Methods for Discipline =
	// ==================================
	private DisciplineDTO findDiscipline(final String id) throws FinderException {
		final DisciplineDTO disciplineDTO = sportService.findDiscipline(id);
		return disciplineDTO;
	}

	private int findAllDisciplines() throws FinderException {
		try {
			return sportService.findDisciplines().size();
		} catch (FinderException e) {
			return 0;
		}
	}

	private void createDiscipline(final String id) throws CreateException {
		final DisciplineDTO disciplineDTO = new DisciplineDTO(id, "name" + id, "description" + id);
		disciplineDTO.setDocuments("documents" + id);
		sportService.createDiscipline(disciplineDTO);
	}

	private void updateDiscipline(final DisciplineDTO disciplineDTO, final String updatePattern)
			throws UpdateException {
		disciplineDTO.setName("name" + updatePattern);
		disciplineDTO.setDescription("description" + updatePattern);
		disciplineDTO.setDocuments("documents" + updatePattern);
		sportService.updateDiscipline(disciplineDTO);
	}

	private void deleteDiscipline(final String id) throws RemoveException, FinderException {
		sportService.deleteDiscipline(id);
	}

	private void checkDiscipline(final DisciplineDTO disciplineDTO, final String id) {
		assertEquals("name", "name" + id, disciplineDTO.getName());
		assertEquals("description", "description" + id, disciplineDTO.getDescription());
		assertEquals("documents", "documents" + id, disciplineDTO.getDocuments());
	}

	// ==================================
	// = Private Methods for Activity =
	// ==================================
	private ActivityDTO findActivity(final String id) throws FinderException {
		final ActivityDTO activityDTO = sportService.findActivity(id);
		return activityDTO;
	}

	private int findAllActivities() throws FinderException {
		try {
			return sportService.findActivities().size();
		} catch (FinderException e) {
			return 0;
		}
	}

	private int findAllActivities(String disciplineId) throws FinderException {
		try {
			return sportService.findActivities(disciplineId).size();
		} catch (FinderException e) {
			return 0;
		}
	}

	// Creates a discipline first and then a activity linked to this discipline
	private void createActivity(final String id) throws CreateException, RemoveException, FinderException {
		// Create Discipline
		final String disciplineId = counterService.getUniqueId("Discipline");
		final DisciplineDTO disciplineDTO = new DisciplineDTO(disciplineId, "name" + disciplineId,
				"description" + disciplineId);
		disciplineDTO.setDocuments("documents" + disciplineId);
		sportService.createDiscipline(disciplineDTO);
		// Create Activity
		final ActivityDTO activityDTO = new ActivityDTO(id, "name" + id, "description" + id);
		activityDTO.setDisciplineDTO(disciplineDTO);
		try {
			sportService.createActivity(activityDTO);
		} catch (DuplicateKeyException e) {
			deleteDiscipline(disciplineId);
			throw new DuplicateKeyException();
		}
	}

	// Creates a discipline and updates the activity with this new discipline
	private void updateActivity(final ActivityDTO activityDTO, final String id)
			throws UpdateException, CreateException, RemoveException, FinderException {
		// get old discipline id
		String catId = activityDTO.getDisciplineDTO().getId();
		// Create Discipline
		final String disciplineId = counterService.getUniqueId("Discipline");
		final DisciplineDTO disciplineDTO = new DisciplineDTO(disciplineId, "name" + disciplineId,
				"description" + disciplineId);
		disciplineDTO.setDocuments("documents" + disciplineId);
		sportService.createDiscipline(disciplineDTO);
		// Update Activity with new discipline
		activityDTO.setName("name" + id);
		activityDTO.setDescription("description" + id);
		activityDTO.setDisciplineDTO(disciplineDTO);
		sportService.updateActivity(activityDTO);
		// delete old discipline
		deleteDiscipline(catId);
	}

	private void deleteActivity(final String id) throws RemoveException, FinderException {
		final String activityId = id;
		final ActivityDTO activityDTO = sportService.findActivity(activityId);
		final String disciplineId = activityDTO.getDisciplineDTO().getId();
		sportService.deleteActivity(activityId);
		sportService.deleteDiscipline(disciplineId);
	}

	private void checkActivity(final ActivityDTO activityDTO, final String id) {
		assertEquals("name", "name" + id, activityDTO.getName());
		assertEquals("description", "description" + id, activityDTO.getDescription());
	}

	// Creates a new discipline and return it
	private DisciplineDTO createNewDiscipline() throws CreateException {
		final String disciplineId = counterService.getUniqueId("Discipline");
		final DisciplineDTO disciplineDTO = new DisciplineDTO(disciplineId, "name" + disciplineId,
				"description" + disciplineId);
		disciplineDTO.setDocuments("documents" + disciplineId);
		sportService.createDiscipline(disciplineDTO);
		return disciplineDTO;
	}

	// Creates a activity linked to an existing discipline
	private ActivityDTO createActivityForDiscipline(final DisciplineDTO disciplineDTO) throws CreateException {
		final String id = counterService.getUniqueId("Activity");
		final ActivityDTO activityDTO = new ActivityDTO(id, "name" + id, "description" + id);
		activityDTO.setDisciplineDTO(disciplineDTO);
		sportService.createActivity(activityDTO);
		return activityDTO;
	}

	// ==================================
	// = Private Methods for Event =
	// ==================================
	private EventDTO findEvent(final Long id) throws FinderException {
		final EventDTO eventDTO = sportService.findEvent(id);
		return eventDTO;
	}

	private int findAllEvents() throws FinderException {
		try {
			return ((List<EventDTO>)sportService.findEvents()).size();
		} catch (FinderException e) {
			return 0;
		}
	}

	private int findAllEvents(String activityId) throws FinderException {
		try {
			return sportService.findEventsByActivity(activityId).size();
		} catch (FinderException e) {
			return 0;
		}
	}

	private List<EventDTO> getAllEventsForActivity(String activityId) throws FinderException {
		try {
			return sportService.findEvents(activityId);
		} catch (FinderException e) {
			return null;
		}
	}

	// Creates a discipline first, then a activity and then an event linked to this
	// activity
	private long createEvent() throws CreateException, FinderException, RemoveException {
		final String coachId = "coach1";
		UserDTO coachDTO = findUser(coachId);
		ActivityDTO activityDTO = findActivity("ABDOS");
		// Create Event
		final EventDTO eventDTO = new EventDTO("name" + coachId, _defaultDate, _defaultCreditCost, coachDTO,
				activityDTO);
		eventDTO.setActivityDTO(activityDTO);
		eventDTO.setDuration(50);
		eventDTO.setIntensity(3);
		eventDTO.setEquipment("equipment" + coachId);
		eventDTO.setDescription("description" + coachId);
		
		EventDTO evtDTO= sportService.createEvent(eventDTO);
		
		return evtDTO.getId();
	}

	// Creates a discipline, a activity and updates the event with this new activity
	private void updateEvent(final EventDTO eventDTO, final String updatePattern)
			throws UpdateException, CreateException, FinderException, RemoveException {
		ActivityDTO activityDTO = findActivity("BODY");
		// Updates the event
		eventDTO.setName("Evt" + updatePattern);
		eventDTO.setActivityDTO(activityDTO);
		sportService.updateEvent(eventDTO);
	}

	private void deleteEvent(final long id) throws RemoveException, FinderException {
		EventDTO eventDTO = sportService.findEvent(id);
		System.out.println("id: "+id);
		final ActivityDTO activityDTO = eventDTO.getActivityDTO();
		System.out.println("activityDTO: "+activityDTO);
		final UserDTO user = eventDTO.getCoachDTO();
		System.out.println("user: "+user);
		sportService.deleteEvent(id);
	}

	private void checkEvent(final EventDTO eventDTO, final String id) {
		assertEquals("name", eventDTO.getName(), eventDTO.getName());
		assertNotNull("activity", eventDTO.getActivityDTO());
		assertNotNull("coach", eventDTO.getCoachDTO());
	}

	// Creates an event linked to an existing activity
	private EventDTO createEventForActivity(final ActivityDTO activityDTO) throws CreateException, FinderException {
		final String coachId = "coach1";
		UserDTO coachDTO = findUser(coachId);
		final EventDTO eventDTO = new EventDTO("for" + coachId, _defaultDate, _defaultCreditCost,
				coachDTO, activityDTO);
		eventDTO.setDuration(50);
		eventDTO.setIntensity(3);
		eventDTO.setEquipment("equipment" + coachId);
		eventDTO.setDescription("description" + coachId);
		sportService.createEvent(eventDTO);
		return eventDTO;
	}

	private UserDTO findUser(final String id) throws FinderException {
		final UserDTO userDTO = userService.findUser(id);
		return userDTO;
	}
}
