package com.sport.training.domain.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.model.Discipline;
import com.sport.training.domain.model.DisciplineRegistry;

public interface DisciplineRegistryRepository extends CrudRepository<DisciplineRegistry, Long> {

	Iterable<DisciplineRegistry> findAllByDiscipline(Discipline discipline);

	Iterable<DisciplineRegistry> findAllByCoach(User coach);

	DisciplineRegistry findByCoachAndDiscipline(User user, Discipline discipline);

	@Query("select MAX(id) from DisciplineRegistry d")
	public Optional<Long> findLastId();

	@Query("SELECT d.discipline FROM DisciplineRegistry d WHERE d.coach= :coach")
	public Iterable<Discipline> findDisciplinesByCoach(@Param("coach") User coach);

}
