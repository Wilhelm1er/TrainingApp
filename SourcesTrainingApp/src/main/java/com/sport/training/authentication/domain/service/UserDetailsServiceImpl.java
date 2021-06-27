package com.sport.training.authentication.domain.service;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.exception.FinderException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	
	@Autowired
	private UserService userService;

	@Override
	public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final String mname = "loadUserByUsername";
        LOGGER.debug("entering "+mname);
		UserDTO userDTO = null;
		try {
			userDTO = userService.findUser(username);
		} catch (FinderException e) {
			LOGGER.error("exception in "+mname+" : "+e.getMessage());
			e.printStackTrace();
		}
		if (userDTO == null) {
			LOGGER.debug("exiting "+mname+" with exception");
			throw new UsernameNotFoundException("User not found");
		} else {
			List<SimpleGrantedAuthority> grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(userDTO.getRoleName()));
			LOGGER.debug("exiting "+mname);
			return new CustomUserDetails(userDTO.getUsername(), userDTO.getPassword(), grantedAuthorities, userDTO.getCredit(), userDTO.getStatut());
		}
		
		
	}
}