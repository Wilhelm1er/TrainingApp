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

import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.service.UserServiceImpl;
import com.sport.training.domain.dao.DisciplineRegistryRepository;
import com.sport.training.domain.dao.EventRegistryRepository;
import com.sport.training.domain.dto.DisciplineRegistryDTO;
import com.sport.training.domain.dto.EventRegistryDTO;
import com.sport.training.domain.model.Discipline;
import com.sport.training.domain.model.DisciplineRegistry;
import com.sport.training.domain.model.EventRegistry;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.DuplicateKeyException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.RemoveException;
import com.sport.training.exception.UpdateException;

/**
 * This class is a facade for all user services.
 */

@Service
@Validated
public class RegistryServiceImpl implements RegistryService {

	// ======================================
	// = Attributes =
	// ======================================
	
	@Autowired
	private ModelMapper commonModelMapper;
	
	@Autowired
	private DisciplineRegistryRepository disciplineRegistryRepository;
	
	@Autowired
	private EventRegistryRepository eventRegistryRepository;
	

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	// =====================================
		// = Constructors =
		// =====================================
		public RegistryServiceImpl() {
		}

		
	// =====================================
		// =    DisciplineRegistry Business methods    =
		// =====================================
	
	@Override
	@Transactional
	public DisciplineRegistryDTO createDisciplineRegistry(@Valid DisciplineRegistryDTO disciplineRegistryDTO)
			throws CreateException {
		final String mname = "createDisciplineRegistry";
		LOGGER.debug("entering " + mname);

		if (disciplineRegistryDTO == null || disciplineRegistryDTO.getId() == null || disciplineRegistryDTO.getId().equals(""))
			throw new CreateException("Discipline registry object is null");

		try {
			if (findDisciplineRegistry(disciplineRegistryDTO.getId()) != null)
				throw new DuplicateKeyException();
		} catch (FinderException e) {
		}

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		DisciplineRegistry disciplineRegistry = commonModelMapper.map(disciplineRegistryDTO, DisciplineRegistry.class);

		// Creates the object
		disciplineRegistryRepository.save(disciplineRegistry);

		LOGGER.debug("exiting " + mname);
		return disciplineRegistryDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public DisciplineRegistryDTO findDisciplineRegistry(final String disciplineRegistryId) throws FinderException {
		final String mname = "findDiscipline";
		LOGGER.debug("entering " + mname + " for id " + disciplineRegistryId);

		checkId(disciplineRegistryId);

		DisciplineRegistry disciplineRegistry = null;
		if (!disciplineRegistryRepository.findById(disciplineRegistryId).isPresent())
			throw new FinderException("Discipline must exist to be found");
		else
			disciplineRegistry = disciplineRegistryRepository.findById(disciplineRegistryId).get();

		LOGGER.debug("exiting " + mname);
		return commonModelMapper.map(disciplineRegistry, DisciplineRegistryDTO.class);
	}

	@Override
	@Transactional
	public void deleteDisciplineRegistry(final String disciplineRegistryId) throws FinderException, RemoveException {
		final String mname = "deleteDisciplineRegistry";
		LOGGER.debug("entering " + mname + " for id " + disciplineRegistryId);

		checkId(disciplineRegistryId);

		DisciplineRegistry disciplineRegistry = null;
		if (!disciplineRegistryRepository.findById(disciplineRegistryId).isPresent())
			throw new RemoveException("Discipline registry must exist to be deleted");
		else
			disciplineRegistry = disciplineRegistryRepository.findById(disciplineRegistryId).get();
		// Deletes the object
		disciplineRegistryRepository.delete(disciplineRegistry);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional
	public void updateDisciplineRegistry(@Valid final DisciplineRegistryDTO updatedDisciplineRegistryDTO) throws UpdateException {
		final String mname = "updateDisciplineRegistry";
		LOGGER.debug("entering " + mname);

		if (updatedDisciplineRegistryDTO == null)
			throw new UpdateException("Discipline registry object is null");

		// Checks if the object exists
		if (!disciplineRegistryRepository.findById(updatedDisciplineRegistryDTO.getId()).isPresent())
			throw new UpdateException("Discipline registry must exist to be updated");

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		DisciplineRegistry updatedDisciplineRegistry = commonModelMapper.map(updatedDisciplineRegistryDTO, DisciplineRegistry.class);
		// Updates the object
		disciplineRegistryRepository.save(updatedDisciplineRegistry);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DisciplineRegistryDTO> findDisciplineRegistries() throws FinderException {
		final String mname = "findDisciplineRegistries";
		LOGGER.debug("entering " + mname);

		// Finds all the objects
		final Iterable<DisciplineRegistry> disciplineRegistries = disciplineRegistryRepository.findAll();
		int size;
		if ((size = ((Collection<DisciplineRegistry>) disciplineRegistries).size()) == 0) {
			throw new FinderException("No discipline in the database");
		}
		List<DisciplineRegistryDTO> disciplineRegistryDTOs = ((List<DisciplineRegistry>) disciplineRegistries).stream()
				.map(disciplineRegistry -> commonModelMapper.map(disciplineRegistry, DisciplineRegistryDTO.class)).collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return disciplineRegistryDTOs;
	}

	@Override
	public List<UserDTO> findCoachs() throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Discipline findByDisciplineName(String disciplineName) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}
	
	// =====================================
			// =    EventRegistry Business methods    =
			// =====================================
	@Override
	@Transactional
	public EventRegistryDTO createEventRegistry(@Valid EventRegistryDTO eventRegistryDTO)
			throws CreateException {
		final String mname = "createEventRegistry";
		LOGGER.debug("entering " + mname);

		if (eventRegistryDTO == null || eventRegistryDTO.getId() == null || eventRegistryDTO.getId().equals(""))
			throw new CreateException("Event registry object is null");

		try {
			if (findDisciplineRegistry(eventRegistryDTO.getId()) != null)
				throw new DuplicateKeyException();
		} catch (FinderException e) {
		}

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		EventRegistry eventRegistry = commonModelMapper.map(eventRegistryDTO, EventRegistry.class);

		// Creates the object
		eventRegistryRepository.save(eventRegistry);

		LOGGER.debug("exiting " + mname);
		return eventRegistryDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public EventRegistryDTO findEventRegistry(final String eventRegistryId) throws FinderException {
		final String mname = "findDiscipline";
		LOGGER.debug("entering " + mname + " for id " + eventRegistryId);

		checkId(eventRegistryId);

		EventRegistry eventRegistry = null;
		if (!disciplineRegistryRepository.findById(eventRegistryId).isPresent())
			throw new FinderException("Event must exist to be found");
		else
			eventRegistry = eventRegistryRepository.findById(eventRegistryId).get();

		LOGGER.debug("exiting " + mname);
		return commonModelMapper.map(eventRegistry, EventRegistryDTO.class);
	}

	@Override
	@Transactional
	public void deleteEventRegistry(final String eventRegistryId) throws FinderException, RemoveException {
		final String mname = "deleteEventRegistry";
		LOGGER.debug("entering " + mname + " for id " + eventRegistryId);

		checkId(eventRegistryId);

		EventRegistry eventRegistry = null;
		if (!eventRegistryRepository.findById(eventRegistryId).isPresent())
			throw new RemoveException("Event registry must exist to be deleted");
		else
			eventRegistry = eventRegistryRepository.findById(eventRegistryId).get();
		// Deletes the object
		eventRegistryRepository.delete(eventRegistry);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional
	public void updateEventRegistry(@Valid final EventRegistryDTO updatedEventRegistryDTO) throws UpdateException {
		final String mname = "updateEventRegistry";
		LOGGER.debug("entering " + mname);

		if (updatedEventRegistryDTO == null)
			throw new UpdateException("Event registry object is null");

		// Checks if the object exists
		if (!eventRegistryRepository.findById(updatedEventRegistryDTO.getId()).isPresent())
			throw new UpdateException("Event registry must exist to be updated");

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		EventRegistry updatedEventRegistry = commonModelMapper.map(updatedEventRegistryDTO, EventRegistry.class);
		// Updates the object
		eventRegistryRepository.save(updatedEventRegistry);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EventRegistryDTO> findEventRegistries() throws FinderException {
		final String mname = "findEventRegistries";
		LOGGER.debug("entering " + mname);

		// Finds all the objects
		final Iterable<EventRegistry> eventRegistries = eventRegistryRepository.findAll();
		int size;
		if ((size = ((Collection<EventRegistry>) eventRegistries).size()) == 0) {
			throw new FinderException("No event in the database");
		}
		List<EventRegistryDTO> eventRegistryDTOs = ((List<EventRegistry>)eventRegistries).stream()
				.map(eventRegistry -> commonModelMapper.map(eventRegistry, EventRegistryDTO.class)).collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return eventRegistryDTOs;
	}
	
	// ======================================
    // =          Private Methods           =
    // ======================================
	

	private void checkId(final String id) throws FinderException {
		if (id == null || id.equals(""))
			throw new FinderException(id + " should not be null or empty");
	}

}