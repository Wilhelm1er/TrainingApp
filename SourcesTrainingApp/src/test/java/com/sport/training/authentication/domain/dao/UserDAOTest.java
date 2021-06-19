package com.sport.training.authentication.domain.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.service.CounterService;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.DuplicateKeyException;
import com.sport.training.exception.FinderException;

/**
 * This class tests the CustomerDAO class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserDAOTest {

	private static final String COUNTER_NAME = "User";

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CounterService counterService;

	// ==================================
	// = Test cases =
	// ==================================
	/**
	 * This test tries to find an object with a invalid identifier.
	 */
	@Test
	public void testDomainFindUserWithInvalidValues() throws Exception {

		// Finds an object with a unknown identifier
		final String id = counterService.getUniqueId(COUNTER_NAME);
		try {
			findUser(id);
			fail("Object with unknonw id should not be found");
		} catch (NoSuchElementException e) {
		}

		// Finds an object with a null identifier
		try {
			userRepository.findById(null).get();
			fail("Object with null id should not be found");
		} catch (Exception e) {
		}
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testDomainFindAllUsers() throws Exception {
		final String id = counterService.getUniqueId(COUNTER_NAME);

		// First findAll
		final int firstSize = findAllUsers();

		// Create an object
		createUser(id);

		// Ensures that the object exists
		try {
			findUser(id);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// Second findAll
		final int secondSize = findAllUsers();
		
		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			
			fail("The collection size should have increased by 1");

		// Cleans the test environment
		removeUser(id);

		try {
			findUser(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This method ensures that creating an object works. It first finds the object,
	 * makes sure it doesn't exist, creates it and checks it then exists.
	 */
	@Test
	public void testDomainCreateUser() throws Exception {
		final String id = counterService.getUniqueId(COUNTER_NAME);
		User user = null;

		// Ensures that the object doesn't exist
		try {
			user = findUser(id);
			fail("Object has not been created yet it shouldn't be found");
		} catch (NoSuchElementException e) {
		}

		// Creates an object
		createUser(id);
		// Ensures that the object exists
		try {
			user = findUser(id);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkUser(user, id);

		// Cleans the test environment
		removeUser(id);

		try {
			findUser(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}

	}

	/**
	 * This test make sure that updating an object success
	 */
	@Test
	public void testDomainUpdateUser() throws Exception {
		final String id = counterService.getUniqueId(COUNTER_NAME);

		// Creates an object
		createUser(id);

		// Ensures that the object exists
		User user = null;
		try {
			user = findUser(id);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkUser(user, id);

		// Updates the object with new values
		updateUser(user, id + 1);

		// Ensures that the object still exists
		User userUpdated = null;
		try {
			userUpdated = findUser(id);
		} catch (NoSuchElementException e) {
			fail("Object should be found");
		}

		// Checks that the object values have been updated
		checkUser(userUpdated, id + 1);

		// Cleans the test environment
		removeUser(id);

		try {
			findUser(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test ensures that the system cannot remove an unknown object
	 */
	@Test
	public void testDomainDeleteUnknownUser() throws Exception {
		// Removes an unknown object
		try {
			removeUser(counterService.getUniqueId(COUNTER_NAME));
			fail("Deleting an unknown object should break");
		} catch (EmptyResultDataAccessException e) {
		}
	}

	// ==================================
	// = Private Methods =
	// ==================================
	private User findUser(final String id) throws NoSuchElementException {
		final User user = userRepository.findById("user" + id).get();
		return user;
	}

	private int findAllUsers() throws FinderException {
		try {
			return ((Collection<User>) userRepository.findAll()).size();
		} catch (Exception e) {
			System.out.println(" exception= "+ e.getMessage());
			return 0;
		}
	}

	private void createUser(final String id) throws CreateException {
		final User user = new User("user" + id, "firstname" + id, "lastname" + id);
		user.setCity("city" + id);
		user.setCountry("cnty" + id);
		user.setState("state" + id);
		user.setAddress1("address1" + id);
		user.setAddress2("address2" + id);
		user.setTelephone("phone" + id);
		user.setEmail("email" + id);
		user.setPassword("pwd" + id);
		user.setZipcode("zip" + id);
		user.setStatut("VALIDE");
		userRepository.save(user);
	}

	private void updateUser(final User user, final String id) throws DuplicateKeyException {
		user.setFirstname("firstname" + id);
		user.setLastname("lastname" + id);
		user.setCity("city" + id);
		user.setCountry("cnty" + id);
		user.setState("state" + id);
		user.setAddress1("address1" + id);
		user.setAddress2("address2" + id);
		user.setTelephone("phone" + id);
		user.setEmail("email" + id);
		user.setZipcode("zip" + id);
		user.setStatut("VALIDE");
		userRepository.save(user);
	}

	private void removeUser(final String id) throws EmptyResultDataAccessException {
		final String sid = "user" + id;
		userRepository.deleteById(sid);
	}

	private void checkUser(final User user, final String id) {
		assertEquals("firstname", "firstname" + id, user.getFirstname());
		assertEquals("lastname", "lastname" + id, user.getLastname());
		assertEquals("city", "city" + id, user.getCity());
		assertEquals("country", "cnty" + id, user.getCountry());
		assertEquals("state", "state" + id, user.getState());
		assertEquals("address1", "address1" + id, user.getAddress1());
		assertEquals("address2", "address2" + id, user.getAddress2());
		assertEquals("telephone", "phone" + id, user.getTelephone());
		assertEquals("email", "email" + id, user.getEmail());
		assertEquals("zipcode", "zip" + id, user.getZipcode());
	}

}
