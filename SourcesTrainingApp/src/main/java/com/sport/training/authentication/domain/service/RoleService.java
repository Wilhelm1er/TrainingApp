package com.sport.training.authentication.domain.service;

import com.sport.training.authentication.domain.model.Role;
import com.sport.training.exception.FinderException;

public interface RoleService {
	
	public Role findByRoleName(String roleName) throws FinderException;

}
