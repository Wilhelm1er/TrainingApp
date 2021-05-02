package com.sport.training.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.sport.training.domain.model.Bookmark;

public interface BookmarkRepository extends CrudRepository<Bookmark, String> {

	//Iterable<Bookmark> findBookmarkByUserId(User user);
}
