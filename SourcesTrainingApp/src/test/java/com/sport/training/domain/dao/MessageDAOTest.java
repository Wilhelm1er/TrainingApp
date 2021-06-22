package com.sport.training.domain.dao;

import static org.junit.Assert.assertEquals;
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

import com.sport.training.authentication.domain.dao.UserRepository;
import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.model.Discussion;
import com.sport.training.domain.model.Message;
import com.sport.training.domain.service.CounterService;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.ObjectNotFoundException;
import com.sport.training.exception.RemoveException;
import com.sport.training.exception.UpdateException;

/**
 * This class tests the ItemDAO class
 */
@RunWith(SpringRunner.class)
@SpringBootTest
//@Transactional
public class MessageDAOTest {

	private static final String COUNTER_NAME = "Message";

	private static Logger logger = LogManager.getLogger(MessageDAOTest.class);

	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private DiscussionRepository discussionRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CounterService counterService;

	private Random random = new Random();

	// ==================================
	// = Test cases =
	// ==================================
	/**
	 * This test tries to find an object with a invalid identifier.
	 */
	@Test
	public void testDomainFindMessageWithInvalidValues() throws Exception {

		// Finds an object with a unknown identifier
		final Long id = messageRepository.findLastId().orElse(10000L) + 1;

		try {
			findMessage(id);
			fail("Object with unknonw id should not be found");
		} catch (NoSuchElementException e) {
		}

		// Finds an object with an empty identifier
		try {
			messageRepository.findById(random.nextLong() + 1L).get();
			fail("Object with empty id should not be found");
		} catch (Exception e) {
		}

		// Finds an object with a null identifier
		try {
			messageRepository.findById(null).get();
			fail("Object with null id should not be found");
		} catch (Exception e) {
		}
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testDomainFindAllMessages() throws Exception {
		final Long id = messageRepository.findLastId().orElse(10000L) + 1;

		// First findAll
		final int firstSize = findAllMessages();
		// Create an object
		final long messageId = createMessage(id);

		// Ensures that the object exists
		try {
			findMessage(messageId);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// second findAll
		final int secondSize = findAllMessages();

		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Cleans the test environment
		removeMessage(messageId);

		try {
			findMessage(messageId);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testDomainFindAllMessagesForACoach() throws Exception {

		User newCoach = createNewUser();
		final String coachId = newCoach.getUsername();

		// First findAll
		final int firstSize = findAllMessagesBySender(coachId);

		// Checks that the collection is empty
		if (firstSize != 0)
			fail("The collection should be empty");

		// Create an object
		Message message1 = createMessageForCoach(newCoach);

		// Ensures that the object exists
		try {
			findMessage(message1.getId());
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// second findAll
		final int secondSize = findAllMessagesBySender(coachId);

		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Create an new object with a different id
		Message message2 = createMessageForCoach(newCoach);

		// Ensures that the new object exists
		try {
			findMessage(message2.getId());
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// third findAll
		final int thirdSize = findAllMessagesBySender(coachId);

		// Checks that the collection size has increase of one
		if (thirdSize != secondSize + 1)
			fail("The collection should have the same size");

		// Cleans the test environment
		messageRepository.delete(message1);
		messageRepository.delete(message2);
		removeUser(newCoach);
	}

	/**
	 * This test ensures that the method findAll works. It does a first findAll,
	 * creates a new object and does a second findAll.
	 */
	@Test
	public void testDomainFindAllMessagesForAnActivity() throws Exception {

		Discussion newDiscussion = createNewDiscussion();
		final Long discussionId = newDiscussion.getId();

		// First findAll
		final int firstSize = findAllMessagesByDiscussion(discussionId);

		// Checks that the collection is empty
		if (firstSize != 0)
			fail("The collection should be empty");

		// Create an object
		Message message1 = createMessageForDiscussion(newDiscussion);

		// Ensures that the object exists
		try {
			findMessage(message1.getId());
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// second findAll
		final int secondSize = findAllMessagesByDiscussion(discussionId);

		// Checks that the collection size has increase of one
		if (firstSize + 1 != secondSize)
			fail("The collection size should have increased by 1");

		// Create an new object with a different id
		Message message2 = createMessageForDiscussion(newDiscussion);

		// Ensures that the new object exists
		try {
			findMessage(message2.getId());
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// third findAll
		final int thirdSize = findAllMessagesByDiscussion(discussionId);

		// Checks that the collection size has increase of one
		if (thirdSize != secondSize + 1)
			fail("The collection should have the same size");

		// Cleans the test environment
		messageRepository.delete(message1);
		messageRepository.delete(message2);
		removeDiscussion(newDiscussion);
	}

	/**
	 * This method ensures that creating an object works. It first finds the object,
	 * makes sure it doesn't exist, creates it and checks it then exists.
	 */
	@Test
	public void testDomainCreateMessage() throws Exception {
		final Long id = messageRepository.findLastId().orElse(10000L) + 1;
		Message message = null;

		// Ensures that the object doesn't exist
		try {
			message = findMessage(id);
			fail("Object has not been created yet it shouldn't be found");
		} catch (NoSuchElementException e) {
		}

		// Creates an object
		createMessage(id);

		// Ensures that the object exists
		try {
			message = findMessage(id);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkMessage(message, id);

		// Cleans the test environment
		removeMessage(id);

		try {
			findMessage(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test make sure that updating an object success
	 */
	@Test
	public void testDomainUpdateMessage() throws Exception {
		final Long id = messageRepository.findLastId().orElse(10000L) + 1;

		// Creates an object
		createMessage(id);

		// Ensures that the object exists
		Message message = null;
		try {
			message = findMessage(id);
		} catch (NoSuchElementException e) {
			fail("Object has been created it should be found");
		}

		// Checks that it's the right object
		checkMessage(message, id);

		// Updates the object with new values
		updateMessage(message, id + 1);

		// Ensures that the object still exists
		Message messageUpdated = null;
		try {
			messageUpdated = findMessage(id);
		} catch (NoSuchElementException e) {
			fail("Object should be found");
		}

		// Checks that the object values have been updated
		checkMessage(messageUpdated, id + 1);

		// Cleans the test environment
		removeMessage(id);

		try {
			findMessage(id);
			fail("Object has been deleted it shouldn't be found");
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * This test ensures that the system cannont remove an unknown object
	 */
	@Test
	public void testDomainDeleteUnknownMessage() throws Exception {
		// Removes an unknown object
		try {
			messageRepository.delete(null);
			fail("Deleting an unknown object should break");
		} catch (Exception e) {
		}
	}

	// ==================================
	// = Private Methods =
	// ==================================
	private Message findMessage(final Long id) throws NoSuchElementException {
		final Message message = messageRepository.findById(id).get();
		return message;
	}

	private int findAllMessages() throws FinderException {
		try {
			return ((Collection<Message>) messageRepository.findAll()).size();
		} catch (Exception e) {
			logger.info("Exception is ... " + e.getMessage());
			return 0;
		}
	}

	private int findAllMessagesByDiscussion(Long discussionId) throws FinderException {
		try {
			Discussion discussion = discussionRepository.findById(discussionId).get();
			return ((Collection<Message>) messageRepository.findAllByDiscussion(discussion)).size();
		} catch (Exception e) {
			return 0;
		}
	}

	private int findAllMessagesBySender(String senderId) throws FinderException {
		try {
			User sender = userRepository.findById(senderId).get();
			return ((Collection<Message>) messageRepository.findAllBySender(sender)).size();
		} catch (Exception e) {
			return 0;
		}
	}

	// Creates a coach first, then a activity and then an message linked to this
	// activity and coach
	private long createMessage(final long id) throws CreateException, RemoveException, FinderException {

		// Create Sender
		final User sender = createNewUser();

		// Create Sender
		final User recipient = createNewUser();

		// Create Activity
		final Discussion discussion = createNewDiscussion();

		// Create Message
		final Message message = new Message("texte" + id, sender, recipient, discussion);

		messageRepository.save(message);
		return message.getId();
	}

	// Creates a discipline, an activity and a coach, updates the message with this
	// new
	// activity and coach
	private void updateMessage(final Message message, final long id)
			throws UpdateException, CreateException, RemoveException, FinderException {
		// get old activity
		final Discussion discu = message.getDiscussion();

		// Create Activity
		final Discussion discussion = createNewDiscussion();

		// get old activity
		final User sen = message.getSender();

		// Create Coach
		final User sender = createNewUser();
		// Updates the message
		message.setTexte("texte" + id);
		message.setDiscussion(discussion);
		message.setSender(sender);
		messageRepository.save(message);
		// delete old discipline & activity
		removeDiscussion(discu);
		removeUser(sen);
	}

	private void checkMessage(final Message message, final Long id) {
		assertEquals("texte", "texte" + id, message.getTexte());
		assertNotNull("sender", message.getSender());
		assertNotNull("recipient", message.getRecipient());
	}

	private void removeMessage(final long messageId) throws RemoveException, ObjectNotFoundException {
		Message message = messageRepository.findById(messageId).get();
		final Long discussionId = message.getDiscussion().getId();
		Discussion discussion = discussionRepository.findById(discussionId).get();
		final String senderId = message.getSender().getUsername();
		User sender = userRepository.findById(senderId).get();
		final String recipientId = message.getRecipient().getUsername();
		User recipient = userRepository.findById(recipientId).get();

		userRepository.delete(sender);
		discussionRepository.delete(discussion);
		userRepository.delete(recipient);
		messageRepository.deleteById(messageId);
	}

	// Creates a discipline first, then an activity and return it
	private Discussion createNewDiscussion() throws CreateException, FinderException {

		// Create User
		final User user = createNewUser();

		// Finds an object with a unknown identifier
		final Long id = discussionRepository.findLastId().orElse(10000L) + 1;
		// Create discussion
		final String newDiscussionId = counterService.getUniqueId("Discussion");
		final Discussion discussion = new Discussion(user, "subject" + newDiscussionId);

		try {
			discussionRepository.save(discussion);
		} catch (Exception e) {
			// remove the added user object
			userRepository.deleteById(user.getUsername());
			// rethrow the exception
			throw e;
		}
		return discussion;
	}

	// Creates a coach
	private User createNewUser() throws CreateException, FinderException {
		// Create Coach
		final String newUserId = counterService.getUniqueId("Coach");
		final User user = new User("user" + newUserId, "firstname" + newUserId, "lastname" + newUserId);
		user.setCity("city" + newUserId);
		user.setCountry("cnty" + newUserId);
		user.setState("state" + newUserId);
		user.setAddress1("address1" + newUserId);
		user.setAddress2("address2" + newUserId);
		user.setTelephone("phone" + newUserId);
		user.setEmail("email" + newUserId);
		user.setPassword("pwd" + newUserId);
		user.setZipcode("zip" + newUserId);
		user.setStatut("VALIDE");
		user.setPassword("pwd" + newUserId);
		userRepository.save(user);
		return user;
	}

	// Creates an message linked to an existing activity
	private Message createMessageForCoach(final User sender) throws CreateException, FinderException {
		final String id = counterService.getUniqueId(COUNTER_NAME);

		// Create Activity
		Discussion discussion = createNewDiscussion();
		// Create Coach
		User recipient = createNewUser();
		final Message message = new Message("texte" + id, sender, recipient, discussion);
		messageRepository.save(message);
		return message;
	}

	// Creates an message linked to an existing activity
	private Message createMessageForDiscussion(final Discussion discussion) throws CreateException, FinderException {
		final String id = counterService.getUniqueId(COUNTER_NAME);
		// Create Coach
		User sender = createNewUser();
		// Create Coach
		User recipient = createNewUser();
		final Message message = new Message("texte" + id, sender, recipient, discussion);
		messageRepository.save(message);
		return message;
	}

	private void removeDiscussion(final Discussion discussion) throws RemoveException, ObjectNotFoundException {
		final String userId = discussion.getUser().getUsername();
		User user = userRepository.findById(userId).get();
		userRepository.delete(user);
		discussionRepository.delete(discussion);
	}

	private void removeUser(final User coach) throws RemoveException, ObjectNotFoundException {
		userRepository.delete(coach);
	}
}
