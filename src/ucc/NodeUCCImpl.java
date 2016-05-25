package ucc;

/**
 * Use case controller over RSSI
 */
public class NodeUCCImpl implements NodeUCC {

    static NodeUCCImpl instance;

    /**
     * To get the singleton instance of RssiUCCImpl
     * @return the instance singleton of RssiUCCImpl
     */
    public static NodeUCC getInstance() {
        if (instance == null)
            instance = new NodeUCCImpl();
        return instance;
    }

    /**
     * private empty controller
     */
    private NodeUCCImpl() {
    }


    @Override
    public void addNeighbour(NodeDTO node, NodeDTO neighbour) {
        if (node.getNeighbours().contains(neighbour)) return;

        node.getNeighbours().add(neighbour);
        node.getNeighboursHasCondition().put(neighbour, neighbour.getCondition());
    }

}
