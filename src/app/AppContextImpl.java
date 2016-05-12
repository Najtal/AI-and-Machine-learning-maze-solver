package app;

import exception.FatalException;
import util.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 */
class AppContextImpl implements AppContext {

	/**
	 * Create property
	 */
	private static Properties props = new Properties();

	/**
	 * Create the handler and logger file
	 */
	private static FileHandler file;
	private static Logger log;
	private static final String ADRESSE_FICHIER = "log.txt";

	static {
		try {
			file = new FileHandler(ADRESSE_FICHIER, true);
			file.setEncoding("UTF-8");
			file.setFormatter(new SimpleFormatter());
			log = Logger.getLogger("log");
			log.setUseParentHandlers(false);
			log.addHandler(file);
		} catch (IOException ioe) {
			file = null;
			System.out.print("Error while opening the log file");
			ioe.printStackTrace();
		}
	}

	/**
	 * Load context and config
	 * @param context 	The app context
	 */
	public final void loadAndConfig(final String context) {
		FileInputStream input;
		try {
			input = new FileInputStream(context);
			props.load(input);
		} catch (IOException e) {
			throw new FatalException("Cannot load the prop file", e);
		}

	}

	/**
	 * Get a prop value
	 * @param key 	the research key
	 * @return 		the research value
	 */
	public final String getProperty(final String key) {
		return props.getProperty(key);
	}

	/**
	 * Get logger
	 * @return le logger
	 */
	public final Logger getLogger() {
		return log;
	}

	/**
	 * Load utilities
	 */
	public void loadUtilities() {

	}

	/**
	 * Close the application
	 */
	public final void closeApplication() {
		Log.logInfo("Application close");
		file.close();
	}
}
