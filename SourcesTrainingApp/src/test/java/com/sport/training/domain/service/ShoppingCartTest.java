package com.sport.training.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sport.training.authentication.domain.dao.UserRepository;
import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.model.User;
import com.sport.training.authentication.domain.service.RoleService;
import com.sport.training.authentication.domain.service.UserService;
import com.sport.training.configuration.TestConfig;
import com.sport.training.domain.dto.ActivityDTO;
import com.sport.training.domain.dto.DisciplineDTO;
import com.sport.training.domain.dto.EventDTO;
import com.sport.training.domain.model.Activity;
import com.sport.training.domain.model.Event;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.RemoveException;

@Transactional
@Import(TestConfig.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShoppingCartTest {

	@Autowired
	ShoppingCartService shoppingCartService;

	@Autowired
	SportService sportService;

	@Autowired
	private CounterService counterService;
	@Autowired
	private UserService userService;

	private final Double _defaultCreditCost = 1.0;
	private final LocalDateTime _defaultDate = LocalDateTime.of(2021, 7, 12, 20, 15, 50, 345678900);

	@Test
	public void testshoppingCartService() throws Exception {

		Long id = getPossibleUniqueStringId();
		double total;
		EventDTO eventDTO = null;
		EventDTO newEventDTO = null;

		// Creates an item
		createEvent(id);

		// Gets the item
		try {
			eventDTO = findEvent(id);
		} catch (FinderException e) {
			fail("Object has been created it should be found");
		}

		// Adds the item into the shopping cart [1 item]
		shoppingCartService.addEvent(eventDTO.getId());

		// Checks the amount of the shopping cart
		total = eventDTO.getCreditCost() * 1;
		assertEquals("The total should be equal to " + total, shoppingCartService.getTotal(), (Double) total);

		// Creates a new event
		id = getPossibleUniqueStringId();
		createEvent(id);
		try {
			newEventDTO = findEvent(id);
		} catch (FinderException e) {
			fail("Object has been created it should be found");
		}

		// Adds the new event into the shopping cart [1 event, 1 new event]]
		shoppingCartService.addEvent(newEventDTO.getId());

		// Checks the amount of the shopping cart
		total = (eventDTO.getCreditCost()) + newEventDTO.getCreditCost();
		assertEquals("The total should be equal to " + total, shoppingCartService.getTotal(), (Double) total);

		// Removes the new item from the shopping cart [10 items]
		shoppingCartService.removeEvent(newEventDTO.getId());

		// Checks the amount of the shopping cart
		total = eventDTO.getCreditCost();
		assertEquals("The total should be equal to " + total, shoppingCartService.getTotal(), (Double) total);

		// Empties the shopping cart [0]
		shoppingCartService.empty();

		// Checks the amount of the shopping cart
		total = 0;
		assertEquals("The total should be equal to " + total, shoppingCartService.getTotal(), (Double) total);

		// Cleans the test environment
		deleteItem(id);
	}

	// ==================================
	// = Private Methods for Event =
	// ==================================
	private EventDTO findEvent(final Long id) throws FinderException {
		final EventDTO eventDTO = sportService.findEvent(id);
		return eventDTO;
	}

	// Creates a category first, then a product and then an item linked to this
	// product
	private void createEvent(final Long id) throws CreateException, FinderException, RemoveException {
		// Create Category
		final DisciplineDTO disciplineDTO = new DisciplineDTO("disc" + id, "name" + id, "description" + id);
		sportService.createDiscipline(disciplineDTO);
		// Create Product
		final ActivityDTO activityDTO = new ActivityDTO("act" + id, "name" + id, "description" + id);
		activityDTO.setDisciplineDTO(disciplineDTO);
		sportService.createActivity(activityDTO);
		// Create Coach
		final String newCoachId = counterService.getUniqueId("Coach");
		final UserDTO coachDTO = new UserDTO("user" + newCoachId, "firstname" + newCoachId, "lastname" + newCoachId);
		coachDTO.setCity("city" + newCoachId);
		coachDTO.setCountry("cnty" + newCoachId);
		coachDTO.setState("state" + newCoachId);
		coachDTO.setAddress1("address1" + newCoachId);
		coachDTO.setAddress2("address2" + newCoachId);
		coachDTO.setTelephone("phone" + newCoachId);
		coachDTO.setEmail("email" + newCoachId);
		coachDTO.setPassword("pwd" + newCoachId);
		coachDTO.setZipcode("zip" + newCoachId);
		coachDTO.setStatut("VALIDE");
		coachDTO.setPassword("pwd" + newCoachId);
		coachDTO.setRoleName("ROLE_COACH");
		userService.createUser(coachDTO);
		
		final EventDTO eventDTO = new EventDTO("name" + id, _defaultDate, _defaultCreditCost, coachDTO, activityDTO);
		  try {
			  sportService.createEvent(eventDTO);
	        } catch ( Exception e ) {
	        	// remove the added category object
	        	sportService.deleteDiscipline(disciplineDTO.getId());
	        	sportService.deleteActivity(activityDTO.getId());
	        	userService.deleteUser(coachDTO.getUsername());
	        	// rethrow the exception
	        	throw e;
	        }
	}

	private void deleteItem(final Long id) throws RemoveException, FinderException {
		sportService.deleteEvent(id);
		sportService.deleteActivity("act" + id);
		sportService.deleteDiscipline("disc" + id);
	}

	protected Long getPossibleUniqueIntId() {
		return (long) (Math.random() * 100000);
	}

	protected Long getPossibleUniqueStringId() {
		Long id = (long) (Math.random() * 100000);
		return id;
	}

}
