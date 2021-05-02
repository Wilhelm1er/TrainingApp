package com.sport.training.domain.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.sport.training.domain.model.Event;
import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.model.Activity;

public interface EventAthletesRepository extends CrudRepository<Event, String> {

	//Iterable<Event> findAllByUser(User athlete);

}
