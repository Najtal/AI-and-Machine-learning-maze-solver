package bizz;

import ucc.NodeDTO;

import java.util.Map;

/**
 */
public class MazeImpl extends BizzObjectImpl implements Maze {

	// Maze graph solver approach
	private NodeDTO startNode;
	private NodeDTO goalNode;
	private int nbKey;
	private Map<Integer,NodeDTO> keyPosition;
	private int nbdoor;
	private Map<Integer,NodeDTO> doorPosition;

	// Maze gui approach
	private int[][] mazeStructure;
	private int[][] mazeKeys;
	private int[][] mazeDoors;
	private int sizex;
	private int sizey;

	private NodeDTO solverPosition;
	private NodeDTO[] solverkeys;
	private int solverCarriedKey;
	private NodeDTO[] keysAtStart;


	/**
	 * Maze for ninja (they only know the startpoint
	 * @param startNode
	 * @param sizex
     * @param sizey
     */
	protected MazeImpl(final NodeDTO startNode, final int sizex, final int sizey) {
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
	protected MazeImpl(final NodeDTO startNode, final int sizex, final int sizey,
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

	public NodeDTO getStartNode() {
		return startNode;
	}

	public void setStartNode(NodeDTO startNode) {
		this.startNode = startNode;
	}

	public int getNbKey() {
		return nbKey;
	}

	public void setNbKey(int nbKey) {
		this.nbKey = nbKey;
	}

	public Map<Integer, NodeDTO> getKeyPosition() {
		return keyPosition;
	}

	public void setKeyPosition(Map<Integer, NodeDTO> keyPosition) {
		this.keyPosition = keyPosition;
	}

	public int getNbdoor() {
		return nbdoor;
	}

	public void setNbdoor(int nbdoor) {
		this.nbdoor = nbdoor;
	}

	public Map<Integer, NodeDTO> getDoorPosition() {
		return doorPosition;
	}

	public void setDoorPosition(Map<Integer, NodeDTO> doorPosition) {
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

	public NodeDTO getGoalNode() {
		return goalNode;
	}

	public void setGoalNode(NodeDTO goalNode) {
		this.goalNode = goalNode;
	}

	public int getSolverCarriedKey() { return this.solverCarriedKey; }

	public void setSolverCarriedKey(int k) { this.solverCarriedKey = k; }

	@Override
	public NodeDTO[] getKeysAtStart() { return this.keysAtStart; }

	@Override
	public void setKeysAtStart(NodeDTO[] keysLocation) { this.keysAtStart = keysLocation; }

	@Override
	public void addKeyAtStart(int k, NodeDTO keyLocation) { this.keysAtStart[k-1] = keyLocation; }

	public NodeDTO[] getSolverkeys() {
		return solverkeys;
	}

	public void setSolverkeys(NodeDTO[] solverkeys) {
		this.solverkeys = solverkeys;
	}

	public void removeSolverkey(int key) { this.solverkeys[key-1] = null; }

	public void addSolverkey(NodeDTO solverkey, int key) { this.solverkeys[key-1] = solverkey; }

	public NodeDTO getSolverPosition() {
		return solverPosition;
	}

	public void setSolverPosition(NodeDTO solverPosition) {
		this.solverPosition = solverPosition;
	}

	public boolean checkMazeValidity() { return true; }
}
