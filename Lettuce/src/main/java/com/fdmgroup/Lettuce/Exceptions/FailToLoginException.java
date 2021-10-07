package com.fdmgroup.Lettuce.Exceptions;

public class FailToLoginException extends Exception {

	public FailToLoginException() {
		super("fail to log in because of invalid email or password");
		// TODO Auto-generated constructor stub
	}

	public FailToLoginException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public FailToLoginException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public FailToLoginException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public FailToLoginException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
