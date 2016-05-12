package ucc;

/**
 * Use case cntroller over RSSI
 */
public class MazeUCCImpl implements MazeUCC {

    static MazeUCCImpl instance;

    /**
     * To get the singleton instance of RssiUCCImpl
     * @return the instance singleton of RssiUCCImpl
     */
    public static MazeUCC getInstance() {
        if (instance == null)
            instance = new MazeUCCImpl();
        return instance;
    }

    /**
     * private empty controller
     */
    private MazeUCCImpl() {
    }


}
