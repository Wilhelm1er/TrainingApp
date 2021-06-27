package com.sport.training.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.model.CreditUser;

public interface CreditUserRepository extends CrudRepository<CreditUser, Long> {

	Iterable<CreditUser> findAllByUser(User user);

}
