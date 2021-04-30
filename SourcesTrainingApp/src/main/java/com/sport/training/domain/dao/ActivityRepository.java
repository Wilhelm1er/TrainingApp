package com.sport.training.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.sport.training.domain.model.Discipline;
import com.sport.training.domain.model.Activity;

public interface ActivityRepository extends CrudRepository<Activity, String> {

	Iterable<Activity> findAllByDiscipline(Discipline discipline);

}
