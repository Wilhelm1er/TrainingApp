package com.sport.training.authentication.domain.service;

import java.util.List;

import javax.validation.Valid;

import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.authentication.domain.model.Role;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.RemoveException;
import com.sport.training.exception.UpdateException;

public interface UserService {

	 public UserDTO createUser(@Valid final UserDTO userDTO) throws CreateException, FinderException;

	    public UserDTO findUser(final String userId) throws FinderException;

	    public void deleteUser(final String userId) throws RemoveException, FinderException;

	    public void updateUser(@Valid final UserDTO userDTO) throws UpdateException, FinderException;

	    public List<UserDTO> findUsers() throws FinderException;

		public List<UserDTO> findUsersByRole(Role role) throws FinderException;

}
