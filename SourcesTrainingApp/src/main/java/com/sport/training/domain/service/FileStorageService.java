package com.sport.training.domain.service;

import java.io.IOException;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.domain.model.File;
import com.sport.training.exception.FinderException;

public interface FileStorageService {

	// ======================================
	// = File Business methods =
	// ======================================

	public File store(MultipartFile file, UserDTO userDTO) throws IOException;

	public Set<String> getAllFileNamesByUser(String userId) throws FinderException;

	public File getFile(String name) throws FinderException;

	public Set<UserDTO> getAllUsers() throws FinderException;

}