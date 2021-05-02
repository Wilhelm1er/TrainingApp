package com.sport.training.authentication.domain.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.sport.training.authentication.domain.dao.UserRepository;
import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.model.Role;
import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.dao.DisciplineRepository;
import com.sport.training.domain.dto.ActivityDTO;
import com.sport.training.domain.dto.DisciplineDTO;
import com.sport.training.domain.model.Activity;
import com.sport.training.domain.model.Discipline;
import com.sport.training.domain.service.CreditCardService;
import com.sport.training.domain.service.SportService;
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
public class UserServiceImpl implements UserService {

	// ======================================
	// = Attributes =
	// ======================================

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private DisciplineRepository disciplineRepository;
	
	@Autowired
	private SportService sportService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private CreditCardService creditCardService;

	@Autowired
	private ModelMapper userModelMapper, userDTOModelMapper;

	// ======================================
	// = Constructors =
	// ======================================
	public UserServiceImpl() {
	}

	// ======================================
	// = Business methods =
	// ======================================
	@Override
	@Transactional
	public UserDTO createUser(@Valid UserDTO userDTO) throws CreateException, FinderException {
		final String mname = "createUser";
		LOGGER.debug("entering " + mname);

		if (userDTO == null)
			throw new CreateException("User object is null");

		try {
			if (findUser(userDTO.getUsername()) != null)
				throw new DuplicateKeyException();
		} catch (FinderException e) {
		}

		// Credit Card Check
		if (userDTO.getCreditCardNumber() != null && userDTO.getCreditCardExpiryDate() != null
				&& !userDTO.getCreditCardNumber().equals("") && !userDTO.getCreditCardExpiryDate().equals(""))
			creditCardService.verifyCreditCard(userDTO.getCreditCardDTO());

		if (userDTO.getPassword().length() < 4)
			throw new CreateException("password's length exception (mini. of 4 char. required)");

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		User user = userModelMapper.map(userDTO, User.class);

		// Encrypting the password before storing it in the database
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

		// make sure that the user has a role
		if (user.getRole() == null || user.getRole().getName() == null) {
			try {
				user.setRole(roleService.findByRoleName("ROLE_USER"));
			} catch (FinderException e) {
				LOGGER.error("role missing for user");
			}
		} else if (user.getRole().getId() == null || user.getRole().getId() == 0) {
			try {
				user.setRole(roleService.findByRoleName(user.getRole().getName()));
			} catch (FinderException e) {
				LOGGER.error("role missing for user");
			}
		}
		// Creates the object
		userRepository.save(user);

		UserDTO result = userDTOModelMapper.map(user, UserDTO.class);

		LOGGER.debug("exiting " + mname);
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDTO findUser(final String username) throws FinderException {
		final String mname = "findUser";
		LOGGER.debug("entering " + mname + " with " + username);

		checkId(username);
		// Finds the object
		User user = null;
		if (!userRepository.findById(username).isPresent())
			throw new FinderException("User must exist to be found");
		else
			user = userRepository.findById(username).get();

		UserDTO result = userModelMapper.map(user, UserDTO.class);
		LOGGER.debug("exiting " + mname);
		return result;
	}

	@Override
	@Transactional
	public void deleteUser(final String username) throws RemoveException, FinderException {
		final String mname = "deleteUser";
		LOGGER.debug("entering " + mname + " with " + username);
		User user;
		checkId(username);
		if (!userRepository.findById(username).isPresent())
			throw new RemoveException("User must exist to be deleted");
		else
			user = userRepository.findById(username).get();

		userRepository.delete(user);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional
	public void updateUser(@Valid UserDTO userDTO) throws UpdateException, FinderException {
		final String mname = "updateUser";
		LOGGER.debug("entering " + mname);

		if (userDTO == null)
			throw new UpdateException("User object is null");
		if (userDTO.getPassword().length() < 4)
			throw new UpdateException("password's length exception (mini. of 4 char. required)");

		// Checks if the object exists
		if (!userRepository.findById(userDTO.getUsername()).isPresent())
			throw new UpdateException("User must exist to be updated");

		// Credit Card Check
		if (userDTO.getCreditCardNumber() != null && userDTO.getCreditCardExpiryDate() != null
				&& !userDTO.getCreditCardNumber().equals("") && !userDTO.getCreditCardExpiryDate().equals(""))
			creditCardService.verifyCreditCard(userDTO.getCreditCardDTO());

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		User user = userModelMapper.map(userDTO, User.class);
		// Role must be set
		User userToUpdate = userRepository.findById(userDTO.getUsername()).get();
		user.setRole(userToUpdate.getRole());

		// Encrypting the password before storing it in the database
		user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

		// Updates the object
		userRepository.save(user);
		LOGGER.debug("exiting " + mname);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDTO> findUsers() throws FinderException {
		final String mname = "findUsers";
		LOGGER.debug("entering " + mname);

		// Finds all the objects
		final Iterable<User> users = userRepository.findAll();
		int size;
		if ((size = ((Collection<User>) users).size()) == 0) {
			throw new FinderException("No user in the database");
		}
		List<UserDTO> usersDTO = ((List<User>) users)
								.stream()
								.map(user -> userDTOModelMapper.map(user, UserDTO.class))
								.collect(Collectors.toList());
		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return usersDTO;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDTO> findUsersByRole(final Role role) throws FinderException {
		final String mname = "findUsersByRole";
		LOGGER.debug("entering " + mname);

		// Finds all the objects
		final Iterable<User> usersByRole = userRepository.findAllByRole(role);
		int size;
		if ((size = ((Collection<User>) usersByRole).size()) == 0) {
			throw new FinderException("No user in the database for role " + role.getName());
		}
		List<UserDTO> usersDTOByRole = ((List<User>) usersByRole)
										.stream()
										.map(user -> userDTOModelMapper.map(user, UserDTO.class))
										.collect(Collectors.toList());
		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return usersDTOByRole;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<UserDTO> findUsersByDiscipline(final String disciplineId) throws FinderException {
		final String mname = "findUsersByDiscipline";
		LOGGER.debug("entering " + mname);

		checkId(disciplineId);

		Discipline discipline = null;
		if (!disciplineRepository.findById(disciplineId).isPresent())
			throw new FinderException("Discipline must exist to be found");
		else
			discipline = disciplineRepository.findById(disciplineId).get();

		// Finds all the objects
		final Iterable<User> coachsByDiscipline = userRepository.findAllByDiscipline(discipline);
		
		int size;
		if ((size = ((Collection<User>) coachsByDiscipline).size()) == 0) {
			throw new FinderException("No Coach in the database");
		}

		List<UserDTO> coachDTOs = ((List<User>) coachsByDiscipline)
										.stream()
										.map(coach -> userModelMapper.map(coach, UserDTO.class))
										.collect(Collectors.toList());

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return coachDTOs;
	}

	// ======================================
	// = Private Methods =
	// ======================================

	private void checkId(final String id) throws FinderException {
		if (id == null || id.equals(""))
			throw new FinderException("Id should not be null or empty");
	}

}
