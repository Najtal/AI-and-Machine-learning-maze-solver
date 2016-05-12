package ucc;


import bizz.Node;

import java.util.Map;

/**
 * Interface de gestion des tomes
 * @author BOUREZ Philippe, LINS Sébastien, REYNERS Gaetan, STREEL Xavier
 * @version 1.1
 */
public interface MazeDTO {

    Node getStartNode();

    void setStartNode(Node startNode);

    int getNbKey();

    void setNbKey(int nbKey);

    Map<Integer, Node> getKeyPosition();

    void setKeyPosition(Map<Integer, Node> keyPosition);

    int getNbdoor();

    void setNbdoor(int nbdoor);

    Map<Integer, Node> getDoorPosition();

    void setDoorPosition(Map<Integer, Node> doorPosition);

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

}
