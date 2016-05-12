package bizz;

import java.util.Map;

/**
 */
public class MazeImpl extends BizzObjectImpl implements Maze {

	// Maze graph solver approach
	private Node startNode;
	private int nbKey;
	private Map<Integer,Node> keyPosition;
	private int nbdoor;
	private Map<Integer,Node> doorPosition;

	// Maze gui approach
	private int[][] mazeStructure;
	private int[][] mazeKeys;
	private int[][] mazeDoors;
	private int sizex;
	private int sizey;


	/**
	 * Maze for ninja (they only know the startpoint
	 * @param startNode
	 * @param sizex
     * @param sizey
     */
	public MazeImpl(final Node startNode, final int sizex, final int sizey) {
		super();
		this.startNode = startNode;
		this.sizex = sizex;
		this.sizey = sizey;
	}

	/**
	 * Maze for gods (they know everything
	 * @param startNode
	 * @param sizex
	 * @param sizey
	 * @param mazeStructure
	 * @param mazeKeys
     * @param mazeDoors
     */
	public MazeImpl(final Node startNode, final int sizex, final int sizey,
					int[][] mazeStructure, int[][] mazeKeys, int[][] mazeDoors) {
		super();
		this.startNode = startNode;
		this.sizex = sizex;
		this.sizey = sizey;
		this.mazeStructure = mazeStructure;
		this.mazeKeys = mazeKeys;
		this.mazeDoors = mazeDoors;

	}



	/**
	 * Constructeur vide
	 */
	public MazeImpl() {
	}


	/*
	 * GETTERS AND SETTERS
	 */

	public Node getStartNode() {
		return startNode;
	}

	public void setStartNode(Node startNode) {
		this.startNode = startNode;
	}

	public int getNbKey() {
		return nbKey;
	}

	public void setNbKey(int nbKey) {
		this.nbKey = nbKey;
	}

	public Map<Integer, Node> getKeyPosition() {
		return keyPosition;
	}

	public void setKeyPosition(Map<Integer, Node> keyPosition) {
		this.keyPosition = keyPosition;
	}

	public int getNbdoor() {
		return nbdoor;
	}

	public void setNbdoor(int nbdoor) {
		this.nbdoor = nbdoor;
	}

	public Map<Integer, Node> getDoorPosition() {
		return doorPosition;
	}

	public void setDoorPosition(Map<Integer, Node> doorPosition) {
		this.doorPosition = doorPosition;
	}

	public int[][] getMazeStructure() {
		return mazeStructure;
	}

	public void setMazeStructure(int[][] mazeStructure) {
		this.mazeStructure = mazeStructure;
	}

	public int[][] getMazeKeys() {
		return mazeKeys;
	}

	public void setMazeKeys(int[][] mazeKeys) {
		this.mazeKeys = mazeKeys;
	}

	public int[][] getMazeDoors() {
		return mazeDoors;
	}

	public void setMazeDoors(int[][] mazeDoors) {
		this.mazeDoors = mazeDoors;
	}

	public int getSizex() {
		return sizex;
	}

	public void setSizex(int sizex) {
		this.sizex = sizex;
	}

	public int getSizey() {
		return sizey;
	}

	public void setSizey(int sizey) {
		this.sizey = sizey;
	}
}
