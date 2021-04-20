package com.train.sports.authentication.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.train.sports.authentication.domain.model.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {
	
	public Role findByName(String name);
}
