package ucc;

import bizz.BizzFactory;

import java.util.ArrayList;
import java.util.HashMap;

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


    public MazeDTO getNinjaMazeFromOmniscientMaze(MazeDTO maze) {
        MazeDTO mNinja = BizzFactory.INSTANCE.createNinjaMaze(maze.getStartNode(), maze.getSizex(), maze.getSizey());
        mNinja.setDoorPosition(new HashMap<Integer, NodeDTO>());
        mNinja.setKeyPosition(new HashMap<Integer, NodeDTO>());
        mNinja.setMazeStructure(new int[maze.getSizex()][maze.getSizey()]);
        return mNinja;
    }

    @Override
    public void clean(MazeDTO maze) {
        cleanNodes(maze.getGoalNode(), null);
    }

    private void cleanNodes(NodeDTO node, NodeDTO father) {
        node.setUsefulNeighbour(new ArrayList<>());
        for (NodeDTO son : node.getNeighbours()) {
            if (son != father) {
                cleanNodes(son, node);
            }
        }
    }
}
