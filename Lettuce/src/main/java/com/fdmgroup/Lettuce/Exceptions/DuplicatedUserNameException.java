package com.fdmgroup.Lettuce.Exceptions;

public class DuplicatedUserNameException extends Exception {

	public DuplicatedUserNameException() {
		super("User name is duplicated.");
	}

	public DuplicatedUserNameException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DuplicatedUserNameException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicatedUserNameException(String message) {
		super(message);
	}

	public DuplicatedUserNameException(Throwable cause) {
		super(cause);
	}

	
}
