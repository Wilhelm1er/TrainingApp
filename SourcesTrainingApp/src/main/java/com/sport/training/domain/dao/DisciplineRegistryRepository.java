package com.sport.training.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.model.Discipline;
import com.sport.training.domain.model.DisciplineRegistry;

public interface DisciplineRegistryRepository extends CrudRepository<DisciplineRegistry, Long> {

	// Iterable<Discipline> findAllByUser(User user);
	Iterable<DisciplineRegistry> findAllByDiscipline(Discipline discipline);

	Iterable<DisciplineRegistry> findAllByCoach(User coach);

}
