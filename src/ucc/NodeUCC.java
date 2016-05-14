package ucc;

public interface NodeUCC {
	
	NodeUCC INSTANCE = NodeUCCImpl.getInstance();

	void addNeighbour(NodeDTO node, NodeDTO neighbour);

}
