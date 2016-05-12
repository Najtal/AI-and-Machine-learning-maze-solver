package app;

import java.util.logging.Logger;

/**
 *
 */
public interface AppContext {
	
	AppContext INSTANCE = new AppContextImpl();
	
	String PRODUCTION = "prod.properties";
	String TESTS = "test.properties";

	/**
	 *  Load application context
	 *  @param context	The load context : PRODUCTION or TESTS
	 */
	void loadAndConfig(String context);

	/**
	 * Get the logger
	 * @return logger
	 */
	Logger getLogger();
	
	/**
	 * Close application
	 */
	void closeApplication();

	/**
	 * Load utilities
	 */
	void loadUtilities();
	
	/**
	 * Get prop value
	 * @param key	the key
	 * @return 		the value
	 */
	String getProperty(String key);

}
