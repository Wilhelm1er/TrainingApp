package com.sport.training.exception;

/**
 * This exception is thrown when an object cannot be deleted.
 */
@SuppressWarnings("serial")
public class RemoveException extends ApplicationException {

	public RemoveException(final String message) {
		super(message);
	}
}
