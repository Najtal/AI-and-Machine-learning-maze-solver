package ucc;

import bizz.BizzFactory;

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

    @Override
    public MazeDTO getNinjaMazeFromOmniscientMaze(MazeDTO maze) {
        MazeDTO mNinja = BizzFactory.INSTANCE.createNinjaMaze(maze.getStartNode(), maze.getSizex(), maze.getSizey());
        return mNinja;
    }

}
