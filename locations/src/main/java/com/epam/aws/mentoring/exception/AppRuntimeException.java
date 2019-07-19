package com.epam.aws.mentoring.exception;

/**
 * Created by Andrei_Seliazniou on 7/16/2019.
 */
public class AppRuntimeException extends RuntimeException {

	public AppRuntimeException(String message) {
		super(message);
	}

	public AppRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppRuntimeException(Throwable cause) {
		super(cause);
	}
}
