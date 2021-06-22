package com.sport.training.domain.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

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
import com.sport.training.domain.model.Discipline;
import com.sport.training.domain.model.DisciplineRegistry;
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
public class DisciplineRegistryDAOTest {

	private static Logger logger = LogManager.getLogger(DisciplineRegistryDAOTest.class);

	@Autowired
	private DisciplineRegistryRepository disciplineRegistryRepository;
	@Autowired
	private DisciplineRepository disciplineRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CounterService counterService;
	@Autowired
	private RoleService roleService;

	private Random random = new Random();

	// ==================================
	// = Test cases =
	// ==================================
	/**
	 * This test tries to find an object with a invalid identifier.
	 */
	@Test
	public void testDomainFindDisciplineRegistryWithInvalidValues() throws Exception {

		// Finds an object with a unknown identifier
		final Long id = disciplineRegistryRepository.findLastId().orElse(10000L) + 1;

		try {
			findDisciplineRegistry(id);
			fail("Object with unknonw id should not be found");
		} catch (NoSuchElementException e) {
		}

		// Finds an object with an empty identifier
		try {
			disciplineRegistryRepository.findById(random.nextLong() + 1L).get();
			fail("Object with empty id should not be found");
		} catch (Exception e) {
		}

		// Finds an object with a null identifier
		try {
			disciplineRegistryRepository.findById(null).get();
			fail("Object with null id should not be found");
		} catch (Exception e) {
		}
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testDomainFindAllDisciplineRegistries() throws Exception {
		// First findAll
		final int firstSize = findAllDisciplineRegistries();
		// Create an object
		final long disciplineRegId = createDisciplineRegistry();

		// Ensures that the object exists
		try {
			findDisciplineRegistry(disciplineRegId);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// second findAll
		final int secondSize = findAllDisciplineRegistries();

		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Cleans the test environment
		removeDisciplineRegistry(disciplineRegId);

		try {
			findDisciplineRegistry(disciplineRegId);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testDomainFindAllDisciplineRegistriesForACoach() throws Exception {

		User newCoach = createNewCoach();
		final String coachId = newCoach.getUsername();

		// First findAll
		final int firstSize = findAllDisciplineRegistriesByCoach(coachId);

		// Checks that the collection is empty
		if (firstSize != 0)
			fail("The collection should be empty");

		// Create an object
		DisciplineRegistry disciplineReg1 = createDisciplineRegistryForCoach(newCoach);

		// Ensures that the object exists
		try {
			findDisciplineRegistry(disciplineReg1.getId());
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// second findAll
		final int secondSize = findAllDisciplineRegistriesByCoach(coachId);

		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Create an new object with a different id
		DisciplineRegistry disciplineReg2 = createDisciplineRegistryForCoach(newCoach);

		// Ensures that the new object exists
		try {
			findDisciplineRegistry(disciplineReg2.getId());
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// third findAll
		final int thirdSize = findAllDisciplineRegistriesByCoach(coachId);

		// Checks that the collection size has increase of one
		if (thirdSize != secondSize + 1)
			fail("The collection should have the same size");

		// Cleans the test environment
		disciplineRegistryRepository.delete(disciplineReg1);
		disciplineRegistryRepository.delete(disciplineReg2);
		removeCoach(newCoach);
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testDomainFindAllDisciplineRegistriesForADiscipline() throws Exception {

		Discipline newDiscipline = createNewDiscipline();
		final String disciplineId = newDiscipline.getId();

		// First findAll
		final int firstSize = findAllDisciplineRegistriesByDiscipline(disciplineId);

		// Checks that the collection is empty
		if (firstSize != 0)
			fail("The collection should be empty");

		// Create an object
		DisciplineRegistry disciplineReg1 = createDisciplineRegistryForDiscipline(newDiscipline);

		// Ensures that the object exists
		try {
			findDisciplineRegistry(disciplineReg1.getId());
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// second findAll
		final int secondSize = findAllDisciplineRegistriesByDiscipline(disciplineId);

		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Create an new object with a different id
		DisciplineRegistry disciplineReg2 = createDisciplineRegistryForDiscipline(newDiscipline);

		// Ensures that the new object exists
		try {
			findDisciplineRegistry(disciplineReg2.getId());
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// third findAll
		final int thirdSize = findAllDisciplineRegistriesByDiscipline(disciplineId);

		// Checks that the collection size has increase of one
		if (thirdSize != secondSize + 1)
			fail("The collection should have the same size");

		// Cleans the test environment
		disciplineRegistryRepository.delete(disciplineReg1);
		disciplineRegistryRepository.delete(disciplineReg2);
		removeDiscipline(newDiscipline.getId());
	}

	/**
	 * This method ensures that creating an object works. It first finds the object,
	 * makes sure it doesn't exist, creates it and checks it then exists.
	 */
	@Test
	public void testDomainCreateDisciplineRegistry() throws Exception {
		DisciplineRegistry disciplineRegistry = null;

		// Creates an object
		final long disciplineRegId = createDisciplineRegistry();
		// Ensures that the object doesn't exist
		try {
			disciplineRegistry = findDisciplineRegistry(disciplineRegId);
			fail("Object has not been created yet it shouldn't be found");
		} catch (NoSuchElementException e) {
		}

		// Checks that it's the right object
		checkDisciplineRegistry(disciplineRegistry);

		// Cleans the test environment
		removeDisciplineRegistry(disciplineRegId);

		try {
			findDisciplineRegistry(disciplineRegId);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test make sure that updating an object success
	 */
	@Test
	public void testDomainUpdateDisciplineRegistry() throws Exception {
		// Creates an object
		final long disciplineRegId = createDisciplineRegistry();

		// Ensures that the object exists
		DisciplineRegistry disciplineRegistry = null;
		try {
			disciplineRegistry = findDisciplineRegistry(disciplineRegId);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkDisciplineRegistry(disciplineRegistry);

		// Updates the object with new values
		updateDisciplineRegistry(disciplineRegistry);

		// Ensures that the object still exists
		DisciplineRegistry disciplineRegistryUpdated = null;
		try {
			disciplineRegistryUpdated = findDisciplineRegistry(disciplineRegId);
		} catch (NoSuchElementException e) {
			fail("Object should be found");
		}

		// Checks that the object values have been updated
		checkDisciplineRegistry(disciplineRegistryUpdated);

		// Cleans the test environment
		removeDisciplineRegistry(disciplineRegId);

		try {
			findDisciplineRegistry(disciplineRegId);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test ensures that the system cannont remove an unknown object
	 */
	@Test
	public void testDomainDeleteUnknownDisciplineRegistry() throws Exception {
		// Removes an unknown object
		try {
			disciplineRegistryRepository.delete(null);
			fail("Deleting an unknown object should break");
		} catch (Exception e) {
		}
	}

	// ==================================
	// = Private Methods =
	// ==================================
	private DisciplineRegistry findDisciplineRegistry(final Long id) throws NoSuchElementException {
		final DisciplineRegistry discReg = disciplineRegistryRepository.findById(id).get();
		return discReg;
	}

	private int findAllDisciplineRegistries() throws FinderException {
		try {
			return ((Collection<DisciplineRegistry>) disciplineRegistryRepository.findAll()).size();
		} catch (Exception e) {
			logger.info("Exception is ... " + e.getMessage());
			return 0;
		}
	}

	private int findAllDisciplineRegistriesByDiscipline(String disciplineId) throws FinderException {
		try {
			Discipline discipline = disciplineRepository.findById(disciplineId).get();
			return ((Collection<DisciplineRegistry>) disciplineRegistryRepository.findAllByDiscipline(discipline))
					.size();
		} catch (Exception e) {
			return 0;
		}
	}

	private int findAllDisciplineRegistriesByCoach(String coachId) throws FinderException {
		try {
			User coach = userRepository.findById(coachId).get();
			return ((Collection<DisciplineRegistry>) disciplineRegistryRepository.findAllByCoach(coach)).size();
		} catch (Exception e) {
			return 0;
		}
	}

	// Creates a discipline first, then a activity and then an event linked to this
	// activity
	private long createDisciplineRegistry() throws CreateException, RemoveException, FinderException {

		// Create new coach
		final String coachId = counterService.getUniqueId("Coach");
		final User coach = new User("user" + coachId, "firstname" + coachId, "lastname" + coachId);
		coach.setPassword("pwd" + coachId);
		coach.setRole(roleService.findByRoleName("ROLE_COACH"));
		coach.setStatut("VALIDE");
		userRepository.save(coach);

		// Finds an object with a unknown identifier
		final String id = counterService.getUniqueId("Discipline");
		final Discipline discipline = new Discipline("disc" + id, "name" + id, "description" + id);
		discipline.setDocuments("documents" + id);
		disciplineRepository.save(discipline);

		// Create DisciplineRegistry
		final DisciplineRegistry disciplineRegistry = new DisciplineRegistry(discipline, coach);

		disciplineRegistryRepository.save(disciplineRegistry);
		return disciplineRegistry.getId();
	}

	// Creates a discipline, an activity and updates the event with this new
	// activity
	private void updateDisciplineRegistry(final DisciplineRegistry disciplineRegistry)
			throws UpdateException, CreateException, RemoveException, FinderException {
		// get old user
		User coa = disciplineRegistry.getCoach();

		// Create new coach
		final String coachId = counterService.getUniqueId("coach");
		final User coach = new User("user" + coachId, "firstname" + coachId, "lastname" + coachId);
		coach.setPassword("pwd" + coachId);
		coach.setRole(roleService.findByRoleName("ROLE_COACH"));
		coach.setStatut("VALIDE");
		userRepository.save(coach);

		// get old event
		Discipline disc = disciplineRegistry.getDiscipline();

		// Create Event
		Discipline discipline = createNewDiscipline();
		// Updates the event
		disciplineRegistry.setCoach(coach);
		disciplineRegistry.setDiscipline(discipline);
		disciplineRegistryRepository.save(disciplineRegistry);
		// delete old discipline & activity
		// delete old coach
		disciplineRepository.delete(disc);
		userRepository.delete(coa);
	}

	private void checkDisciplineRegistry(final DisciplineRegistry disciplineRegistry) {
		assertNotNull("coach", disciplineRegistry.getCoach());
		assertNotNull("discipline", disciplineRegistry.getDiscipline());
	}

	private void removeDisciplineRegistry(final long disciplineRegId) throws RemoveException, ObjectNotFoundException {
		DisciplineRegistry disciplineRegistry = disciplineRegistryRepository.findById(disciplineRegId).get();
		final String coachId = disciplineRegistry.getCoach().getUsername();
		User coach = userRepository.findById(coachId).get();
		final String disciplineId = disciplineRegistry.getDiscipline().getId();
		Discipline discipline = disciplineRepository.findById(disciplineId).get();

		disciplineRepository.delete(discipline);
		userRepository.delete(coach);
		disciplineRegistryRepository.deleteById(disciplineRegId);
	}

	// Creates a discipline first, then an activity and return it
	private Discipline createNewDiscipline() throws CreateException, FinderException {
		// Finds an object with a unknown identifier
		final String id = counterService.getUniqueId("Discipline");
		final Discipline discipline = new Discipline("disc" + id, "name" + id, "description" + id);
		discipline.setDocuments("documents" + id);
		disciplineRepository.save(discipline);
		return discipline;
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
	private DisciplineRegistry createDisciplineRegistryForCoach(final User coach)
			throws CreateException, FinderException {

		// Create Event
		Discipline discipline = createNewDiscipline();

		final DisciplineRegistry disciplineReg = new DisciplineRegistry(discipline, coach);
		disciplineRegistryRepository.save(disciplineReg);
		return disciplineReg;
	}

	// Creates an event linked to an existing activity
	private DisciplineRegistry createDisciplineRegistryForDiscipline(final Discipline discipline)
			throws CreateException, FinderException {
		// Create User
		User coach = createNewCoach();
		final DisciplineRegistry disciplineRegistry = new DisciplineRegistry(discipline, coach);
		disciplineRegistryRepository.save(disciplineRegistry);
		return disciplineRegistry;
	}

	private void removeDiscipline(final String disciplineId) throws RemoveException, ObjectNotFoundException {
		Discipline discipline = disciplineRepository.findById(disciplineId).get();
		disciplineRepository.delete(discipline);
	}

	private void removeCoach(final User coach) throws RemoveException, ObjectNotFoundException {
		userRepository.delete(coach);
	}
}
