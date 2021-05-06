package com.sport.training.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.sport.training.domain.model.Discipline;

public interface DisciplineRepository extends CrudRepository<Discipline, String> {
	
	public Discipline findByName(String name);
}
