package com.train.sports.authentication.domain.service;

import java.util.List;

import javax.validation.Valid;

import com.train.sports.authentication.domain.dto.UserDTO;
import com.train.sports.authentication.domain.model.Role;
import com.train.sports.exception.CreateException;
import com.train.sports.exception.FinderException;
import com.train.sports.exception.RemoveException;
import com.train.sports.exception.UpdateException;

public interface UserService {

	 public UserDTO createUser(@Valid final UserDTO userDTO) throws CreateException, FinderException;

	    public UserDTO findUser(final String userId) throws FinderException;

	    public void deleteUser(final String userId) throws RemoveException, FinderException;

	    public void updateUser(@Valid final UserDTO userDTO) throws UpdateException, FinderException;

	    public List<UserDTO> findUsers() throws FinderException;

		public List<UserDTO> findUsersByRole(Role role) throws FinderException;

}
