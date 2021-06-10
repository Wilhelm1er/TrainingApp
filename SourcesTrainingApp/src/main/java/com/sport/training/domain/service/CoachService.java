package com.sport.training.domain.service;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import com.sport.training.authentication.domain.dto.UserDTO;
import com.sport.training.domain.dto.BookmarkDTO;
import com.sport.training.domain.dto.DisciplineDTO;
import com.sport.training.domain.dto.DiscussionDTO;
import com.sport.training.domain.dto.MessageDTO;
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

	// ======================================
	// = Discussion Business methods =
	// ======================================
	public DiscussionDTO createDiscussion(@Valid final DiscussionDTO discussionDTO) throws CreateException;

	public DiscussionDTO findDiscussion(final Long discussionId) throws FinderException;

	public void deleteDiscussion(final Long discussionId) throws FinderException, RemoveException;

	public void updateDiscussion(@Valid final DiscussionDTO discussionDTO) throws UpdateException;

	public List<DiscussionDTO> findDiscussions() throws FinderException;

	public List<DiscussionDTO> findDiscussionsByUser(String userId) throws FinderException;

	// ======================================
	// = Message Business methods =
	// ======================================
	public MessageDTO createMessage(@Valid final MessageDTO messageDTO) throws CreateException;

	public MessageDTO findMessage(final Long messageId) throws FinderException;

	public void deleteMessage(final Long messageId) throws FinderException, RemoveException;

	public void updateMessage(@Valid final MessageDTO messageDTO) throws UpdateException;

	public List<MessageDTO> findMessages() throws FinderException;

	List<MessageDTO> findMessagesByDiscussion(Long discussionId) throws FinderException;

}