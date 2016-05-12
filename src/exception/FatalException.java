package exception;

import app.AppContext;

import java.util.logging.Level;

/**
 * Classe of exceptions for fatal errors in the program
 */
public class FatalException extends RealException {

	/**
	 * Constructor
	 */
	public FatalException() {
	}

	/**
	 * Constructor
	 * Log the message of the error
	* @param message 	The exception message
	 */
	public FatalException(final String message) {
		super(message);
		AppContext.INSTANCE.getLogger().log(Level.SEVERE, message);
	}

	/**
	 * Constructor
 	 * Log the origin of the error
 	 * @param origin 	The exception at origin of the error
	 */
	public FatalException(final Throwable origin) {
		super(origin);
		AppContext.INSTANCE.getLogger().log(Level.SEVERE, origin.toString());
	}

	/**
	 * Constructor
	 * Log the origin of the error
	 * @param message 	The error message
	 * @param origin		The exception at origin of the error
	 */
	public FatalException(final String message, final Throwable origin) {
		super(message, origin);
		AppContext.INSTANCE.getLogger().log(Level.SEVERE, message + "\n" + origin.toString());
	}
}
