package com.train.sports.authentication.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.train.sports.authentication.domain.dao.RoleRepository;
import com.train.sports.authentication.domain.model.Role;
import com.train.sports.exception.FinderException;

@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	RoleRepository roleRepository;

	@Override
	public Role findByRoleName(String roleName) throws FinderException {
		Role role;
		if( (role=roleRepository.findByName(roleName))==null)
			throw new FinderException("role unknown");
		return role;
	}

}
