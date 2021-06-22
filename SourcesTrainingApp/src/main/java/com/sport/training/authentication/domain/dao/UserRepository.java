package com.sport.training.authentication.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.sport.training.authentication.domain.model.Role;
import com.sport.training.authentication.domain.model.User;

public interface UserRepository extends CrudRepository<User, String> {

	public Iterable<User> findAllByRole(Role role);

}
