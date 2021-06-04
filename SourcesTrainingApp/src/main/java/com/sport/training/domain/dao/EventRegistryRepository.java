package com.sport.training.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.model.Event;
import com.sport.training.domain.model.EventRegistry;

public interface EventRegistryRepository extends CrudRepository<EventRegistry, Long> {

	Iterable<EventRegistry> findAllByUser(User user);
	
	EventRegistry findByUserAndEvent(User user, Event event);

	Iterable<EventRegistry> findAllByEvent(Event event);

}
