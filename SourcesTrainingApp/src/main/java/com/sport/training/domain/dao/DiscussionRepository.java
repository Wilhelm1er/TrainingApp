package com.sport.training.domain.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.model.Discipline;
import com.sport.training.domain.model.Discussion;

public interface DiscussionRepository extends CrudRepository<Discussion, Long> {
	
	Iterable <Discussion> findAllByUser(User user);
	
	@Query("select MAX(id) from Discussion d")
	public Optional<Long> findLastId();	
	
}
