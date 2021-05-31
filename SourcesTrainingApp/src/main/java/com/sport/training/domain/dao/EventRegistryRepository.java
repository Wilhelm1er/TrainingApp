package com.sport.training.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.model.EventRegistry;

public interface EventRegistryRepository extends CrudRepository<EventRegistry, Long> {

	Iterable<EventRegistry> findAllByAthlete(User athlete);

}
