package com.sport.training.domain.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Random;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sport.training.domain.model.Notation;
import com.sport.training.domain.model.Discipline;
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
@Transactional
public class NotationDAOTest {

	private static final String COUNTER_NAME = "notation";
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private NotationRepository notationRepository;
	@Autowired
	private CounterService counterService;

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
	public void testDomainFindAllNotationsForADiscipline() throws Exception {
		Discipline newdiscipline = createNewDiscipline();
		final String disciplineId = newdiscipline.getId();

		// First findAll
		final int firstSize = findAllNotations(disciplineId);

		// Checks that the collection is empty
		if (firstSize != 0)
			fail("The collection should be empty");

		// Create an object
		Notation notation = createNotationForDiscipline(newdiscipline);

		// Ensures that the object exists
		try {
			findNotation(notation.getId());
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// second findAll
		final int secondSize = findAllNotations(disciplineId);

		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Cleans the test environment
		removeNotation(notation.getId());

	}

	/**
	 * This method ensures that creating an object works. It first finds the object,
	 * makes sure it doesn't exist, creates it and checks it then exists.
	 */
	@Test
	public void testDomainCreateNotation() throws Exception {
		final String id = counterService.getUniqueId(COUNTER_NAME);
		Notation notation = null;

		// Ensures that the object doesn't exist
		try {
			notation = findNotation(id);
			fail("Object has not been created yet it shouldn't be found");
		} catch (NoSuchElementException e) {
		}

		// Creates an object
		createNotation(id);

		// Ensures that the object exists
		try {
			notation = findNotation(id);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkNotation(notation, id);

		// Cleans the test environment
		removeNotation(id);

		try {
			findNotation(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test make sure that updating an object success
	 */
	@Test
	public void testDomainUpdateNotation() throws Exception {
		final String id = counterService.getUniqueId(COUNTER_NAME);

		// Creates an object
		createNotation(id);

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
			notationUpdated = findNotation(id);
		} catch (NoSuchElementException e) {
			fail("Object should be found");
		}

		// Checks that the object values have been updated
		checkNotation(notationUpdated, id + 1);

		// Cleans the test environment
		removeNotation(id);

		try {
			findNotation(id);
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
	private Notation findNotation(final String id) throws NoSuchElementException {
		final Notation notation = notationRepository.findById(id).get();
		return notation;
	}

	private int findAllNotations() throws FinderException {
		try {
			return ((Collection<Notation>) notationRepository.findAll()).size();
		} catch (Exception e) {
			return 0;
		}
	}

	private int findAllNotations(String disciplineId) throws FinderException {
		try {
			Discipline discipline = disciplineRepository.findById(disciplineId).get();
			return ((Collection<Notation>) notationRepository.findAllByDiscipline(discipline)).size();
		} catch (Exception e) {
			return 0;
		}
	}

	// Creates a discipline first and then a notation linked to this discipline
	private void createNotation(final String id) throws CreateException, ObjectNotFoundException {
		// Create discipline
		final String newdisciplineId = counterService.getUniqueId("discipline");
		final Discipline discipline = new Discipline(newdisciplineId, "name" + newdisciplineId,
				"description" + newdisciplineId);
		discipline.setDocuments("documents" + newdisciplineId);
		disciplineRepository.save(discipline);
		// Create notation
		final Notation notation = new Notation(id, "name" + id, "description" + id, discipline);
		notation.setCreditcostMax(4);
		notation.setCreditcostMin(1);
		try {
			notationRepository.save(notation);
		} catch (Exception e) {
			// remove the added discipline object
			disciplineRepository.deleteById(newdisciplineId);
			// rethrow the exception
			throw e;
		}
	}

	// Creates a discipline and updates the notation with this new discipline
	private void updateNotation(final Notation notation, final String id)
			throws UpdateException, CreateException, ObjectNotFoundException {
		// Create discipline
		final String newdisciplineId = counterService.getUniqueId("discipline");
		final Discipline discipline = new Discipline(newdisciplineId, "name" + newdisciplineId,
				"description" + newdisciplineId);
		discipline.setDocuments("documents" + newdisciplineId);
		disciplineRepository.save(discipline);

		// get old discipline
		Discipline disc = notation.getDiscipline();

		// Update notation with new discipline
		notation.setName("name" + id);
		notation.setDescription("description" + id);
		notation.setDiscipline(discipline);
		notation.setCreditcostMax(4);
		notation.setCreditcostMin(1);
		notationRepository.save(notation);

		// delete old discipline
		disciplineRepository.delete(disc);
	}

	private void removeNotation(final String id) throws RemoveException, ObjectNotFoundException {
		final String notationId = id;
		Notation notation = notationRepository.findById(notationId).get();
		final String disciplineId = notation.getDiscipline().getId();
		notationRepository.deleteById(id);
		disciplineRepository.deleteById(disciplineId);
	}

	private void checkNotation(final Notation notation, final String id) {
		assertEquals("name", "name" + id, notation.getName());
		assertEquals("description", "description" + id, notation.getDescription());
		assertNotNull("discipline", notation.getDiscipline());
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

	// Creates an notation linked to an existing discipline
	private Notation createNotationForDiscipline(final Discipline discipline) throws CreateException {
		final String id = counterService.getUniqueId(COUNTER_NAME);
		final Notation notation = new Notation(id, "name" + id, "description" + id, discipline);
		notation.setCreditcostMax(4);
		notation.setCreditcostMin(1);
		notationRepository.save(notation);
		return notation;
	}

}
