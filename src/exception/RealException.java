package exception;

/**
 * Real exception class (run time exceptions)
 */
public class RealException extends RuntimeException {

	/**
	 * constructor
	 * (exception Unchecked).
	 */
	public RealException() {
	}

	/**
	 * constructor
	 * (exception Unchecked).
	 * @param message	the message of the error
	 */
	public RealException(final String message) {
		super(message);
	}

	/**
	 * constructor
	 * (exception Unchecked).
	 * @param origin		The exception at the origin
	 */
	public RealException(final Throwable origin) {
		super(origin);
	}

	/**
	 * constructor
	 * (exception Unchecked).
	 * @param message	the message of the error
	 * @param origin		The exception at the origin
	 */
	public RealException(final String message, final Throwable origin) {
		super(message, origin);
	}
}
