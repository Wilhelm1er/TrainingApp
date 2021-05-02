package com.sport.training.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.model.Discipline;
import com.sport.training.domain.model.Event;

public interface DisciplineCoachsRepository extends CrudRepository<Discipline, String> {
	//Iterable<Event> findAllByDiscipline(User coachs);
}
