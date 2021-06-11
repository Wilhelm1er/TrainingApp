package com.sport.training.domain.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.sport.training.authentication.domain.dao.UserRepository;
import com.sport.training.authentication.domain.model.User;
import com.sport.training.authentication.domain.service.UserService;
import com.sport.training.authentication.domain.service.UserServiceImpl;
import com.sport.training.domain.dao.BookmarkRepository;
import com.sport.training.domain.dao.DiscussionRepository;
import com.sport.training.domain.dao.EventRepository;
import com.sport.training.domain.dao.MessageRepository;
import com.sport.training.domain.dao.NotationRepository;
import com.sport.training.domain.dto.BookmarkDTO;
import com.sport.training.domain.dto.DiscussionDTO;
import com.sport.training.domain.dto.MessageDTO;
import com.sport.training.domain.dto.NotationDTO;
import com.sport.training.domain.model.Bookmark;
import com.sport.training.domain.model.Discussion;
import com.sport.training.domain.model.Message;
import com.sport.training.domain.model.Notation;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.RemoveException;
import com.sport.training.exception.UpdateException;

/**
 * This class is a facade for all user services.
 */

@Service
@Validated
public class CoachServiceImpl implements CoachService {

	// ======================================
	// = Attributes =
	// ======================================

	@Autowired
	private ModelMapper commonModelMapper, bookmarkModelMapper, messageModelMapper, discussionModelMapper;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private NotationRepository notationRepository;

	@Autowired
	private BookmarkRepository bookmarkRepository;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private DiscussionRepository discussionRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	// =====================================
	// = Constructors =
	// =====================================
	public CoachServiceImpl() {
	}

	// ======================================
	// = Notation Business methods =
	// ======================================

	@Override
	@Transactional
	public NotationDTO createNotation(@Valid NotationDTO notationDTO) throws CreateException {
		final String mname = "createNotation";
		LOGGER.debug("entering " + mname);

		if (notationDTO == null)
			throw new CreateException("DisciplineRegistry object is null");

		if (notationDTO.getEventDTO() == null || notationDTO.getEventDTO().getId() == null
				|| !eventRepository.findById(notationDTO.getEventDTO().getId()).isPresent())
			throw new CreateException("Event must exist to create a notation");

		try {
			userService.findUser(notationDTO.getCoachDTO().getUsername());
		} catch (NullPointerException | FinderException e) {
			throw new CreateException("Coach must exist to create a Notation");
		}

		try {
			userService.findUser(notationDTO.getAthleteDTO().getUsername());
		} catch (NullPointerException | FinderException e) {
			throw new CreateException("Athlete must exist to create a Notation");
		}

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		Notation notation = commonModelMapper.map(notationDTO, Notation.class);

		// Creates the object
		notationRepository.save(notation);

		LOGGER.debug("exiting " + mname);
		return notationDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public NotationDTO findNotation(Long notationId) throws FinderException {
		final String mname = "findNotation";
		LOGGER.debug("entering " + mname + " for id " + notationId);

		checkId(notationId);

		Notation notation = null;
		if (!notationRepository.findById(notationId).isPresent())
			throw new FinderException("Notation must exist to be found");
		else
			notation = notationRepository.findById(notationId).get();

		LOGGER.debug("exiting " + mname);
		return commonModelMapper.map(notation, NotationDTO.class);
	}

	@Override
	@Transactional
	public void deleteNotation(Long notationId) throws FinderException, RemoveException {
		final String mname = "deleteNotation";
		LOGGER.debug("entering " + mname + " for id " + notationId);

		checkId(notationId);

		Notation notation = null;
		if (!notationRepository.findById(notationId).isPresent())
			throw new RemoveException("Notation must exist to be deleted");
		else
			notation = notationRepository.findById(notationId).get();
		// Deletes the object
		notationRepository.delete(notation);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional
	public void updateNotation(@Valid NotationDTO updatedNotationDTO) throws UpdateException {
		final String mname = "updateNotation";
		LOGGER.debug("entering " + mname);

		if (updatedNotationDTO == null)
			throw new UpdateException("Notation object is null");

		// Checks if the object exists
		if (!notationRepository.findById(updatedNotationDTO.getId()).isPresent())
			throw new UpdateException("Notation must exist to be updated");

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		Notation updatedNotation = commonModelMapper.map(updatedNotationDTO, Notation.class);
		// Updates the object
		notationRepository.save(updatedNotation);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional(readOnly = true)
	public List<NotationDTO> findNotations() throws FinderException {
		final String mname = "findNotations";
		LOGGER.debug("entering " + mname);

		// Finds all the objects
		final Iterable<Notation> notations = notationRepository.findAll();
		int size;
		if ((size = ((Collection<Notation>) notations).size()) == 0) {
			throw new FinderException("No notation in the database");
		}
		List<NotationDTO> notationDTOs = ((List<Notation>) notations).stream()
				.map(notation -> commonModelMapper.map(notation, NotationDTO.class)).collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return notationDTOs;
	}

	// ======================================
	// = Bookmark Business methods =
	// ======================================

	@Override
	@Transactional
	public BookmarkDTO createBookmark(@Valid BookmarkDTO bookmarkDTO) throws CreateException {
		final String mname = "createBookmark";
		LOGGER.debug("entering " + mname);

		if (bookmarkDTO == null)
			throw new CreateException("Bookmark object is null");

		try {
			userService.findUser(bookmarkDTO.getCoachDTO().getUsername());
		} catch (NullPointerException | FinderException e) {
			throw new CreateException("Coach must exist to create a Notation");
		}

		try {
			userService.findUser(bookmarkDTO.getAthleteDTO().getUsername());
		} catch (NullPointerException | FinderException e) {
			throw new CreateException("Athlete must exist to create a Notation");
		}

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		Bookmark bookmark = bookmarkModelMapper.map(bookmarkDTO, Bookmark.class);

		// Creates the object
		bookmarkRepository.save(bookmark);

		LOGGER.debug("exiting " + mname);
		return bookmarkDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public BookmarkDTO findBookmark(Long bookmarkId) throws FinderException {
		final String mname = "findBookmark";
		LOGGER.debug("entering " + mname + " for id " + bookmarkId);

		checkId(bookmarkId);

		Bookmark bookmark = null;
		if (!bookmarkRepository.findById(bookmarkId).isPresent())
			throw new FinderException("Bookmark must exist to be found");
		else
			bookmark = bookmarkRepository.findById(bookmarkId).get();

		LOGGER.debug("exiting " + mname);
		return bookmarkModelMapper.map(bookmark, BookmarkDTO.class);
	}

	@Override
	@Transactional
	public void deleteBookmark(Long bookmarkId) throws FinderException, RemoveException {
		final String mname = "deleteBookmark";
		LOGGER.debug("entering " + mname + " for id " + bookmarkId);

		checkId(bookmarkId);

		Bookmark bookmark = null;
		if (!bookmarkRepository.findById(bookmarkId).isPresent())
			throw new RemoveException("Bookmark must exist to be deleted");
		else
			bookmark = bookmarkRepository.findById(bookmarkId).get();
		// Deletes the object
		bookmarkRepository.delete(bookmark);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional
	public void updateBookmark(@Valid BookmarkDTO updatedBookmarkDTO) throws UpdateException {
		final String mname = "updateBookmark";
		LOGGER.debug("entering " + mname);

		if (updatedBookmarkDTO == null)
			throw new UpdateException("Bookmark object is null");

		// Checks if the object exists
		if (!bookmarkRepository.findById(updatedBookmarkDTO.getId()).isPresent())
			throw new UpdateException("Bookmark must exist to be updated");

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		Bookmark updatedBookmark = bookmarkModelMapper.map(updatedBookmarkDTO, Bookmark.class);
		// Updates the object
		bookmarkRepository.save(updatedBookmark);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookmarkDTO> findBookmarks() throws FinderException {
		final String mname = "findBookmarks";
		LOGGER.debug("entering " + mname);

		// Finds all the objects
		final Iterable<Bookmark> bookmarks = bookmarkRepository.findAll();
		int size;
		if ((size = ((Collection<Bookmark>) bookmarks).size()) == 0) {
			throw new FinderException("No bookmark in the database");
		}
		List<BookmarkDTO> bookmarkDTOs = ((List<Bookmark>) bookmarks).stream()
				.map(bookmark -> bookmarkModelMapper.map(bookmark, BookmarkDTO.class)).collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return bookmarkDTOs;
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookmarkDTO> findBookmarksByAthlete(String athleteId) throws FinderException {
		final String mname = "findBookmarksByAthlete";
		LOGGER.debug("entering " + mname);

		checkStringId(athleteId);

		User athlete = null;
		if (!userRepository.findById(athleteId).isPresent())
			throw new FinderException("Coach must exist to be found");
		else
			athlete = userRepository.findById(athleteId).get();

		// Finds all the objects
		final Iterable<Bookmark> bookmarks = bookmarkRepository.findAllByAthlete(athlete);
		int size;
		if ((size = ((Collection<Bookmark>) bookmarks).size()) == 0) {
			throw new FinderException("No bookmark in the database");
		}

		List<BookmarkDTO> bookmarkDTOs = ((List<Bookmark>) bookmarks).stream()
				.map(bookmark -> bookmarkModelMapper.map(bookmark, BookmarkDTO.class)).collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return bookmarkDTOs;
	}

	// ======================================
	// = Message Business methods =
	// ======================================

	@Override
	@Transactional
	public MessageDTO createMessage(@Valid MessageDTO messageDTO) throws CreateException {
		final String mname = "createMessage";
		LOGGER.debug("entering " + mname);

		if (messageDTO == null)
			throw new CreateException("Message object is null");

		try {
			userService.findUser(messageDTO.getSenderDTO().getUsername());
		} catch (NullPointerException | FinderException e) {
			throw new CreateException("Sender must exist to create a Notation");
		}

		try {
			userService.findUser(messageDTO.getRecipientDTO().getUsername());
		} catch (NullPointerException | FinderException e) {
			throw new CreateException("Recipient must exist to create a Notation");
		}

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		Message message = messageModelMapper.map(messageDTO, Message.class);

		// Creates the object
		messageRepository.save(message);

		LOGGER.debug("exiting " + mname);
		return messageDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public MessageDTO findMessage(Long messageId) throws FinderException {
		final String mname = "findMessage";
		LOGGER.debug("entering " + mname + " for id " + messageId);

		checkId(messageId);

		Message message = null;
		if (!messageRepository.findById(messageId).isPresent())
			throw new FinderException("Message must exist to be found");
		else
			message = messageRepository.findById(messageId).get();

		LOGGER.debug("exiting " + mname);
		return messageModelMapper.map(message, MessageDTO.class);
	}

	@Override
	@Transactional
	public void deleteMessage(Long messageId) throws FinderException, RemoveException {
		final String mname = "deleteMessage";
		LOGGER.debug("entering " + mname + " for id " + messageId);

		checkId(messageId);

		Message message = null;
		if (!messageRepository.findById(messageId).isPresent())
			throw new RemoveException("Message must exist to be deleted");
		else
			message = messageRepository.findById(messageId).get();
		// Deletes the object
		messageRepository.delete(message);
		LOGGER.debug("exiting " + mname);

	}

	@Override
	@Transactional
	public void updateMessage(@Valid MessageDTO messageDTO) throws UpdateException {
		final String mname = "updateMessage";
		LOGGER.debug("entering " + mname);

		if (messageDTO == null)
			throw new UpdateException("Message object is null");

		// Checks if the object exists
		if (!messageRepository.findById(messageDTO.getId()).isPresent())
			throw new UpdateException("Message must exist to be updated");

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		Message message = messageModelMapper.map(messageDTO, Message.class);
		// Updates the object
		messageRepository.save(message);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MessageDTO> findMessages() throws FinderException {
		final String mname = "findMessages";
		LOGGER.debug("entering " + mname);

		// Finds all the objects
		final Iterable<Message> messages = messageRepository.findAll();
		int size;
		if ((size = ((Collection<Message>) messages).size()) == 0) {
			throw new FinderException("No message in the database");
		}
		List<MessageDTO> messageDTOs = ((List<Message>) messages).stream()
				.map(message -> messageModelMapper.map(message, MessageDTO.class)).collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return messageDTOs;
	}

	@Override
	@Transactional(readOnly = true)
	public List<MessageDTO> findMessagesByDiscussion(Long discussionId) throws FinderException {
		final String mname = "findMessagesByDiscussion";
		LOGGER.debug("entering " + mname);

		checkId(discussionId);

		Discussion discussion = null;
		if (!discussionRepository.findById(discussionId).isPresent())
			throw new FinderException("Discussion must exist to be found");
		else
			discussion = discussionRepository.findById(discussionId).get();

		// Finds all the objects
		final Iterable<Message> messages = messageRepository.findAllByDiscussion(discussion);
		int size;
		if ((size = ((Collection<Message>) messages).size()) == 0) {
			throw new FinderException("No message in the database");
		}

		List<MessageDTO> messageDTOs = ((List<Message>) messages).stream()
				.map(message -> messageModelMapper.map(message, MessageDTO.class)).collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return messageDTOs;
	}

	// ======================================
	// = Discussion Business methods =
	// ======================================

	@Override
	@Transactional
	public DiscussionDTO createDiscussion(@Valid DiscussionDTO discussionDTO) throws CreateException {
		final String mname = "createDiscussion";
		LOGGER.debug("entering " + mname);

		if (discussionDTO == null)
			throw new CreateException("Discussion object is null");

		try {
			userService.findUser(discussionDTO.getUserDTO().getUsername());
		} catch (NullPointerException | FinderException e) {
			throw new CreateException("User must exist to create a Discussion");
		}

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		Discussion discussion = discussionModelMapper.map(discussionDTO, Discussion.class);

		// Creates the object
		discussionRepository.save(discussion);

		LOGGER.debug("exiting " + mname);
		return discussionDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public DiscussionDTO findDiscussion(Long discussionId) throws FinderException {
		final String mname = "findDiscussion";
		LOGGER.debug("entering " + mname + " for id " + discussionId);

		checkId(discussionId);

		Discussion discussion = null;
		if (!discussionRepository.findById(discussionId).isPresent())
			throw new FinderException("Discussion must exist to be found");
		else
			discussion = discussionRepository.findById(discussionId).get();

		LOGGER.debug("exiting " + mname);
		return discussionModelMapper.map(discussion, DiscussionDTO.class);
	}

	@Override
	@Transactional
	public void deleteDiscussion(Long discussionId) throws FinderException, RemoveException {
		final String mname = "deleteDiscussion";
		LOGGER.debug("entering " + mname + " for id " + discussionId);

		checkId(discussionId);

		Discussion discussion = null;
		if (!discussionRepository.findById(discussionId).isPresent())
			throw new RemoveException("Discussion must exist to be deleted");
		else
			discussion = discussionRepository.findById(discussionId).get();
		// Deletes the object
		discussionRepository.delete(discussion);
		LOGGER.debug("exiting " + mname);

	}

	@Override
	@Transactional
	public void updateDiscussion(@Valid DiscussionDTO discussionDTO) throws UpdateException {
		final String mname = "updateDiscussion";
		LOGGER.debug("entering " + mname);

		if (discussionDTO == null)
			throw new UpdateException("Discussion object is null");

		// Checks if the object exists
		if (!discussionRepository.findById(discussionDTO.getId()).isPresent())
			throw new UpdateException("Discussion must exist to be updated");

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		Discussion discussion = discussionModelMapper.map(discussionDTO, Discussion.class);
		// Updates the object
		discussionRepository.save(discussion);
		LOGGER.debug("exiting " + mname);

	}

	@Override
	@Transactional(readOnly = true)
	public List<DiscussionDTO> findDiscussions() throws FinderException {
		final String mname = "findDiscussions";
		LOGGER.debug("entering " + mname);

		// Finds all the objects
		final Iterable<Discussion> discussions = discussionRepository.findAll();
		int size;
		if ((size = ((Collection<Discussion>) discussions).size()) == 0) {
			throw new FinderException("No discussion in the database");
		}

		List<DiscussionDTO> discussionDTOs = ((List<Discussion>) discussions).stream()
				.map(discussion -> discussionModelMapper.map(discussion, DiscussionDTO.class))
				.collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return discussionDTOs;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DiscussionDTO> findDiscussionsByUser(String userId) throws FinderException {
		final String mname = "findDiscussionsByUser";
		LOGGER.debug("entering " + mname);

		checkStringId(userId);

		User user = null;
		if (!userRepository.findById(userId).isPresent())
			throw new FinderException("User must exist to be found");
		else
			user = userRepository.findById(userId).get();

		// Finds all the objects
		final Iterable<Discussion> discussions = discussionRepository.findAllByUser(user);
		int size;
		if ((size = ((Collection<Discussion>) discussions).size()) == 0) {
			throw new FinderException("No discussion in the database");
		}

		List<DiscussionDTO> discussionDTOs = ((List<Discussion>) discussions).stream()
				.map(discussion -> discussionModelMapper.map(discussion, DiscussionDTO.class))
				.collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return discussionDTOs;
	}

	// ======================================
	// = Private Methods =
	// ======================================

	private void checkId(final long l) throws FinderException {
		if (l == 0)
			throw new FinderException("Id should not be 0");
	}

	private void checkStringId(final String id) throws FinderException {
		if (id == null || id.equals(""))
			throw new FinderException(id + " should not be null or empty");
	}
}
