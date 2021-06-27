package com.sport.training.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.time.LocalDateTime;
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
import com.sport.training.domain.dao.DisciplineRepository;
import com.sport.training.domain.dao.DisciplineUserRepository;
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
public class UserServiceTest extends TestCase {
	
	@Autowired
	private CounterService counterService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DisciplineUserRepository disciplineUserRepository;
	
	private Random random = new Random();
	// ==================================
		// = Test cases for Discipline =
		// ==================================
		/**
		 * This test tries to find an object with a invalid identifier.
		 */
		@Test
		public void testServiceFindDisciplineUserWithInvalidValues() throws Exception {

			// Finds an object with a unknown identifier
			final Long id = disciplineUserRepository.findLastId().orElse(10000L) + 1;
			try {
				userService.findDisciplineUser(id);
				fail("Object with unknonw id should not be found");
			} catch (NoSuchElementException e) {
			}

			// Finds an object with an empty identifier
			try {
				userService.findDisciplineUser(random.nextLong() + 1L).getId();
				fail("Object with empty id should not be found");
			} catch (Exception e) {
			}

			// Finds an object with a null identifier
			try {
				userService.findDisciplineUser(null).getId();
				fail("Object with null id should not be found");
			} catch (Exception e) {
			}
		}

		/**
		 * This test ensures that the method findAll works. It does a first findAll,
		 * creates a new object and does a second findAll.
		 */
		@Test
		public void testServiceFindAllDisciplineUsers() throws Exception {
			// Finds an object with a unknown identifier
			final Long id = disciplineUserRepository.findLastId().orElse(10000L) + 1;

			// First findAll
			final int firstSize = findAllDisciplineUsers();
			// Create an object
			final long disciplineUserId = createDisciplineUser(id);
			
			// Ensures that the object exists
			try {
				findDisciplineUser(disciplineUserId);
			} catch (NoSuchElementException e) {
				fail("Object has been created it should be found");
			}

			// second findAll
			final int secondSize = findAllDisciplineUsers();

			// Checks that the collection size has increase of one
			if (firstSize + 1 != secondSize)
				fail("The collection size should have increased by 1");

			// Cleans the test environment
			deleteDisciplineUser(disciplineUserId);

			try {
				findDisciplineUser(disciplineUserId);
				fail("Object has been deleted it shouldn't be found");
			} catch (NoSuchElementException e) {
			}
		}

		/**
		 * This method ensures that creating an object works. It first finds the object,
		 * makes sure it doesn't exist, creates it and checks it then exists.
		 */
		@Test
		public void testServiceCreateDisciplineUser() throws Exception {
			// Finds an object with a unknown identifier
			final Long id = disciplineUserRepository.findLastId().orElse(10000L) + 1;
			DisciplineDTO disciplineDTO = null;

			// Ensures that the object doesn't exist
			try {
				findDisciplineUser(id);
				fail("Object has not been created yet it shouldn't be found");
			} catch (FinderException e) {
			}

			// Creates an object
			createDisciplineUser(id);

			// Ensures that the object exists
			try {
				disciplineDTO = findDisciplineUser(id);
			} catch (FinderException e) {
				fail("Object has been created it should be found");
			}

			// Checks that it's the right object
			checkDiscipline(disciplineDTO, id);

			// Creates an object with the same identifier. An exception has to be thrown
			try {
				createDisciplineUser(id);
				fail("An object with the same id has already been created");
			} catch (DuplicateKeyException e) {
			}

			// Cleans the test environment
			deleteDisciplineUser(id);

			try {
				findDisciplineUser(id);
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
				userService.createDiscipline(null);
				fail("Object with null parameter should not be created");
			} catch (CreateException e) {
			}

			// Creates an object with empty values
			try {
				disciplineDTO = new DisciplineDTO(new String(), new String(), new String());
				userService.createDiscipline(disciplineDTO);
				fail("Object with empty values should not be created");
			} catch (Exception e) {
				assertTrue("Il devrait s'agir d'une  CreateException", e instanceof CreateException);
			}

			// Creates an object with null values
			try {
				disciplineDTO = new DisciplineDTO(null, null, null);
				userService.createDiscipline(disciplineDTO);
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
			createDisciplineUser(id);

			// Ensures that the object exists
			DisciplineDTO disciplineDTO = null;
			try {
				disciplineDTO = findDisciplineUser(id);
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
				disciplineUpdated = findDisciplineUser(id);
			} catch (FinderException e) {
				fail("Object should be found");
			}

			// Checks that the object values have been updated
			checkDiscipline(disciplineUpdated, updatePattern);

			// Cleans the test environment
			deleteDisciplineUser(id);

			try {
				findDisciplineUser(id);
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
				userService.updateDiscipline(null);
				fail("Object with null parameter should not be updated");
			} catch (UpdateException e) {
			}

			// Updates an object with empty values
			try {
				disciplineDTO = new DisciplineDTO(new String(), new String(), new String());
				userService.updateDiscipline(disciplineDTO);
				fail("Object with empty values should not be updated");
			} catch (Exception e) {
				assertTrue("Il devrait s'agir d'une  UpdateException", e instanceof UpdateException);
			}

			// Updates an object with null values
			try {
				disciplineDTO = new DisciplineDTO(null, null, null);
				userService.updateDiscipline(disciplineDTO);
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
				findDisciplineUser(id);
				fail("Object has not been created it shouldn't be found");
			} catch (FinderException e) {
			}

			// Delete the unknown object
			try {
				deleteDisciplineUser(id);
				fail("Deleting an unknown object should break");
			} catch (RemoveException e) {
			}
		}
		
		// ==================================
		// = Private Methods =
		// ==================================

		// ==================================
		// = Private Methods for Discipline =
		// ==================================
		private DisciplineDTO findDisciplineUser(final String id) throws FinderException {
			final DisciplineDTO disciplineDTO = userService.findDiscipline(id);
			return disciplineDTO;
		}

		private int findAllDisciplineUsers() throws FinderException {
			try {
				return userService.findDisciplines().size();
			} catch (FinderException e) {
				return 0;
			}
		}

		private void createDisciplineUser(final String id) throws CreateException {
			final DisciplineDTO disciplineDTO = new DisciplineDTO(id, "name" + id, "description" + id);
			disciplineDTO.setDocuments("documents"+id);
			userService.createDiscipline(disciplineDTO);
		}

		private void updateDiscipline(final DisciplineDTO disciplineDTO, final String updatePattern) throws UpdateException {
			disciplineDTO.setName("name" + updatePattern);
			disciplineDTO.setDescription("description" + updatePattern);
			disciplineDTO.setDocuments("documents"+updatePattern);
			userService.updateDiscipline(disciplineDTO);
		}

		private void deleteDisciplineUser(final String id) throws RemoveException, FinderException {
			userService.deleteDiscipline(id);
		}

		private void checkDiscipline(final DisciplineDTO disciplineDTO, final String id) {
			assertEquals("name", "name" + id, disciplineDTO.getName());
			assertEquals("description", "description" + id, disciplineDTO.getDescription());
			assertEquals("documents", "documents" + id, disciplineDTO.getDocuments());
		}


	
	
}
