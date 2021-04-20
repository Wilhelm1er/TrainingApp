package com.train.sports.authentication.domain.service;

import com.train.sports.authentication.domain.model.Role;
import com.train.sports.exception.FinderException;

public interface RoleService {
	
	public Role findByRoleName(String roleName) throws FinderException;

}
