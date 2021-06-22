package com.sport.training.domain.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sport.training.domain.model.Discipline;
import com.sport.training.domain.service.CounterService;
import com.sport.training.exception.*;

/**
 * This class tests the CategoryDAO class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class DisciplineDAOTest {

	private static final String COUNTER_NAME = "Category";

	@Autowired
	private DisciplineRepository disciplineRepository;

	@Autowired
	CounterService counterService;

	// ==================================
	// = Test cases =
	// ==================================
	/**
	 * This test tries to find an object with a invalid identifier.
	 */
	@Test
	public void testDomainFindDisciplineWithInvalidValues() throws Exception {

		// Finds an object with a unknown identifier
		final String id = counterService.getUniqueId(COUNTER_NAME);
		try {
			findDiscipline(id);
			fail("Object with unknown id should not be found");
		} catch (NoSuchElementException e) {
		}

		// Finds an object with an empty identifier
		try {
			disciplineRepository.findById(new String()).get();
			fail("Object with empty id should not be found");
		} catch (NoSuchElementException e) {
		}

	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testDomainFindAllDisciplines() throws Exception {
		final String id = counterService.getUniqueId(COUNTER_NAME);

		// First findAll
		final int firstSize = findAllDisciplines();

		// Creates an object
		createDiscipline(id);

		// Ensures that the object exists
		try {
			findDiscipline(id);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// Second findAll
		final int secondSize = findAllDisciplines();

		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Cleans the test environment
		removeDiscipline(id);

		try {
			findDiscipline(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This method ensures that creating an object works. It first finds the object,
	 * makes sure it doesn't exist, creates it and checks it then exists.
	 */
	@Test
	public void testDomainCreateDiscipline() throws Exception {
		final String id = counterService.getUniqueId(COUNTER_NAME);
		Discipline discipline = null;

		// Ensures that the object doesn't exist
		try {
			discipline = findDiscipline(id);
			System.out.println("discipline: " + discipline);
			fail("Object has not been created yet it shouldn't be found");
		} catch (NoSuchElementException e) {
		}

		// Creates an object
		createDiscipline(id);

		// Ensures that the object exists
		try {
			discipline = findDiscipline(id);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkDiscipline(discipline, id);

		// Cleans the test environment
		removeDiscipline(id);

		try {
			findDiscipline(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test make sure that updating an object success
	 */
	@Test
	public void testDomainUpdateDiscipline() throws Exception {
		final String id = counterService.getUniqueId(COUNTER_NAME);

		// Creates an object
		createDiscipline(id);

		// Ensures that the object exists
		Discipline discipline = null;
		try {
			discipline = findDiscipline(id);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkDiscipline(discipline, id);

		// Updates the object with new values
		updateDiscipline(discipline, id + 1);

		// Ensures that the object still exists
		Discipline disciplineUpdated = null;
		try {
			disciplineUpdated = findDiscipline(id);
		} catch (NoSuchElementException e) {
			fail("Object should be found");
		}

		// Checks that the object values have been updated
		checkDiscipline(disciplineUpdated, id + 1);

		// Cleans the test environment
		removeDiscipline(id);

		try {
			findDiscipline(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	// ==================================
	// = Private Methods =
	// ==================================
	private Discipline findDiscipline(final String id) throws NoSuchElementException {
		final Discipline discipline = disciplineRepository.findById(id).get();
		return discipline;
	}

	private int findAllDisciplines() throws FinderException {
		try {
			return ((Collection<Discipline>) disciplineRepository.findAll()).size();
		} catch (Exception e) {
			return 0;
		}
	}

	private void createDiscipline(final String id) throws CreateException {
		final Discipline discipline = new Discipline(id, "name" + id, "description" + id);
		discipline.setDocuments("documents" + id);
		disciplineRepository.save(discipline);
	}

	private void updateDiscipline(final Discipline discipline, final String id)
			throws UpdateException, ObjectNotFoundException {
		discipline.setName("name" + id);
		discipline.setDescription("description" + id);
		discipline.setDocuments("documents" + id);
		disciplineRepository.save(discipline);
	}

	private void removeDiscipline(final String id) throws ObjectNotFoundException, RemoveException {
		final Discipline discipline = new Discipline(id);
		disciplineRepository.delete(discipline);
	}

	private void checkDiscipline(final Discipline discipline, final String id) {
		assertEquals("name", "name" + id, discipline.getName());
		assertEquals("description", "description" + id, discipline.getDescription());
		assertEquals("documents", "documents" + id, discipline.getDocuments());
	}

}