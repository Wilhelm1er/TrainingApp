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

import com.sport.training.domain.model.Bookmark;
import com.sport.training.authentication.domain.dao.UserRepository;
import com.sport.training.authentication.domain.model.User;
import com.sport.training.authentication.domain.service.RoleService;
import com.sport.training.domain.service.CounterService;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.ObjectNotFoundException;
import com.sport.training.exception.RemoveException;
import com.sport.training.exception.UpdateException;

/**
 * This class tests the bookmarkDAO class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
//@Transactional
public class BookmarkDAOTest {

	private static Logger logger = LogManager.getLogger(BookmarkDAOTest.class);

	@Autowired
	private BookmarkRepository bookmarkRepository;
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
	public void testDomainFindBookmarkWithInvalidValues() throws Exception {

		// Finds an object with a unknown identifier
		final Long id = bookmarkRepository.findLastId().orElse(10000L) + 1;
		try {
			findBookmark(id);
			fail("Object with unknonw id should not be found");
		} catch (NoSuchElementException e) {
		}

		// Finds an object with an empty identifier
		try {
			bookmarkRepository.findById(random.nextLong() + 1L).get();
			fail("Object with empty id should not be found");
		} catch (Exception e) {
		}

		// Finds an object with a null identifier
		try {
			bookmarkRepository.findById(null).get();
			fail("Object with null id should not be found");
		} catch (Exception e) {
		}
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testDomainFindAllBookmarks() throws Exception {
		// First findAll
		final int firstSize = findAllBookmarks();

		// Create an object
		final long bookmarkId = createBookmark();

		// Ensures that the object exists
		try {
			findBookmark(bookmarkId);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// second findAll
		final int secondSize = findAllBookmarks();

		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Cleans the test environment
		removeBookmark(bookmarkId);

		try {
			findBookmark(bookmarkId);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This method ensures that creating an object works. It first finds the object,
	 * makes sure it doesn't exist, creates it and checks it then exists.
	 */
	@Test
	public void testDomainCreateBookmark() throws Exception {
		Bookmark bookmark = null;

		// Creates an object
		final long bookmarkId = createBookmark();

		// Ensures that the object exists
		try {
			bookmark = findBookmark(bookmarkId);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkBookmark(bookmark);

		// Cleans the test environment
		removeBookmark(bookmarkId);

		try {
			findBookmark(bookmarkId);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testDomainFindAllBookmarksForAnAthlete() throws Exception {
		User newAthlete = createNewAthlete();
		final String athleteId = newAthlete.getUsername();

		// First findAll
		final int firstSize = findAllBookmarks(athleteId);

		// Checks that the collection is empty
		if (firstSize != 0)
			fail("The collection should be empty");

		// Create an object
		Bookmark bookmark1 = createBookmarkForAthlete(newAthlete);

		// Ensures that the object exists
		try {
			findBookmark(bookmark1.getId());
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// second findAll
		final int secondSize = findAllBookmarks(athleteId);

		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Create an object
		Bookmark bookmark2 = createBookmarkForAthlete(newAthlete);

		// Ensures that the new object exists
		try {
			findBookmark(bookmark2.getId());
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// third findAll
		final int thirdSize = findAllBookmarks(athleteId);

		// Checks that the collection size has increase of one
		if (thirdSize != secondSize + 1)
			fail("The collection should have the same size");

		// Cleans the test environment
		bookmarkRepository.delete(bookmark1);
		bookmarkRepository.delete(bookmark2);

		// Cleans the test environment
		removeUser(newAthlete);

	}

	/**
	 * This test make sure that updating an object success
	 */
	@Test
	public void testDomainUpdateBookmark() throws Exception {
		// Creates an object
		final long bookmarkId = createBookmark();

		// Ensures that the object exists
		Bookmark bookmark = null;
		try {
			bookmark = findBookmark(bookmarkId);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkBookmark(bookmark);

		// Updates the object with new values
		updateBookmark(bookmark);

		// Ensures that the object still exists
		Bookmark bookmarkUpdated = null;
		try {
			bookmarkUpdated = findBookmark(bookmarkId);
		} catch (NoSuchElementException e) {
			fail("Object should be found");
		}

		// Checks that the object values have been updated
		checkBookmark(bookmarkUpdated);

		// Cleans the test environment
		removeBookmark(bookmarkId);

		try {
			findBookmark(bookmarkId);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test ensures that the system cannont remove an unknown object
	 */
	@Test
	public void testDomainDeleteUnknownBookmark() throws Exception {
		// Removes an unknown object
		try {
			bookmarkRepository.delete(null);
			fail("Deleting an unknown object should break");
		} catch (Exception e) {
		}
	}

	// ==================================
	// = Private Methods =
	// ==================================
	private Bookmark findBookmark(final Long id) throws NoSuchElementException {
		final Bookmark bookmark = bookmarkRepository.findById(id).get();
		return bookmark;
	}

	private int findAllBookmarks() throws FinderException {
		try {
			return ((Collection<Bookmark>) bookmarkRepository.findAll()).size();
		} catch (Exception e) {
			logger.info("Exception is ... " + e.getMessage());
			return 0;
		}
	}

	private int findAllBookmarks(String athleteId) throws FinderException {
		try {
			User athlete = userRepository.findById(athleteId).get();
			return ((Collection<Bookmark>) bookmarkRepository.findAllByAthlete(athlete)).size();
		} catch (Exception e) {
			return 0;
		}
	}

	// Creates a discipline first and then a bookmark linked to this discipline
	private long createBookmark() throws CreateException, FinderException {
		// Create user
		final String newAthleteId = counterService.getUniqueId("Athlete");
		final User athlete = new User(newAthleteId, "firstname" + newAthleteId, "lastname" + newAthleteId);
		athlete.setPassword("pwd" + newAthleteId);
		athlete.setRole(roleService.findByRoleName("ROLE_ATHLETE"));
		athlete.setStatut("VALIDE");
		userRepository.save(athlete);
		// Create user
		final String newCoachId = counterService.getUniqueId("Coach");
		final User coach = new User(newCoachId, "firstname" + newCoachId, "lastname" + newCoachId);
		coach.setPassword("pwd" + newCoachId);
		coach.setRole(roleService.findByRoleName("ROLE_COACH"));
		coach.setStatut("VALIDE");
		userRepository.save(coach);
		// Create bookmark
		final Bookmark bookmark = new Bookmark(athlete, coach);

		bookmarkRepository.save(bookmark);
		return bookmark.getId();
	}

	// Creates a discipline and updates the bookmark with this new discipline
	private void updateBookmark(final Bookmark bookmark) throws UpdateException, CreateException, FinderException {
		// get old athlete
		User ath = bookmark.getAthlete();
		// Create new athlete
		final String athleteId = counterService.getUniqueId("athlete");
		final User athlete = new User(athleteId, "firstname" + athleteId, "lastname" + athleteId);
		athlete.setPassword("pwd" + athleteId);
		athlete.setRole(roleService.findByRoleName("ROLE_ATHLETE"));
		athlete.setStatut("VALIDE");
		userRepository.save(athlete);

		// get old coach
		User coa = bookmark.getCoach();

		// Create new coach
		final String coachId = counterService.getUniqueId("coach");
		final User coach = new User(coachId, "firstname" + coachId, "lastname" + coachId);
		coach.setPassword("pwd" + coachId);
		coach.setRole(roleService.findByRoleName("ROLE_COACH"));
		coach.setStatut("VALIDE");
		userRepository.save(coach);

		// Update bookmark with new discipline
		bookmark.setAthlete(athlete);
		bookmark.setCoach(coach);
		bookmarkRepository.save(bookmark);

		// delete old athlete
		userRepository.delete(ath);
		// delete old coach
		userRepository.delete(coa);
	}

	private void removeBookmark(final long bookmarkId) throws RemoveException, ObjectNotFoundException {
		Bookmark bookmark = bookmarkRepository.findById(bookmarkId).get();
		final User athlete = bookmark.getAthlete();
		final User coach = bookmark.getCoach();
		bookmarkRepository.deleteById(bookmarkId);
		userRepository.delete(athlete);
		userRepository.delete(coach);
	}

	private void checkBookmark(final Bookmark bookmark) {
		assertNotNull("athlete", bookmark.getAthlete());
		assertNotNull("coach", bookmark.getCoach());
	}

	// Creates a new discipline and return it
	private User createNewAthlete() throws CreateException, FinderException {
		final String athleteId = counterService.getUniqueId("athlete");
		final User athlete = new User(athleteId, "firstname" + athleteId, "lastname" + athleteId);
		athlete.setPassword("pwd" + athleteId);
		athlete.setRole(roleService.findByRoleName("ROLE_ATHLETE"));
		athlete.setStatut("VALIDE");
		userRepository.save(athlete);
		return athlete;
	}

	// Creates a new discipline and return it
	private User createNewCoach() throws CreateException, FinderException {
		final String coachId = counterService.getUniqueId("athlete");
		final User coach = new User(coachId, "firstname" + coachId, "lastname" + coachId);
		coach.setPassword("pwd" + coachId);
		coach.setRole(roleService.findByRoleName("ROLE_COACH"));
		coach.setStatut("VALIDE");
		userRepository.save(coach);
		return coach;
	}

	// Creates an bookmark linked to an existing user
	private Bookmark createBookmarkForAthlete(final User athlete) throws CreateException, FinderException {
		User coach = createNewCoach();
		final Bookmark bookmark = new Bookmark(athlete, coach);
		bookmarkRepository.save(bookmark);
		return bookmark;
	}

	private void removeUser(final User user) throws RemoveException, ObjectNotFoundException {
		userRepository.delete(user);
	}
}
