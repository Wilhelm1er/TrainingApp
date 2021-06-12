package com.sport.training.domain.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.web.multipart.MultipartFile;

import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.dto.BookmarkDTO;
import com.sport.training.domain.dto.DiscussionDTO;
import com.sport.training.domain.dto.FileDTO;
import com.sport.training.domain.dto.MessageDTO;
import com.sport.training.domain.dto.NotationDTO;
import com.sport.training.domain.model.File;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.RemoveException;
import com.sport.training.exception.UpdateException;

public interface FileStorageService {

	// ======================================
	// = File Business methods =
	// ======================================
	
	public File store(MultipartFile file, UserDTO userDTO) throws IOException ;
	
	public Set<String> getAllFileNamesByUser(String userId) throws FinderException;

	public File getFile(String name) throws FinderException;

	public Set<UserDTO> getAllUsers() throws FinderException;
	
}