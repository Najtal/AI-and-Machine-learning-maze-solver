package ucc;


import java.util.Map;

public interface MazeDTO {

    NodeDTO getStartNode();

    void setStartNode(NodeDTO startNode);

    int getNbKey();

    void setNbKey(int nbKey);

    Map<Integer, NodeDTO> getKeyPosition();

    void setKeyPosition(Map<Integer, NodeDTO> keyPosition);

    int getNbdoor();

    void setNbdoor(int nbdoor);

    Map<Integer, NodeDTO> getDoorPosition();

    void setDoorPosition(Map<Integer, NodeDTO> doorPosition);

    int[][] getMazeStructure();

    void setMazeStructure(int[][] mazeStructure);

    int[][] getMazeKeys();

    void setMazeKeys(int[][] mazeKeys);

    int[][] getMazeDoors();

    void setMazeDoors(int[][] mazeDoors);

    int getSizex();

    void setSizex(int sizex);

    int getSizey();

    void setSizey(int sizey);

    NodeDTO getGoalNode();

    void setGoalNode(NodeDTO goalNode);

    NodeDTO getSolverPosition();

    void setSolverPosition(NodeDTO solverPosition);

    NodeDTO[] getSolverkeys();

    void setSolverkeys(NodeDTO[] solverkeys);

    void removeSolverkey(int key);

    void addSolverkey(NodeDTO solverkey, int key);

    int getSolverCarriedKey();

    void setSolverCarriedKey(int key);

    NodeDTO[] getKeysAtStart();

    void setKeysAtStart(NodeDTO[] keysLocation);

    void addKeyAtStart(int k, NodeDTO keyLocation);

    boolean checkMazeValidity();
}
