package com.train.sports.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.train.sports.domain.model.Discipline;

public interface DisciplineRepository extends CrudRepository<Discipline, String> {

}
