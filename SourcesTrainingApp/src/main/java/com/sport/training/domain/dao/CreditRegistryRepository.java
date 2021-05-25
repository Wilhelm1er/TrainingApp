package com.sport.training.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.model.CreditRegistry;

public interface CreditRegistryRepository extends CrudRepository<CreditRegistry, Long> {

	Iterable<CreditRegistry> findAllByUser(User user);

}
