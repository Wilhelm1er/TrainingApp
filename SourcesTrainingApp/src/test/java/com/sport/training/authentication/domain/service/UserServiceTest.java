package com.sport.training.authentication.domain.service;

import java.rmi.RemoteException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.domain.service.CounterService;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.DuplicateKeyException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.ObjectNotFoundException;
import com.sport.training.exception.RemoveException;
import com.sport.training.exception.UpdateException;

import junit.framework.TestCase;

/**
 * This class tests the CatalogService class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest extends TestCase {

	private static final String COUNTER_NAME = "User";

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceTest.class);

	@Autowired
	private UserService userService;

	@Autowired
	private CounterService counterService;

	// ==================================
	// = Test cases =
	// ==================================
	/**
	 * This test tries to find an object with a invalid identifier.
	 */
	@Test
	public void testServiceFindUSerWithInvalidValues() throws Exception {

		// Finds an object with a unknown identifier
		final String id = counterService.getUniqueId(COUNTER_NAME);
		try {
			userService.findUser(id);
			fail("Object with unknonw id should not be found");
		} catch (FinderException e) {
		}

		// Finds an object with an empty identifier
		try {
			userService.findUser(new String());
			fail("Object with empty id should not be found");
		} catch (FinderException e) {
		}

		// Finds an object with a null identifier
		try {
			userService.findUser(null);
			fail("Object with null id should not be found");
		} catch (FinderException e) {
		}
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testServiceFindAllUsers() throws Exception {
		final String id = counterService.getUniqueId(COUNTER_NAME);

		// First findAll
		final int firstSize = findAllUsers();
		LOGGER.debug("firstSize is ... " + firstSize);
		// Creates an object
		createUser(id);

		// Ensures that the object exists
		try {
			findUser(id);
		} catch (FinderException e) {
			fail("Object has been created it should be found");
		}

		// Second findAll
		final int secondSize = findAllUsers();

		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Cleans the test environment
		deleteUser(id);

		try {
			findUser(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (FinderException e) {
		}
	}

	/**
	 * This method ensures that creating an object works. It first finds the object,
	 * makes sure it doesn't exist, creates it and checks it then exists.
	 */
	@Test
	public void testServiceCreateUser() throws Exception {
		final String id = counterService.getUniqueId(COUNTER_NAME);
		UserDTO userDTO = null;

		// Ensures that the object doesn't exist
		try {
			findUser(id);
			fail("Object has not been created yet it shouldn't be found");
		} catch (FinderException e) {
		}

		// Creates an object
		createUser(id);

		// Ensures that the object exists
		try {
			userDTO = findUser(id);
		} catch (FinderException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkUser(userDTO, id);

		// Creates an object with the same identifier. An exception has to be thrown
		try {
			createUser(id);
			fail("An object with the same id has already been created");
		} catch (DuplicateKeyException e) {
		}

		// Cleans the test environment
		deleteUser(id);

		try {
			findUser(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (FinderException e) {
		}
	}

	/**
	 * This test tries to create an object with a invalid values.
	 */
	@Test
	public void testServiceCreateUserWithInvalidValues() throws Exception {
		UserDTO userDTO;

		// Creates an object with a null parameter
		try {
			userService.createUser(null);
			fail("Object with null parameter should not be created");
		} catch (CreateException e) {
		}

		// Creates an object with empty values
		try {
			userDTO = new UserDTO(new String(), new String(), new String());
			userDTO.setPassword("validPwd");
			userService.createUser(userDTO);
			fail("Object with empty values should not be created");
		} catch (Exception e) {
			System.err.println(e.getClass().getSimpleName());
		}

		// Creates an object with null values
		try {
			userDTO = new UserDTO(null, null, null);
			userDTO.setPassword("validPwd");
			userService.createUser(userDTO);
			fail("Object with null values should not be created");
		} catch (Exception e) {
			System.err.println(e.getClass().getSimpleName());
		}

		// Creates an object with an invalid password
		try {
			userDTO = new UserDTO("username", "firstname", "lastname");
			userDTO.setPassword("abc");
			userService.createUser(userDTO);
			fail("Object with short password should not be created");
		} catch (CreateException e) {
		}
	}

	/**
	 * This test make sure that updating an object success
	 */
	@Test
	public void testServiceUpdateUser() throws Exception {
		final String id = counterService.getUniqueId(COUNTER_NAME);
		final String updatedId = counterService.getUniqueId(COUNTER_NAME);

		// Creates an object
		createUser(id);

		// Ensures that the object exists
		UserDTO userDTO = null;
		try {
			userDTO = findUser(id);
		} catch (ObjectNotFoundException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkUser(userDTO, id);

		// Updates the object with new values
		updateUser(userDTO, updatedId);

		// Ensures that the object still exists
		UserDTO userUpdated = null;
		try {
			userUpdated = findUser(id);
		} catch (ObjectNotFoundException e) {
			fail("Object should be found");
		}

		// Checks that the object values have been updated
		checkUser(userUpdated, updatedId);

		// Cleans the test environment
		deleteUser(id);

		try {
			findUser(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (FinderException e) {
		}
	}

	/**
	 * This test tries to update an object with a invalid values.
	 */
	@Test
	public void testServiceUpdateUserWithInvalidValues() throws Exception {
		UserDTO userDTO;

		// Updates an object with a null parameter
		try {
			userService.updateUser(null);
			fail("Object with null parameter should not be updated");
		} catch (UpdateException e) {
		}

		// Updates an object with empty values
		try {
			userDTO = new UserDTO(new String(), new String(), new String());
			userService.updateUser(userDTO);
			fail("Object with empty values should not be updated");
		} catch (Exception e) {
			System.err.println(e.getClass().getSimpleName());
		}

		// Updates an object with null values
		try {
			userDTO = new UserDTO(null, null, null);
			userService.updateUser(userDTO);
			fail("Object with null values should not be updated");
		} catch (Exception e) {
			System.err.println(e.getClass().getSimpleName());
		}
	}

	/**
	 * This test ensures that the system cannont remove an unknown object
	 */
	@Test
	public void testServiceDeleteUnknownUser() throws Exception {
		final String id = counterService.getUniqueId(COUNTER_NAME);

		// Ensures that the object doesn't exist
		try {
			findUser(id);
			fail("Object has not been created it shouldn't be found");
		} catch (FinderException e) {
		}

		// Delete the unknown object
		try {
			deleteUser(id);
			fail("Deleting an unknown object should break");
		} catch (RemoveException e) {
		}
	}

	// ==================================
	// = Private Methods =
	// ==================================

	private UserDTO findUser(final String id) throws FinderException, RemoteException {
		UserDTO userDTO = userService.findUser("user" + id);
		return userDTO;
	}

	private int findAllUsers() throws FinderException, RemoteException {
		try {
			return (userService.findUsers()).size();
		} catch (FinderException e) {
			LOGGER.warn("exception is ... " + e.getMessage());
			return 0;
		}
	}

	private UserDTO createUserDTO(final String id) throws CreateException, RemoteException, FinderException {
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername("user" + id);
		userDTO.setFirstname("firstname" + id);
		userDTO.setLastname("lastname" + id);
		userDTO.setPassword("pwd" + id);
		userDTO.setEmail("email" + id);
		userDTO.setTelephone("phone" + id);
		userDTO.setAddress1("street1" + id);
		userDTO.setAddress2("street2" + id);
		userDTO.setCity("city" + id);
		userDTO.setState("" + id);
		userDTO.setZipcode("zip" + id);
		userDTO.setCountry("" + id);
		userDTO.setRoleName("ROLE_ATHLETE");
		return userDTO;
	}

	private void createUser(final String id) throws CreateException, RemoteException, FinderException {
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername("user" + id);
		userDTO.setFirstname("firstname" + id);
		userDTO.setLastname("lastname" + id);
		userDTO.setPassword("pwd" + id);
		userDTO.setEmail("email" + id);
		userDTO.setTelephone("phone" + id);
		userDTO.setAddress1("street1" + id);
		userDTO.setAddress2("street2" + id);
		userDTO.setCity("city" + id);
		userDTO.setState("" + id);
		userDTO.setZipcode("zip" + id);
		userDTO.setCountry("" + id);
		userDTO.setRoleName("ROLE_ATHLETE");
		userService.createUser(userDTO);
	}

	private void updateUser(final UserDTO userDTO, final String id)
			throws UpdateException, RemoteException, FinderException {
		userDTO.setFirstname("firstname" + id);
		userDTO.setLastname("lastname" + id);
		userDTO.setCity("city" + id);
		userDTO.setCountry("" + id);
		userDTO.setState("" + id);
		userDTO.setAddress1("street1" + id);
		userDTO.setAddress2("street2" + id);
		userDTO.setTelephone("phone" + id);
		userDTO.setEmail("email" + id);
		userDTO.setZipcode("zip" + id);
		userService.updateUser(userDTO);
	}

	private void deleteUser(final String id) throws RemoveException, RemoteException, FinderException {
		userService.deleteUser("user" + id);
	}

	private void checkUser(final UserDTO userDTO, final String id) {
		assertEquals("firstname", "firstname" + id, userDTO.getFirstname());
		assertEquals("lastname", "lastname" + id, userDTO.getLastname());
		assertEquals("city", "city" + id, userDTO.getCity());
		assertEquals("country", "" + id, userDTO.getCountry());
		assertEquals("state", "" + id, userDTO.getState());
		assertEquals("street1", "street1" + id, userDTO.getAddress1());
		assertEquals("street2", "street2" + id, userDTO.getAddress2());
		assertEquals("telephone", "phone" + id, userDTO.getTelephone());
		assertEquals("email", "email" + id, userDTO.getEmail());
		assertEquals("zipcode", "zip" + id, userDTO.getZipcode());
	}

}