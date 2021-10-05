package com.fdmgroup.Lettuce.Exceptions;

public class InvalidUserNameException extends Exception {
	
	public InvalidUserNameException() {
		super("User name doesn't exist.");
	}

	public InvalidUserNameException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidUserNameException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidUserNameException(String message) {
		super(message);
	}

	public InvalidUserNameException(Throwable cause) {
		super(cause);
	}
	

}
