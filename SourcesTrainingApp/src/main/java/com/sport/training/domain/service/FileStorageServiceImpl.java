package com.sport.training.domain.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import com.sport.training.authentication.domain.dao.UserRepository;
import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.dao.FileRepository;
import com.sport.training.domain.dto.FileDTO;
import com.sport.training.domain.model.File;
import com.sport.training.exception.FinderException;

/**
 * This class is a facade for all user services.
 */

@Service
@Validated
public class FileStorageServiceImpl implements FileStorageService {

	// ======================================
	// = Attributes =
	// ======================================
	@Autowired
	private ModelMapper fileModelMapper, userModelMapper;

	@Autowired
	private FileRepository fileRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageServiceImpl.class);

	// =====================================
	// = Constructors =
	// =====================================
	public FileStorageServiceImpl() {
	}

	// ======================================
	// = File Business methods =
	// ======================================

	@Override
	public File store(MultipartFile fileSend, UserDTO userDTO) throws IOException {
		
		String fileName = StringUtils.cleanPath(fileSend.getOriginalFilename());
		System.out.println("fileName: "+fileName);
		FileDTO fileDTO = new FileDTO(fileName, fileSend.getContentType(), userDTO, fileSend.getBytes());

		// :::::::::::::::: We change DTO to model ::::::::::::::: //
		File file = fileModelMapper.map(fileDTO, File.class);

		// Creates the object
		fileRepository.save(file);

		return fileRepository.save(file);
	}

	@Override
	public File getFile(String name) throws FinderException{
		final String mname = "getFile";
		LOGGER.debug("entering " + mname);
		
		Long id;
		
		id=fileRepository.findFileIdByName(name);
		
		LOGGER.debug("exiting " + mname);
		return fileRepository.findById(id).get();
	}

	@Override
	public Set<String> getAllFileNamesByUser(String userId) throws FinderException {
		final String mname = "getAllFileNamesByUser";
		LOGGER.debug("entering " + mname);

		checkStringId(userId);

		User user = null;
		if (!userRepository.findById(userId).isPresent())
			throw new FinderException("User must exist to be found");
		else
			user = userRepository.findById(userId).get();

		// Finds all the objects
		final Iterable<String> fileNames = fileRepository.findAllFileNamesByUser(user);
		int size;
		if ((size = ((Collection<String>) fileNames).size()) == 0) {
			throw new FinderException("No file names in the database");
		}
		
		Set<String> names = new HashSet<String>();
		
		for(String l : fileNames) {
			names.add(l);
		}
		
		int size2;
		if ((size2 = (names).size()) == 0) {
			throw new FinderException("No file names in the database");
		}

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return names;
	}
	
	@Override
	public Set<UserDTO> getAllUsers() throws FinderException {
		final String mname = "getAllUsers";
		LOGGER.debug("entering " + mname);

		// Finds all the objects
		final Set<User> users = fileRepository.findAllUsers();
		int size;
		if ((size = ((Collection<User>) users).size()) == 0) {
			throw new FinderException("No file names in the database");
		}
		
		Set<UserDTO> userDTOs = (users.stream().map(user -> userModelMapper.map(user, UserDTO.class))
				.collect(Collectors.toSet()));

		LOGGER.debug("exiting " + mname + " size of collection : " + size);
		return userDTOs;
	}
	
	// ======================================
	// = Private Methods =
	// ======================================

	private void checkStringId(final String id) throws FinderException {
		if (id == null || id.equals(""))
			throw new FinderException(id + " should not be null or empty");
	}

}
