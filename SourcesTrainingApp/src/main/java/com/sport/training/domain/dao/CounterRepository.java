package com.sport.training.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.sport.training.domain.model.Counter;

public interface CounterRepository extends CrudRepository<Counter, String> {

}
