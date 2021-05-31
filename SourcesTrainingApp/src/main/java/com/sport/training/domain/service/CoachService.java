package com.sport.training.domain.service;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import com.sport.training.domain.dto.BookmarkDTO;
import com.sport.training.domain.dto.DisciplineDTO;
import com.sport.training.domain.dto.NotationDTO;
import com.sport.training.exception.CreateException;
import com.sport.training.exception.FinderException;
import com.sport.training.exception.RemoveException;
import com.sport.training.exception.UpdateException;

public interface CoachService {

	// ======================================
	// = Notation Business methods =
	// ======================================
	public NotationDTO createNotation(@Valid final NotationDTO notationDTO) throws CreateException;

	public NotationDTO findNotation(final Long notationId) throws FinderException;

	public void deleteNotation(final Long notationId) throws FinderException, RemoveException;

	public void updateNotation(@Valid final NotationDTO notationDTO) throws UpdateException;

	public List<NotationDTO> findNotations() throws FinderException;

	// ======================================
	// = Bookmark Business methods =
	// ======================================
	public BookmarkDTO createBookmark(@Valid final BookmarkDTO bookmarkDTO) throws CreateException;

	public BookmarkDTO findBookmark(final Long bookmarkId) throws FinderException;

	public void deleteBookmark(final Long bookmarkId) throws FinderException, RemoveException;

	public void updateBookmark(@Valid final BookmarkDTO bookmarkDTO) throws UpdateException;

	public List<BookmarkDTO> findBookmarks() throws FinderException;
	
	public List<BookmarkDTO> findBookmarksByAthlete(final String athleteId) throws FinderException;

}