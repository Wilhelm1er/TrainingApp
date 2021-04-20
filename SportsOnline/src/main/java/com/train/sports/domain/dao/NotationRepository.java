package com.train.sports.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.train.sports.domain.model.Event;
import com.train.sports.domain.model.Notation;

public interface NotationRepository extends CrudRepository<Notation, String> {

	Iterable<Notation> findAllByEvent(Event event);

}
