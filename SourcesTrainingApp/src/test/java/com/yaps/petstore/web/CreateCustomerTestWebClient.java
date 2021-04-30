package com.yaps.petstore.web;

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
public class CreateCustomerTestWebClient {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CreateCustomerTestWebClient.class);
	
	@Autowired
	private WebClient webClient;
	
	@Autowired
	private UserRepository _dao;
	
	@Autowired
	private ModelMapper userDTOModelMapper;

    //==================================
    //=            Test cases          =
    //==================================
    /**
     * This method ensures that creating an object works. It first finds the object,
     * makes sure it doesn't exist, creates it using the web page and checks it then exists.
     */
    @Test
    public void testServletCreateCustomer() throws Exception {

        final int id = getPossibleUniqueIntId();
        UserDTO customer = null;

        // Ensures that the object doesn't exist
        try {
            customer = findCustomer(id);
            fail("Object has not been created yet it shouldn't be found");
        } catch (ObjectNotFoundException | NoSuchElementException e) {
        }

        // Creates an object
        createCustomer(id);

        // Ensures that the object exists
        try {
            customer = findCustomer(id);
        } catch (ObjectNotFoundException | NoSuchElementException e) {
            fail("Object has been created it should be found");
        }

        // Checks that it's the right object
        checkCustomer(customer, id);

        // Creates an object with the same identifier. An exception has to be thrown
        try {
            createCustomer(id);
            fail("An object with the same id has already been created");
        } catch (Exception e) {
        }

        // Cleans the test environment
        removeCustomer(id);

        try {
            findCustomer(id);
            fail("Object has been deleted it shouldn't be found");
        } catch (ObjectNotFoundException | NoSuchElementException e) {
        }
    }

    /**
     * This test tries to create an object with a invalid values.
     */
    @Test
    public void testServletCreateCustomerWithInvalidValues() throws Exception {

        // Creates an object with empty values
        try {
            createInvalidCustomer();
            fail("Object with empty values should not be created");
        } catch (Exception e) {
        }
    }
    
    /**
     * This test tries to create an object with a invalid values.
     */
    @Test
    public void testServletCreateCustomerWithInvalidCC() throws Exception {
    	final int id = getPossibleUniqueIntId();
        // Creates an object with empty values
        try {
        	createCustomerWithInvalidCC(id);
            fail("Object with invalid CC values should not be created");
        } catch (Exception e) {
        }
    }

    //==================================
    //=         Private Methods        =
    //==================================
    private UserDTO findCustomer(final int id) throws FinderException, NoSuchElementException {
        User customer = _dao.findById("" + id).get();
//        System.err.println(customer.getRole());
//        System.err.println(customer.getRole().getName());
        UserDTO userDTO = userDTOModelMapper.map(customer, UserDTO.class);
        return userDTO;
    }

    private void createCustomer(final int id) throws Exception {
        // Gets the Web Page
        HtmlPage customerPage= webClient.getPage("/new-account");
        LOGGER.debug("customerPage="+customerPage);
        // Sets parameter to the web page
        ((HtmlTextInput)customerPage.getElementById("username")).setText("" + id);
        ((HtmlTextInput)customerPage.getElementById("firstname")).setText("firstname" + id);
        ((HtmlTextInput)customerPage.getElementById("lastname")).setText( "lastname" + id);
        ((HtmlPasswordInput)customerPage.getElementById("password")).setText( "password" + id);
        ((HtmlTextInput)customerPage.getElementById("email")).setText( "email" + id);
        ((HtmlTextInput)customerPage.getElementById("telephone")).setText( "phone" + id);
        ((HtmlTextInput)customerPage.getElementById("street1")).setText( "street1" + id);
        ((HtmlTextInput)customerPage.getElementById("street2")).setText( "street2" + id);
        ((HtmlTextInput)customerPage.getElementById("city")).setText( "city" + id);
        ((HtmlTextInput)customerPage.getElementById("state")).setText( "state" + id);
        ((HtmlTextInput)customerPage.getElementById("zipcode")).setText( "zip" + id);
        ((HtmlTextInput)customerPage.getElementById("country")).setText( "cnty" + id);
        ((HtmlTextInput)customerPage.getElementById("creditCardExpiryDate")).setText("10/23");
        ((HtmlTextInput)customerPage.getElementById("creditCardNumber")).setText("4564 1231 4564 1222");
        ((HtmlTextInput)customerPage.getElementById("creditCardType")).setText("Visa");

        HtmlForm createCustomerForm = customerPage.getFormByName("userForm");
        HtmlButton submit = createCustomerForm.getOneHtmlElementByAttribute("button", "type", "submit");
     // Submits the form
        HtmlPage newMessagePage = submit.click();
        if ( newMessagePage.getBody().asText().contains("Page d'erreur"))
            throw new Exception("An error has occured");
    }
    
    private void createCustomerWithInvalidCC(final int id) throws Exception {
        // Gets the Web Page
        HtmlPage customerPage= webClient.getPage("/new-account");
        LOGGER.debug("customerPage="+customerPage);
        // Sets parameter to the web page
        ((HtmlTextInput)customerPage.getElementById("username")).setText("" + id);
        ((HtmlTextInput)customerPage.getElementById("firstname")).setText("firstname" + id);
        ((HtmlTextInput)customerPage.getElementById("lastname")).setText( "lastname" + id);
        ((HtmlPasswordInput)customerPage.getElementById("password")).setText( "password" + id);
        ((HtmlTextInput)customerPage.getElementById("email")).setText( "email" + id);
        ((HtmlTextInput)customerPage.getElementById("telephone")).setText( "phone" + id);
        ((HtmlTextInput)customerPage.getElementById("street1")).setText( "street1" + id);
        ((HtmlTextInput)customerPage.getElementById("street2")).setText( "street2" + id);
        ((HtmlTextInput)customerPage.getElementById("city")).setText( "city" + id);
        ((HtmlTextInput)customerPage.getElementById("state")).setText( "state" + id);
        ((HtmlTextInput)customerPage.getElementById("zipcode")).setText( "zip" + id);
        ((HtmlTextInput)customerPage.getElementById("country")).setText( "cnty" + id);
        ((HtmlTextInput)customerPage.getElementById("creditCardExpiryDate")).setText("10/23");
        ((HtmlTextInput)customerPage.getElementById("creditCardNumber")).setText("4564 1231 4564 1221");
        ((HtmlTextInput)customerPage.getElementById("creditCardType")).setText("Visa");

        HtmlForm createCustomerForm = customerPage.getFormByName("userForm");
        HtmlButton submit = createCustomerForm.getOneHtmlElementByAttribute("button", "type", "submit");
     // Submits the form
        HtmlPage newMessagePage = submit.click();
        if ( newMessagePage.getBody().asText().contains("Page d'erreur"))
            throw new Exception("An error has occured");
    }

    private void createInvalidCustomer() throws Exception {
    	// Gets the Web Page
    	HtmlPage customerPage= webClient.getPage("/new-account");  
    	// Sets parameter to the web page
    	((HtmlTextInput)customerPage.getElementById("firstname")).setText( "");
    	((HtmlTextInput)customerPage.getElementById("lastname")).setText( "");

    	HtmlForm createCustomerForm = customerPage.getFormByName("customerForm");
    	HtmlButton submit = createCustomerForm.getOneHtmlElementByAttribute("button", "type", "submit");
    	// Submits the form        
    	HtmlPage newMessagePage = submit.click();

    	if ( newMessagePage.getBody().asText().contains("Page d'erreur"))
    		throw new Exception("An error has occured");
    }

    private void removeCustomer(final int id) throws ObjectNotFoundException {
        final String sid = "" + id;
        _dao.deleteById(sid);
    }

    private void checkCustomer(final UserDTO customer, final int id) {
        assertEquals("UserDTOname", "" + id, customer.getUsername());
        assertEquals("firstname", "firstname" + id, customer.getFirstname());
        assertEquals("lastname", "lastname" + id, customer.getLastname());
        assertNull(customer.getPassword()); // password is not passed to the view with the DTO !!!
        assertEquals("city", "city" + id, customer.getCity());
        assertEquals("country", "cnty" + id, customer.getCountry());
        assertEquals("state", "state" + id, customer.getState());
        assertEquals("street1", "street1" + id, customer.getStreet1());
        assertEquals("street2", "street2" + id, customer.getStreet2());
        assertEquals("telephone", "phone" + id, customer.getTelephone());
        assertEquals("email", "email" + id, customer.getEmail());
        assertEquals("zipcode", "zip" + id, customer.getZipcode());
        assertEquals("CreditCardExpiryDate", "10/23", customer.getCreditCardExpiryDate());
        assertEquals("CreditCardNumber", "4564 1231 4564 1222", customer.getCreditCardNumber());
        assertEquals("CreditCardType", "Visa", customer.getCreditCardType());
    }

    private int getPossibleUniqueIntId() {
    	return (int) (Math.random() * 100000);
    }

}
