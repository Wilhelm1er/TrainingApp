package com.sport.training.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.sport.training.authentication.domain.dao.UserRepository;
import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.model.User;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.ObjectNotFoundException;

/**
 * This class tests the CreateCustomer servlet
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CreateUserTestWebClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(CreateUserTestWebClient.class);

	@Autowired
	private WebClient webClient;

	@Autowired
	private UserRepository _dao;

	@Autowired
	private ModelMapper userDTOModelMapper;

	// ==================================
	// = Test cases =
	// ==================================
	/**
	 * This method ensures that creating an object works. It first finds the object,
	 * makes sure it doesn't exist, creates it using the web page and checks it then
	 * exists.
	 */
	@Test
	public void testServletCreateAthlete() throws Exception {

		final int id = getPossibleUniqueIntId();
		UserDTO athlete = null;

		// Ensures that the object doesn't exist
		try {
			athlete = findUser(id);
			fail("Object has not been created yet it shouldn't be found");
		} catch (ObjectNotFoundException | NoSuchElementException e) {
		}

		// Creates an object
		createAthlete(id);

		// Ensures that the object exists
		try {
			athlete = findUser(id);
		} catch (ObjectNotFoundException | NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkUser(athlete, id);

		// Creates an object with the same identifier. An exception has to be thrown
		try {
			createAthlete(id);
			fail("An object with the same id has already been created");
		} catch (Exception e) {
		}

		// Cleans the test environment
		removeUser(id);

		try {
			findUser(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (ObjectNotFoundException | NoSuchElementException e) {
		}
	}
	
	/**
	 * This method ensures that creating an object works. It first finds the object,
	 * makes sure it doesn't exist, creates it using the web page and checks it then
	 * exists.
	 */
	@Test
	public void testServletCreateCoach() throws Exception {

		final int id = getPossibleUniqueIntId();
		UserDTO coach = null;

		// Ensures that the object doesn't exist
		try {
			coach = findUser(id);
			fail("Object has not been created yet it shouldn't be found");
		} catch (ObjectNotFoundException | NoSuchElementException e) {
		}

		// Creates an object
		createCoach(id);

		// Ensures that the object exists
		try {
			coach = findUser(id);
		} catch (ObjectNotFoundException | NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkUser(coach, id);

		// Creates an object with the same identifier. An exception has to be thrown
		try {
			createCoach(id);
			fail("An object with the same id has already been created");
		} catch (Exception e) {
		}

		// Cleans the test environment
		removeUser(id);

		try {
			findUser(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (ObjectNotFoundException | NoSuchElementException e) {
		}
	}

	/**
	 * This test tries to create an object with a invalid values.
	 */
	@Test
	public void testServletCreateAthleteWithInvalidValues() throws Exception {

		// Creates an object with empty values
		try {
			createInvalidAthlete();
			fail("Object with empty values should not be created");
		} catch (Exception e) {
		}
	}
	
	/**
	 * This test tries to create an object with a invalid values.
	 */
	@Test
	public void testServletCreateCoachWithInvalidValues() throws Exception {

		// Creates an object with empty values
		try {
			createInvalidCoach();
			fail("Object with empty values should not be created");
		} catch (Exception e) {
		}
	}

	// ==================================
	// = Private Methods =
	// ==================================
	private UserDTO findUser(final int id) throws FinderException, NoSuchElementException {
		User user = _dao.findById("" + id).get();
//        System.err.println(customer.getRole());
//        System.err.println(customer.getRole().getName());
		UserDTO userDTO = userDTOModelMapper.map(user, UserDTO.class);
		return userDTO;
	}

	private void createAthlete(final int id) throws Exception {
		// Gets the Web Page
		HtmlPage athletePage = webClient.getPage("/new-athlete");
		LOGGER.debug("athletePage=" + athletePage);
		// Sets parameter to the web page
		((HtmlTextInput) athletePage.getElementById("username")).setText("" + id);
		((HtmlTextInput) athletePage.getElementById("firstname")).setText("firstname" + id);
		((HtmlTextInput) athletePage.getElementById("lastname")).setText("lastname" + id);
		((HtmlPasswordInput) athletePage.getElementById("password")).setText("password" + id);
		((HtmlTextInput) athletePage.getElementById("email")).setText("email" + id);
		((HtmlTextInput) athletePage.getElementById("telephone")).setText("phone" + id);
		((HtmlTextInput) athletePage.getElementById("address1")).setText("address1" + id);
		((HtmlTextInput) athletePage.getElementById("address2")).setText("address2" + id);
		((HtmlTextInput) athletePage.getElementById("city")).setText("city" + id);
		((HtmlTextInput) athletePage.getElementById("state")).setText("state" + id);
		((HtmlTextInput) athletePage.getElementById("zipcode")).setText("zip" + id);
		((HtmlTextInput) athletePage.getElementById("country")).setText("cnty" + id);

		HtmlForm createUserForm = athletePage.getFormByName("userForm");
		HtmlButton submit = createUserForm.getOneHtmlElementByAttribute("button", "type", "submit");
		// Submits the form
		HtmlPage newMessagePage = submit.click();
		if (newMessagePage.getBody().asText().contains("Page d'erreur"))
			throw new Exception("An error has occured");
	}
	
	private void createCoach(final int id) throws Exception {
		// Gets the Web Page
		HtmlPage coachPage = webClient.getPage("/new-coach");
		LOGGER.debug("coachPage=" + coachPage);
		// Sets parameter to the web page
		((HtmlTextInput) coachPage.getElementById("username")).setText("" + id);
		((HtmlTextInput) coachPage.getElementById("firstname")).setText("firstname" + id);
		((HtmlTextInput) coachPage.getElementById("lastname")).setText("lastname" + id);
		((HtmlPasswordInput) coachPage.getElementById("password")).setText("password" + id);
		((HtmlTextInput) coachPage.getElementById("email")).setText("email" + id);
		((HtmlTextInput) coachPage.getElementById("telephone")).setText("phone" + id);
		((HtmlTextInput) coachPage.getElementById("address1")).setText("address1" + id);
		((HtmlTextInput) coachPage.getElementById("address2")).setText("address2" + id);
		((HtmlTextInput) coachPage.getElementById("city")).setText("city" + id);
		((HtmlTextInput) coachPage.getElementById("state")).setText("state" + id);
		((HtmlTextInput) coachPage.getElementById("zipcode")).setText("zip" + id);
		((HtmlTextInput) coachPage.getElementById("country")).setText("cnty" + id);

		HtmlForm createUserForm = coachPage.getFormByName("userForm");
		HtmlButton submit = createUserForm.getOneHtmlElementByAttribute("button", "type", "submit");
		// Submits the form
		HtmlPage newMessagePage = submit.click();
		if (newMessagePage.getBody().asText().contains("Page d'erreur"))
			throw new Exception("An error has occured");
	}

	private void createInvalidAthlete() throws Exception {
		// Gets the Web Page
		HtmlPage athletePage = webClient.getPage("/new-athlete");
		// Sets parameter to the web page
		((HtmlTextInput) athletePage.getElementById("firstname")).setText("");
		((HtmlTextInput) athletePage.getElementById("lastname")).setText("");

		HtmlForm createUserForm = athletePage.getFormByName("userForm");
		HtmlButton submit = createUserForm.getOneHtmlElementByAttribute("button", "type", "submit");
		// Submits the form
		HtmlPage newMessagePage = submit.click();

		if (newMessagePage.getBody().asText().contains("Page d'erreur"))
			throw new Exception("An error has occured");
	}
	
	private void createInvalidCoach() throws Exception {
		// Gets the Web Page
		HtmlPage coachPage = webClient.getPage("/new-coach");
		// Sets parameter to the web page
		((HtmlTextInput) coachPage.getElementById("firstname")).setText("");
		((HtmlTextInput) coachPage.getElementById("lastname")).setText("");

		HtmlForm createUserForm = coachPage.getFormByName("userForm");
		HtmlButton submit = createUserForm.getOneHtmlElementByAttribute("button", "type", "submit");
		// Submits the form
		HtmlPage newMessagePage = submit.click();

		if (newMessagePage.getBody().asText().contains("Page d'erreur"))
			throw new Exception("An error has occured");
	}

	private void removeUser(final int id) throws ObjectNotFoundException {
		final String sid = "" + id;
		_dao.deleteById(sid);
	}

	private void checkUser(final UserDTO user, final int id) {
		assertEquals("UserDTOname", "" + id, user.getUsername());
		assertEquals("firstname", "firstname" + id, user.getFirstname());
		assertEquals("lastname", "lastname" + id, user.getLastname());
		assertNull(user.getPassword()); // password is not passed to the view with the DTO !!!
		assertEquals("city", "city" + id, user.getCity());
		assertEquals("country", "cnty" + id, user.getCountry());
		assertEquals("state", "state" + id, user.getState());
		assertEquals("address1", "address1" + id, user.getAddress1());
		assertEquals("address2", "address2" + id, user.getAddress2());
		assertEquals("telephone", "phone" + id, user.getTelephone());
		assertEquals("email", "email" + id, user.getEmail());
		assertEquals("zipcode", "zip" + id, user.getZipcode());
	}

	private int getPossibleUniqueIntId() {
		return (int) (Math.random() * 100000);
	}

}
