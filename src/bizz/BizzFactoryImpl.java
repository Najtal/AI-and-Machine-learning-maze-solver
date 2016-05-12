package bizz;

import constant.NodeCondition;
import ucc.NodeDTO;

import java.util.ArrayList;
import java.util.Map;

/**
 * Class generating bizz objects
 */
public class BizzFactoryImpl implements BizzFactory {

	private static BizzFactoryImpl instance;

	/**
	 * To get the singleton instance of BizzFactoryImpl
	 * @return the instance singleton of BizzFactoryImpl
     */
	public static BizzFactory getInstance() {
		if (instance == null)
			instance = new BizzFactoryImpl();
		return instance;
	}

	/**
	 * private empty controller
	 */
	private BizzFactoryImpl() {
	}


    @Override
    public Maze createMaze(final Node startNode, final int sizex, final int sizey,
                           int[][] mazeStructure, int[][] mazeKeys, int[][] mazeDoors) {
        return new MazeImpl(startNode, sizex, sizey, mazeStructure, mazeKeys, mazeDoors);
    }

    @Override
    public Maze createNinjaMaze(Node startNode, int sizex, int sizey) {
        return new MazeImpl(startNode, sizex, sizey);
    }


    @Override
    public Node createNode(final int posx, final int posy, final int isDoor, int hasKey,
                           ArrayList<NodeDTO> neighbours, Map<NodeDTO, NodeCondition> neighboursHasCondition) {
        return new NodeImpl(posx, posy, isDoor, hasKey, neighbours, neighboursHasCondition);
    }

    @Override
    public Node createNode(int posx, int posy) {
        return new NodeImpl(posx, posy);
    }
}
