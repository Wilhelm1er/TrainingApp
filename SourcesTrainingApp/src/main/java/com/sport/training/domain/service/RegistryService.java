package com.sport.training.domain.service;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import com.sport.training.domain.dto.BookmarkDTO;
import com.sport.training.domain.dto.DisciplineDTO;
import com.sport.training.domain.dto.DisciplineRegistryDTO;
import com.sport.training.domain.dto.EventRegistryDTO;
import com.sport.training.domain.dto.NotationDTO;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.RemoveException;
import com.sport.training.exception.UpdateException;
import com.sport.training.authentication.domain.dto.UserDTO;


public interface RegistryService  {
	// ======================================
    // = DisciplineRegistry Business methods=
    // ======================================
	public DisciplineRegistryDTO createDisciplineRegistry(@Valid final DisciplineRegistryDTO disciplineRegistryDTO) throws CreateException, FinderException ;
    public DisciplineRegistryDTO findDisciplineRegistry(final Long disciplineRegistryId) throws FinderException ;
    public void deleteDisciplineRegistry(final Long disciplineRegistryId) throws FinderException, RemoveException ;
    public void updateDisciplineRegistry(@Valid final DisciplineRegistryDTO disciplineRegistryDTO) throws UpdateException ;
    public List<DisciplineRegistryDTO> findDisciplineRegistries() throws FinderException ;
    public Set<UserDTO> findCoachsByDiscipline(final String disciplineId) throws FinderException ;
    public Set<DisciplineDTO> findDisciplinesByCoach(final String coachId) throws FinderException;
    
    // ======================================
    // =   EventRegistry Business methods   =
    // ======================================
	public EventRegistryDTO createEventRegistry(@Valid final EventRegistryDTO eventRegistryDTO) throws CreateException ;
    public EventRegistryDTO findEventRegistry(final Long eventRegistryId) throws FinderException ;
    public void deleteEventRegistry(final Long eventRegistryId) throws FinderException, RemoveException ;
    public void updateEventRegistry(@Valid final EventRegistryDTO eventRegistryDTO) throws UpdateException ;
    public List<EventRegistryDTO> findEventRegistries() throws FinderException ;

    // ======================================
    // =   Notation Business methods   =
    // ======================================
	public NotationDTO createNotation(@Valid final NotationDTO notationDTO) throws CreateException ;
    public NotationDTO findNotation(final Long notationId) throws FinderException ;
    public void deleteNotation(final Long notationId) throws FinderException, RemoveException ;
    public void updateNotation(@Valid final NotationDTO notationDTO) throws UpdateException ;
    public List<NotationDTO> findNotations() throws FinderException ;
	
    // ======================================
    // =   Bookmark Business methods   =
    // ======================================
	public BookmarkDTO createBookmark(@Valid final BookmarkDTO bookmarkDTO) throws CreateException ;
    public BookmarkDTO findBookmark(final Long bookmarkId) throws FinderException ;
    public void deleteBookmark(final Long bookmarkId) throws FinderException, RemoveException ;
    public void updateBookmark(@Valid final BookmarkDTO bookmarkDTO) throws UpdateException ;
    public List<BookmarkDTO> findBookmarks() throws FinderException ;
	
}