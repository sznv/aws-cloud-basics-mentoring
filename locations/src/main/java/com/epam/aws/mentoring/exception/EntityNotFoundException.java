package com.epam.aws.mentoring.exception;

public class EntityNotFoundException extends AppRuntimeException {

	public EntityNotFoundException() {
	}

	public EntityNotFoundException(String message) {
		super(message);
	}

	public EntityNotFoundException(Throwable cause) {
		super(cause);
	}
}
