package com.sport.training.authentication.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.sport.training.authentication.domain.model.Role;
import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.model.Discipline;

public interface UserRepository extends CrudRepository<User, String> {
	
	public Iterable<User> findAllByRole(Role role);
	
	public Iterable<User> findAllByDiscipline(Discipline discipline);

}
