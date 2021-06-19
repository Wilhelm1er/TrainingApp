package com.sport.training.domain.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sport.training.domain.model.Activity;
import com.sport.training.domain.model.Discipline;
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
@Transactional
public class ActivityDAOTest {

	private static final String COUNTER_NAME = "activity";
	@Autowired
	private ActivityRepository activityRepository;
	@Autowired
	private DisciplineRepository disciplineRepository;
	@Autowired
	private CounterService counterService;

	// ==================================
	// = Test cases =
	// ==================================
	/**
	 * This test tries to find an object with a invalid identifier.
	 */
	@Test
	public void testDomainFindActivityWithInvalidValues() throws Exception {

		// Finds an object with a unknown identifier
		final String id = counterService.getUniqueId(COUNTER_NAME);
		try {
			findActivity(id);
			fail("Object with unknonw id should not be found");
		} catch (NoSuchElementException e) {
		}

		// Finds an object with an empty identifier
		try {
			activityRepository.findById(new String()).get();
			fail("Object with empty id should not be found");
		} catch (Exception e) {
		}

		// Finds an object with a null identifier
		try {
			activityRepository.findById(null).get();
			fail("Object with null id should not be found");
		} catch (Exception e) {
		}
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testDomainFindAllActivities() throws Exception {
		final String id = counterService.getUniqueId(COUNTER_NAME);

		// First findAll
		final int firstSize = findAllActivities();

		// Create an object
		createActivity(id);

		// Ensures that the object exists
		try {
			findActivity(id);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// second findAll
		final int secondSize = findAllActivities();
		
		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Cleans the test environment
		removeActivity(id);

		try {
			findActivity(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testDomainFindAllActivitysForADiscipline() throws Exception {
		Discipline newdiscipline = createNewDiscipline();
		final String disciplineId = newdiscipline.getId();

		// First findAll
		final int firstSize = findAllActivities(disciplineId);

		// Checks that the collection is empty
		if (firstSize != 0)
			fail("The collection should be empty");

		// Create an object
		Activity activity = createActivityForDiscipline(newdiscipline);

		// Ensures that the object exists
		try {
			findActivity(activity.getId());
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// second findAll
		final int secondSize = findAllActivities(disciplineId);

		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Cleans the test environment
		removeActivity(activity.getId());

	}

	/**
	 * This method ensures that creating an object works. It first finds the object,
	 * makes sure it doesn't exist, creates it and checks it then exists.
	 */
	@Test
	public void testDomainCreateActivity() throws Exception {
		final String id = counterService.getUniqueId(COUNTER_NAME);
		Activity activity = null;

		// Ensures that the object doesn't exist
		try {
			activity = findActivity(id);
			fail("Object has not been created yet it shouldn't be found");
		} catch (NoSuchElementException e) {
		}

		// Creates an object
		createActivity(id);

		// Ensures that the object exists
		try {
			activity = findActivity(id);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkActivity(activity, id);

		// Cleans the test environment
		removeActivity(id);

		try {
			findActivity(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test make sure that updating an object success
	 */
	@Test
	public void testDomainUpdateActivity() throws Exception {
		final String id = counterService.getUniqueId(COUNTER_NAME);

		// Creates an object
		createActivity(id);

		// Ensures that the object exists
		Activity activity = null;
		try {
			activity = findActivity(id);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkActivity(activity, id);

		// Updates the object with new values
		updateActivity(activity, id + 1);

		// Ensures that the object still exists
		Activity activityUpdated = null;
		try {
			activityUpdated = findActivity(id);
		} catch (NoSuchElementException e) {
			fail("Object should be found");
		}

		// Checks that the object values have been updated
		checkActivity(activityUpdated, id + 1);

		// Cleans the test environment
		removeActivity(id);

		try {
			findActivity(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test ensures that the system cannont remove an unknown object
	 */
	@Test
	public void testDomainDeleteUnknownActivity() throws Exception {
		// Removes an unknown object
		try {
			activityRepository.delete(null);
			fail("Deleting an unknown object should break");
		} catch (Exception e) {
		}
	}

	// ==================================
	// = Private Methods =
	// ==================================
	private Activity findActivity(final String id) throws NoSuchElementException {
		final Activity activity = activityRepository.findById(id).get();
		return activity;
	}

	private int findAllActivities() throws FinderException {
		try {
			return ((Collection<Activity>) activityRepository.findAll()).size();
		} catch (Exception e) {
			return 0;
		}
	}

	private int findAllActivities(String disciplineId) throws FinderException {
		try {
			Discipline discipline = disciplineRepository.findById(disciplineId).get();
			return ((Collection<Activity>) activityRepository.findAllByDiscipline(discipline)).size();
		} catch (Exception e) {
			return 0;
		}
	}

	// Creates a discipline first and then a activity linked to this discipline
	private void createActivity(final String id) throws CreateException, ObjectNotFoundException {
		// Create discipline
		final String newdisciplineId = counterService.getUniqueId("discipline");
		final Discipline discipline = new Discipline(newdisciplineId, "name" + newdisciplineId,
				"description" + newdisciplineId);
		discipline.setDocuments("documents" + newdisciplineId);
		disciplineRepository.save(discipline);
		// Create activity
		final Activity activity = new Activity(id, "name" + id, "description" + id, discipline);
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
	}

	// Creates a discipline and updates the activity with this new discipline
	private void updateActivity(final Activity activity, final String id)
			throws UpdateException, CreateException, ObjectNotFoundException {
		// Create discipline
		final String newdisciplineId = counterService.getUniqueId("discipline");
		final Discipline discipline = new Discipline(newdisciplineId, "name" + newdisciplineId,
				"description" + newdisciplineId);
		discipline.setDocuments("documents" + newdisciplineId);
		disciplineRepository.save(discipline);

		// get old discipline
		Discipline disc = activity.getDiscipline();

		// Update activity with new discipline
		activity.setName("name" + id);
		activity.setDescription("description" + id);
		activity.setDiscipline(discipline);
		activity.setCreditcostMax(4.0);
		activity.setCreditcostMin(1.0);
		activityRepository.save(activity);

		// delete old discipline
		disciplineRepository.delete(disc);
	}

	private void removeActivity(final String id) throws RemoveException, ObjectNotFoundException {
		final String activityId = id;
		Activity activity = activityRepository.findById(activityId).get();
		final String disciplineId = activity.getDiscipline().getId();
		activityRepository.deleteById(id);
		disciplineRepository.deleteById(disciplineId);
	}

	private void checkActivity(final Activity activity, final String id) {
		assertEquals("name", "name" + id, activity.getName());
		assertEquals("description", "description" + id, activity.getDescription());
		assertNotNull("discipline", activity.getDiscipline());
	}

	// Creates a new discipline and return it
	private Discipline createNewDiscipline() throws CreateException {
		final String newDisciplineId = counterService.getUniqueId("Discipline");
		final Discipline discipline = new Discipline("cat" + newDisciplineId, "name" + newDisciplineId,
				"description" + newDisciplineId);
		discipline.setDocuments("documents" + newDisciplineId);
		disciplineRepository.save(discipline);
		return discipline;
	}

	// Creates an activity linked to an existing discipline
	private Activity createActivityForDiscipline(final Discipline discipline) throws CreateException {
		final String id = counterService.getUniqueId(COUNTER_NAME);
		final Activity activity = new Activity(id, "name" + id, "description" + id, discipline);
		activity.setCreditcostMax(4.0);
		activity.setCreditcostMin(1.0);
		activityRepository.save(activity);
		return activity;
	}

}
