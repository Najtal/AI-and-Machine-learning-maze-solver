package util;

import app.AppContext;

import java.util.logging.Level;

/**
 * Created by jvdur on 10/05/2016.
 */
public class Log {


    /**
     * Log an information message
     * @param msg
     */
    public static void logInfo(String msg) {
        AppContext.INSTANCE.getLogger().log(Level.INFO, msg);
        System.out.print("Logger-INFO : " + msg + "\n");
    }


    /**
     * Log the message of an event that has been handled
     * @param msg The message to be logged
     */
    public static void logFine(String msg) {
        AppContext.INSTANCE.getLogger().log(Level.FINE, msg);
        System.out.print("Logger-FINE : " + msg + "\n");
    }

    /**
     * Log a warning message. Event not completely handled
     * @param msg The message to be logged
     */
    public static void logWarning(String msg) {
        AppContext.INSTANCE.getLogger().log(Level.WARNING, msg);
        System.out.print("Logger-WARNING : " + msg + "\n");
    }

    /**
     * Log the message of a severe problem that could lead to a Fatal exception.
     * @param msg The message to be logged
     */
    public static void logSevere(String msg) {
        AppContext.INSTANCE.getLogger().log(Level.SEVERE, msg);
        System.out.print("Logger-SEVERE : " + msg + "\n");
    }





}
