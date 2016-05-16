package exception;

import java.util.logging.Level;

import app.AppContext;

public class NoKeyException extends Exception{

	/**
	 * constructor
	 * (exception Unchecked).
	 */
	public NoKeyException() {
	}

	/**
	 * constructor
	 * (exception Unchecked).
	 * @param message	the message of the error
	 */
	public NoKeyException(final String message) {
		super(message);
	}

	/**
	 * constructor
	 * (exception Unchecked).
	 * @param origin		The exception at the origin
	 */
	public NoKeyException(final Throwable origin) {
		super(origin);
	}

	/**
	 * constructor
	 * (exception Unchecked).
	 * @param message	the message of the error
	 * @param origin		The exception at the origin
	 */
	public NoKeyException(final String message, final Throwable origin) {
		super(message, origin);
	}
}
