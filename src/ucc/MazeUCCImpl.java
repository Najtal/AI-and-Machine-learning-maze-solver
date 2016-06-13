package ucc;

import bizz.BizzFactory;
import constant.NodeCondition;

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
        if (maze.getKeysAtStart() == null) maze.setKeysAtStart(new NodeDTO[maze.getNbKey()+1]);
        NodeDTO[] keys = maze.getKeysAtStart();
        cleanNodes(maze.getGoalNode(), null, keys);
        maze.setSolverCarriedKey(0);
        maze.setSolverkeys(new NodeDTO[maze.getNbKey()+1]);
        for (int i=0; i< keys.length; i++){
            if (keys[i] != null) keys[i].setHasKey(i+1);
        }
    }

    private void cleanNodes(NodeDTO node, NodeDTO father, NodeDTO[] keys) {
        node.setUsefulNeighbour(new ArrayList<>());
        if (node.getIsDoor() != 0) node.setCondition(NodeCondition.NEED_KEY);
        if (node.getHasKey() != 0 && keys[node.getHasKey()-1] != null && keys[node.getHasKey()-1] != node){
            node.setHasKey(0);
        }
        for (NodeDTO son : node.getNeighbours()) {
            if (son != father) {
                cleanNodes(son, node, keys);
            }
        }
    }
}
