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
public class CoachServiceTest extends TestCase {
	
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
			disciplineDTO.setDocuments("documents"+id);
			sportService.createDiscipline(disciplineDTO);
		}

		private void updateDiscipline(final DisciplineDTO disciplineDTO, final String updatePattern) throws UpdateException {
			disciplineDTO.setName("name" + updatePattern);
			disciplineDTO.setDescription("description" + updatePattern);
			disciplineDTO.setDocuments("documents"+updatePattern);
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


	
}