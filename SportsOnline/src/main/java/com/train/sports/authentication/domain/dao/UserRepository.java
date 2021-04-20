package com.train.sports.authentication.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.train.sports.authentication.domain.model.Role;
import com.train.sports.authentication.domain.model.User;

public interface UserRepository extends CrudRepository<User, String> {
	
	public Iterable<User> findAllByRole(Role role);

}
