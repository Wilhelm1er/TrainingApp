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
import com.sport.training.domain.model.Discipline;
import com.sport.training.domain.model.Event;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.ObjectNotFoundException;
import com.sport.training.exception.RemoveException;

//@Transactional
@Import(TestConfig.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShoppingCartTest {

	@Autowired
	ShoppingCartService shoppingCartService;

	@Autowired
	SportService sportService;

	@Autowired
	private UserService userService;

	private final Double _defaultCreditCost = 1.0;
	private final LocalDateTime _defaultDate = LocalDateTime.of(2021, 7, 12, 20, 15, 50, 345678900);

	@Test
	public void testshoppingCartService() throws Exception {

		double total;
		EventDTO eventDTO = null;
		EventDTO newEventDTO = null;

		// Creates an event
		Long id = createEvent();

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
		
		Long id2 = createEvent();
		try {
			newEventDTO = findEvent(id2);
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
		deleteEvent(id2);
	}

	// ==================================
	// = Private Methods for Event =
	// ==================================
	private EventDTO findEvent(final Long id) throws FinderException {
		final EventDTO eventDTO = sportService.findEvent(id);
		return eventDTO;
	}

	// Creates a discipline first, then an activity and then an event linked to this
	// product
	private long createEvent() throws CreateException, FinderException, RemoveException {
		final String coachId = "coach1";
		UserDTO coachDTO = findUser(coachId);
		ActivityDTO activityDTO = findActivity("ABDOS");
		// Create Event
		final EventDTO eventDTO = new EventDTO("name" + coachId, _defaultDate, _defaultCreditCost, coachDTO,
				activityDTO);
		eventDTO.setActivityDTO(activityDTO);
		eventDTO.setDuration(50);
		eventDTO.setIntensity(3);
		eventDTO.setEquipment("equipment" + coachId);
		eventDTO.setDescription("description" + coachId);
		
		EventDTO evtDTO = sportService.createEvent(eventDTO);
		
		return evtDTO.getId();
		
	}

	private void deleteEvent(final Long id) throws RemoveException, FinderException {
		sportService.deleteEvent(id);
	}
	
	private UserDTO findUser(final String id) throws FinderException {
		final UserDTO userDTO = userService.findUser(id);
		return userDTO;
	}
	private ActivityDTO findActivity(final String id) throws FinderException {
		final ActivityDTO activityDTO = sportService.findActivity(id);
		return activityDTO;
	}
}
