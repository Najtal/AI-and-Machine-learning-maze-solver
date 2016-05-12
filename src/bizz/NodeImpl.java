package bizz;


import constant.NodeCondition;
import ucc.NodeDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class NodeImpl extends BizzObjectImpl implements Node {

	private int posx;
	private int posy;
	private int isDoor;
    private boolean isDoorOpen;
	private int hasKey; // If < 1 means has no key. Otherwise, key number
    private boolean isGoal;
	private List<NodeDTO> neighbours;
	private Map<NodeDTO, NodeCondition> neighboursHasCondition;


    /**
     *
     * @param posx
     * @param posy
     * @param isDoor
     * @param hasKey
     * @param neighbours
     * @param neighboursHasCondition
     */
	public NodeImpl(final int posx, final int posy, final int isDoor, int hasKey,
                    ArrayList<NodeDTO> neighbours, Map<NodeDTO, NodeCondition> neighboursHasCondition) {
		super();
        this.posx = posx;
        this.posy = posy;
        this.isDoor = isDoor;
        this.hasKey = hasKey;
        this.neighbours = neighbours;
        this.neighboursHasCondition = neighboursHasCondition;
	}

    /**
     *
     * @param posx
     * @param posy
     */
    public NodeImpl(final int posx, final int posy) {
        super();
        this.posx = posx;
        this.posy = posy;
        this.isDoor = 0;
        this.hasKey = 0;
        this.neighbours = new ArrayList<>();
        this.neighboursHasCondition = new HashMap<>();
    }


    @Override
    public NodeCondition getCondition() {
        if (this.isDoor > 0)
            return NodeCondition.NEED_KEY;
        return NodeCondition.NONE;
    }

    /*
     * GETTERS
     */
    @Override
    public int getPosx() {
        return posx;
    }

    @Override
    public int getPosy() {
        return posy;
    }

    @Override
    public int getIsDoor() {
        return isDoor;
    }

    @Override
    public int getHasKey() {
        return hasKey;
    }

    @Override
    public List<NodeDTO> getNeighbours() {
        return neighbours;
    }

    @Override
    public Map<NodeDTO, NodeCondition> getNeighboursHasCondition() {
        return neighboursHasCondition;
    }

    @Override
    public boolean isDoorOpen() {
        return isDoorOpen;
    }

    /*
     * SETTERS
     */

    @Override
    public void setHasKey(int hasKey) {
        this.hasKey = hasKey;
    }

    @Override
    public void setDoorOpen(boolean doorOpen) {
        isDoorOpen = doorOpen;
    }

    @Override
    public void setGoal() {
        this.isGoal = true;
    }

    public void setIsDoor(int isDoor) {
        this.isDoor = isDoor;
    }

}
