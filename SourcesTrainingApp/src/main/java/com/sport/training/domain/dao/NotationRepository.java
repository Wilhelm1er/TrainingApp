package com.sport.training.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.sport.training.domain.model.Event;
import com.sport.training.domain.model.Notation;

public interface NotationRepository extends CrudRepository<Notation, Long> {

	Iterable<Notation> findAllByEvent(Event event);

}
