package com.sport.training.domain.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.model.Event;
import com.sport.training.domain.model.EventUser;

public interface EventUserRepository extends CrudRepository<EventUser, Long> {

	Iterable<EventUser> findAllByUser(User user);

	EventUser findByUserAndEvent(User user, Event event);

	Iterable<EventUser> findAllByEvent(Event event);
	
	@Query("select MAX(id) from EventUser e")
	public Optional<Long> findLastId();

}
