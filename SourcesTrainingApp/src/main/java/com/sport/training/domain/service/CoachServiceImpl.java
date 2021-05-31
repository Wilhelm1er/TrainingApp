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
import com.sport.training.domain.dao.EventRepository;
import com.sport.training.domain.dao.NotationRepository;
import com.sport.training.domain.dto.BookmarkDTO;
import com.sport.training.domain.dto.DisciplineRegistryDTO;
import com.sport.training.domain.dto.NotationDTO;
import com.sport.training.domain.model.Bookmark;
import com.sport.training.domain.model.DisciplineRegistry;
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
	private ModelMapper commonModelMapper,bookmarkModelMapper;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private NotationRepository notationRepository;

	@Autowired
	private BookmarkRepository bookmarkRepository;

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
		Bookmark bookmark = commonModelMapper.map(bookmarkDTO, Bookmark.class);

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
		return commonModelMapper.map(bookmark, BookmarkDTO.class);
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
		Bookmark updatedBookmark = commonModelMapper.map(updatedBookmarkDTO, Bookmark.class);
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
				.map(bookmark -> commonModelMapper.map(bookmark, BookmarkDTO.class)).collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return bookmarkDTOs;
	}

	@Override
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
