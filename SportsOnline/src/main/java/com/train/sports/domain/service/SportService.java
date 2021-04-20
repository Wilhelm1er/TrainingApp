package com.train.sports.domain.service;

import java.util.List;

import javax.validation.Valid;

import com.train.sports.domain.dto.DisciplineDTO;
import com.train.sports.domain.dto.EventDTO;
import com.train.sports.domain.dto.ActivityDTO;
import com.train.sports.exception.CreateException;
import com.train.sports.exception.FinderException;
import com.train.sports.exception.RemoveException;
import com.train.sports.exception.UpdateException;


public interface SportService  {
	// ======================================
    // =     Discipline Business methods    =
    // ======================================
	public DisciplineDTO createDiscipline(@Valid final DisciplineDTO disciplineDTO) throws CreateException ;
    public DisciplineDTO findDiscipline(final String disciplineId) throws FinderException ;
    public void deleteDiscipline(final String disciplineId) throws FinderException, RemoveException ;
    public void updateDiscipline(@Valid final DisciplineDTO disciplineDTO) throws UpdateException ;
    public List<DisciplineDTO> findDisciplines() throws FinderException ;

    // ======================================
    // =      Activity Business methods     =
    // ======================================
    public ActivityDTO createActivity(@Valid final ActivityDTO activityDTO) throws CreateException ;
    public ActivityDTO findActivity(final String activityId) throws FinderException ;
    public void deleteActivity(final String activityId) throws FinderException, RemoveException ;
    public void updateActivity(@Valid final ActivityDTO activityDTO) throws UpdateException ;
    public List<ActivityDTO> findActivities() throws FinderException ;
    public List<ActivityDTO> findActivities(String disciplineId) throws FinderException;

    // ======================================
    // =       Event Business methods       =
    // ======================================
    public List<EventDTO> searchEvents(String keyword) throws FinderException ;
    public EventDTO createEvent(@Valid final EventDTO eventDTO) throws CreateException ;
    public EventDTO findEvent(final String eventId) throws FinderException ;
    public void deleteEvent(final String eventId) throws FinderException, RemoveException;
    public void updateEvent(@Valid final EventDTO eventDTO) throws UpdateException ;
    public List<EventDTO> findEvents() throws FinderException ;
    public List<EventDTO> findEvents(String activityId) throws FinderException ;

}
