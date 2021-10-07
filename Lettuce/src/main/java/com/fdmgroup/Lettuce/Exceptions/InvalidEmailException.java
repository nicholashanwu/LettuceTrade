package com.fdmgroup.Lettuce.Exceptions;

public class InvalidEmailException extends Exception {
	
	public InvalidEmailException() {
		super("User name doesn't exist.");
	}

	public InvalidEmailException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidEmailException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidEmailException(String message) {
		super(message);
	}

	public InvalidEmailException(Throwable cause) {
		super(cause);
	}
	

}
