package exception;

import app.AppContext;

import java.util.logging.Level;

/**
 * Created by marion on 13/06/16.
 */
public class MyTimeException extends Exception{

    /**
     * Constructor
     */
    public MyTimeException() {
    }

    /**
     * Constructor
     * Log the message of the error
     * @param message 	The exception message
     */
    public MyTimeException(final String message) {
        super(message);
        AppContext.INSTANCE.getLogger().log(Level.SEVERE, message);
    }

    /**
     * Constructor
     * Log the origin of the error
     * @param origin 	The exception at origin of the error
     */
    public MyTimeException(final Throwable origin) {
        super(origin);
        AppContext.INSTANCE.getLogger().log(Level.SEVERE, origin.toString());
    }

    /**
     * Constructor
     * Log the origin of the error
     * @param message 	The error message
     * @param origin		The exception at origin of the error
     */
    public MyTimeException(final String message, final Throwable origin) {
        super(message, origin);
        AppContext.INSTANCE.getLogger().log(Level.SEVERE, message + "\n" + origin.toString());
    }
}

