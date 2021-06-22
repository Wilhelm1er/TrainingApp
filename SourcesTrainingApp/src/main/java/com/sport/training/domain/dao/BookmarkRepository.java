package com.sport.training.domain.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.model.Bookmark;

public interface BookmarkRepository extends CrudRepository<Bookmark, Long> {

	Iterable<Bookmark> findAllByAthlete(User athlete);
	
	@Query("select MAX(id) from Bookmark b")
	public Optional<Long> findLastId();
}
