package com.sport.training.domain.dao;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.model.Discipline;
import com.sport.training.domain.model.File;

public interface FileRepository extends CrudRepository<File, Long> {

	@Query("SELECT d.id FROM File d WHERE d.name= :name")
	public Long findFileIdByName(@Param("name") String name);
	
	@Query("SELECT d.user FROM File d ")
	public Set<User> findAllUsers();
	
	@Query("SELECT d.name FROM File d WHERE d.user= :user")
	public Iterable<String> findAllFileNamesByUser(@Param("user") User user);
	

}
