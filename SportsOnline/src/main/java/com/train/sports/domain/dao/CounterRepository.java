package com.train.sports.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.train.sports.domain.model.Counter;

public interface CounterRepository extends CrudRepository<Counter, String> {

}
