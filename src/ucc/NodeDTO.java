package ucc;


import constant.NodeCondition;

import java.util.List;
import java.util.Map;

/**
 * Interface de gestion des tomes
 * @author BOUREZ Philippe, LINS Sébastien, REYNERS Gaetan, STREEL Xavier
 * @version 1.1
 */
public interface NodeDTO {


    NodeCondition getCondition();

    /*
     * GETTERS
     */
    int getPosx();

    int getPosy();

    int getIsDoor();

    int getHasKey();

    List<NodeDTO> getNeighbours();

    Map<NodeDTO, NodeCondition> getNeighboursHasCondition();

    boolean isDoorOpen();

    /*
     * SETTERS
     */
    void setHasKey(int hasKey);

    void setDoorOpen(boolean doorOpen);

    void setGoal();

    void setIsDoor(int isDoor);

}
