package ucc;


import constant.NodeCondition;

import java.util.List;
import java.util.Map;

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
    
    boolean isGoal();

    /*
     * SETTERS
     */
    void setHasKey(int hasKey);

    void setDoorOpen(boolean doorOpen);

    void setGoal();

    void setIsDoor(int isDoor);

}
