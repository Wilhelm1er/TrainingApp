package com.sport.training.domain.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

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

import com.sport.training.domain.model.Discussion;
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
 * This class tests the discussionDAO class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class DiscussionDAOTest {
	
	private static Logger logger = LogManager.getLogger(DiscussionDAOTest.class);

	@Autowired
	private DiscussionRepository discussionRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	CounterService counterService;
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
	public void testDomainFindDiscussionWithInvalidValues() throws Exception {

		// Finds an object with a unknown identifier
		final Long id = discussionRepository.findLastId().orElse(10000L) + 1;
		try {
			findDiscussion(id);
			fail("Object with unknonw id should not be found");
		} catch (NoSuchElementException e) {
		}

		// Finds an object with an empty identifier
		try {
			discussionRepository.findById(random.nextLong() + 1L).get();
			fail("Object with empty id should not be found");
		} catch (Exception e) {
		}

		// Finds an object with a null identifier
		try {
			discussionRepository.findById(null).get();
			fail("Object with null id should not be found");
		} catch (Exception e) {
		}
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testDomainFindAllDiscussions() throws Exception {
		final Long id = discussionRepository.findLastId().orElse(10000L) + 1;

		// First findAll
		final int firstSize = findAllDiscussions();

		// Create an object
		final long discussionId = createDiscussion(id);

		// Ensures that the object exists
		try {
			findDiscussion(discussionId);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// second findAll
		final int secondSize = findAllDiscussions();
		
		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Cleans the test environment
		removeDiscussion(discussionId);

		try {
			findDiscussion(discussionId);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testDomainFindAllDiscussionsForAUser() throws Exception {
		User newuser = createNewUser();
		final String userId = newuser.getUsername();

		// First findAll
		final int firstSize = findAllDiscussions(userId);

		// Checks that the collection is empty
		if (firstSize != 0)
			fail("The collection should be empty");

		// Create an object
		Discussion discussion1 = createDiscussionForUser(newuser);

		// Ensures that the object exists
		try {
			findDiscussion(discussion1.getId());
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// second findAll
		final int secondSize = findAllDiscussions(userId);

		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Create an object
		Discussion discussion2 = createDiscussionForUser(newuser);
				
		// Ensures that the new object exists
				try {
					findDiscussion(discussion2.getId());
				} catch (NoSuchElementException e) {
					fail("Object has been created it should be found");
				}

				// third findAll
				final int thirdSize = findAllDiscussions(userId);

				// Checks that the collection size has increase of one
				if (thirdSize != secondSize + 1)
					fail("The collection should have the same size");

				// Cleans the test environment
				discussionRepository.delete(discussion1);
				discussionRepository.delete(discussion2);
				
		// Cleans the test environment
		removeUser(newuser);

	}

	/**
	 * This method ensures that creating an object works. It first finds the object,
	 * makes sure it doesn't exist, creates it and checks it then exists.
	 */
	@Test
	public void testDomainCreateDiscussion() throws Exception {
		final Long id = discussionRepository.findLastId().orElse(10000L) + 1;
		Discussion discussion = null;

		// Ensures that the object doesn't exist
		try {
			discussion = findDiscussion(id);
			fail("Object has not been created yet it shouldn't be found");
		} catch (NoSuchElementException e) {
		}

		// Creates an object
		createDiscussion(id);

		// Ensures that the object exists
		try {
			discussion = findDiscussion(id);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkDiscussion(discussion, id);

		// Cleans the test environment
		removeDiscussion(id);

		try {
			findDiscussion(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test make sure that updating an object success
	 */
	@Test
	public void testDomainUpdateDiscussion() throws Exception {
		final Long id = discussionRepository.findLastId().orElse(10000L) + 1;

		// Creates an object
		createDiscussion(id);

		// Ensures that the object exists
		Discussion discussion = null;
		try {
			discussion = findDiscussion(id);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkDiscussion(discussion, id);

		// Updates the object with new values
		updateDiscussion(discussion, id + 1);

		// Ensures that the object still exists
		Discussion discussionUpdated = null;
		try {
			discussionUpdated = findDiscussion(id);
		} catch (NoSuchElementException e) {
			fail("Object should be found");
		}

		// Checks that the object values have been updated
		checkDiscussion(discussionUpdated, id + 1);

		// Cleans the test environment
		removeDiscussion(id);

		try {
			findDiscussion(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test ensures that the system cannont remove an unknown object
	 */
	@Test
	public void testDomainDeleteUnknownDiscussion() throws Exception {
		// Removes an unknown object
		try {
			discussionRepository.delete(null);
			fail("Deleting an unknown object should break");
		} catch (Exception e) {
		}
	}

	// ==================================
	// = Private Methods =
	// ==================================
	private Discussion findDiscussion(final Long id) throws NoSuchElementException {
		final Discussion discussion = discussionRepository.findById(id).get();
		return discussion;
	}

	private int findAllDiscussions() throws FinderException {
		try {
			return ((Collection<Discussion>) discussionRepository.findAll()).size();
		} catch (Exception e) {
			logger.info("Exception is ... " + e.getMessage());
			return 0;
		}
	}

	private int findAllDiscussions(String userId) throws FinderException {
		try {
			User user = userRepository.findById(userId).get();
			return ((Collection<Discussion>) discussionRepository.findAllByUser(user)).size();
		} catch (Exception e) {
			return 0;
		}
	}

	// Creates a discipline first and then a discussion linked to this discipline
	private long createDiscussion(final long id) throws CreateException, ObjectNotFoundException {
		// Create user
		final String newuserId = counterService.getUniqueId("user");
		final User user = new User(newuserId, "firstname" + newuserId,
				"lastname" + newuserId);
		// Create discussion
		final Discussion discussion = new Discussion(user, "subject" + id);
		
			discussionRepository.save(discussion);
			return discussion.getId();
	}

	// Creates a discipline and updates the discussion with this new discipline
	private void updateDiscussion(final Discussion discussion, final long id)
			throws UpdateException, CreateException, FinderException {
		// get old customer
		User usr = discussion.getUser();
		
		// Create new user
		final String userId = counterService.getUniqueId("user");
		final User user = new User(userId, "firstname" + userId,
				"lastname" + userId);
		user.setPassword("pwd" + userId);
		user.setRole(roleService.findByRoleName("ROLE_COACH"));
		userRepository.save(user);

		// Update discussion with new discipline
		discussion.setUser(user);
		discussion.setSubject("newSubject" +userId);
		discussionRepository.save(discussion);

		// delete old user
		userRepository.delete(usr);
	}

	private void removeDiscussion(final long discussionId) throws RemoveException, ObjectNotFoundException {
		Discussion discussion = discussionRepository.findById(discussionId).get();
		final User user = discussion.getUser();
		discussionRepository.deleteById(discussionId);
		userRepository.delete(user);
	}

	private void checkDiscussion(final Discussion discussion, final Long id) {
		assertEquals("subject", "subject" + id, discussion.getSubject());
		assertNotNull("user", discussion.getUser());
	}

	// Creates a new discipline and return it
	private User createNewUser() throws CreateException, FinderException {
				final String userId = counterService.getUniqueId("user");
				final User user = new User(userId, "firstname" + userId,
						"lastname" + userId);
				user.setPassword("pwd" + userId);
				user.setRole(roleService.findByRoleName("ROLE_COACH"));
				userRepository.save(user);
		return user;
	}

	// Creates an discussion linked to an existing user
	private Discussion createDiscussionForUser(final User user) throws CreateException {
		final Long id = discussionRepository.findLastId().orElse(10000L) + 1;
		final Discussion discussion = new Discussion(user, "subject" + id);
		discussionRepository.save(discussion);
		return discussion;
	}
	private void removeUser(final User user) throws RemoveException, ObjectNotFoundException {
		userRepository.delete(user);
	}

}
