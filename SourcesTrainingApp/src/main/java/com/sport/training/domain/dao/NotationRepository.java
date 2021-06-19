package com.sport.training.domain.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sport.training.domain.model.Event;
import com.sport.training.domain.model.Notation;

public interface NotationRepository extends CrudRepository<Notation, Long> {

	Iterable<Notation> findAllByEvent(Event event);
	
	@Query("select MAX(id) from Notation n")
	public Optional<Long> findLastId();

}
