package com.sport.training.domain.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.sport.training.domain.model.Event;
import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.model.Activity;
import com.sport.training.domain.model.Discipline;

public interface EventRepository extends CrudRepository<Event, Long> {

	Iterable<Event> findAllByActivity(Activity activity);
	
	@Query("select distinct e from Event e where upper(e.id) like upper(concat('%', :keyword,'%')) or upper(e.name) like upper(concat('%', :keyword,'%'))")
	Iterable<Event> findByIdOrNameContaining(@Param("keyword") String keyword);

	Iterable<Event> findAllByCoach(User coach);
}
