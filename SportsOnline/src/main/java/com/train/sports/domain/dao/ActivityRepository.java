package com.train.sports.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.train.sports.domain.model.Discipline;
import com.train.sports.domain.model.Activity;

public interface ActivityRepository extends CrudRepository<Activity, String> {

	Iterable<Activity> findAllByDiscipline(Discipline discipline);

}
