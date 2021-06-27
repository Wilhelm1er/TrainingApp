package com.sport.training.domain.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.model.Discipline;
import com.sport.training.domain.model.DisciplineUser;

public interface DisciplineUserRepository extends CrudRepository<DisciplineUser, Long> {

	Iterable<DisciplineUser> findAllByDiscipline(Discipline discipline);

	Iterable<DisciplineUser> findAllByCoach(User coach);

	DisciplineUser findByCoachAndDiscipline(User user, Discipline discipline);

	@Query("select MAX(id) from DisciplineUser d")
	public Optional<Long> findLastId();

	@Query("SELECT d.discipline FROM DisciplineUser d WHERE d.coach= :coach")
	public Iterable<Discipline> findDisciplinesByCoach(@Param("coach") User coach);

}
