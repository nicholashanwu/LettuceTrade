package com.fdmgroup.Lettuce.Exceptions;

public class DuplicatedEmailException extends Exception {

	public DuplicatedEmailException() {
		super("The Email is duplicated.");
	}

	public DuplicatedEmailException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DuplicatedEmailException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicatedEmailException(String message) {
		super(message);
	}

	public DuplicatedEmailException(Throwable cause) {
		super(cause);
	}

	
}
