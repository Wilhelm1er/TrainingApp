package com.sport.training.authentication.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.sport.training.authentication.domain.model.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {
	
	public Role findByName(String name);
}
