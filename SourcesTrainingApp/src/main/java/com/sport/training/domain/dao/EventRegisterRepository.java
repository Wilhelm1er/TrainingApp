package com.sport.training.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.model.Event;
import com.sport.training.domain.model.EventRegister;

public interface EventRegisterRepository extends CrudRepository<EventRegister, String> {

	//Iterable<EventRegister> findAllByUser(User athlete);

}
