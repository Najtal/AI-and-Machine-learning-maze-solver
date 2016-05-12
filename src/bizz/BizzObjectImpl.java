package bizz;

/**
 */
public abstract class BizzObjectImpl implements BizzObject {
	
	private int version;

	/**
	 * BizzObject constructor
	 * @param version		version number
	 */
	public BizzObjectImpl(final int version) {
		this.version = version;
	}
		
	/**
	 * empty constructor
	 */
	public BizzObjectImpl() {
		super();
	}


	/**
	 * get object version
	 * @return le numéro de version
	 */
	public final int getVersion() {
		return version;
	}
	
	/**
	 * set the version number
	 * @param newVersion le nouveau numero de version 
	 */
	public final void setVersion(final int newVersion) {
		this.version = newVersion;
	}

}
