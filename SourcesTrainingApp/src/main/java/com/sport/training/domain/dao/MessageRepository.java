package com.sport.training.domain.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sport.training.authentication.domain.model.User;
import com.sport.training.domain.model.Discussion;
import com.sport.training.domain.model.Message;

public interface MessageRepository extends CrudRepository<Message, Long> {

	Message findBySenderAndRecipient(User sender, User recipient);

	Iterable<Message> findAllByDiscussion(Discussion discussion);

	@Query("select MAX(id) from Event e")
	public Optional<Long> findLastId();

}
