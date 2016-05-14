package bizz;

import constant.NodeCondition;
import ucc.NodeDTO;

import java.util.ArrayList;
import java.util.Map;

/**
 * Interface permettant la création d'objets
 * @author BOUREZ Philippe, LINS Sébastien, REYNERS Gaetan, STREEL Xavier
 * @version 1.1
 */
public interface BizzFactory {

	BizzFactory INSTANCE = BizzFactoryImpl.getInstance();

	/**
	 *	Create Maze Omniscient (full model)
	 * @param startNode
	 * @param sizex
	 * @param sizey
	 * @param mazeStructure
	 * @param mazeKeys
     * @param mazeDoors
     * @return
     */
	Maze createMaze(final NodeDTO startNode, final int sizex, final int sizey,
					int[][] mazeStructure, int[][] mazeKeys, int[][] mazeDoors);


	/**
	 *	Create Maze Ninja
	 * @param startNode
	 * @param sizex
	 * @param sizey
     */
	Maze createNinjaMaze(final NodeDTO startNode, final int sizex, final int sizey);

	/**
	 * Create new Node
	 * @param posx
	 * @param posy
	 * @param isDoor
	 * @param hasKey
	 * @param neighbours
	 * @param neighboursHasCondition
     * @return
     */
	Node createNode(final int posx, final int posy, final int isDoor, int hasKey,
					ArrayList<NodeDTO> neighbours, Map<NodeDTO, NodeCondition> neighboursHasCondition);

	/**
	 *
	 * @param posx
	 * @param posy
     * @return
     */
	Node createNode(final int posx, final int posy);

	/**
	 * Create a new Goal Load
	 * @param loadDiscoverPath
	 * @param loadGrabKey
	 * @param loadOpenDoor
	 * @param loadReachGoal
	 * @param loadAaction
     * @return
     */
	GoalLoad createGoal(int loadDiscoverPath, int loadGrabKey, int loadOpenDoor, int loadReachGoal, int loadAaction);

}
