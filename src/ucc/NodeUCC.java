package ucc;

/**
 * Interface de gestion des Use Case concernants les tomes
 * 
 * @author BOUREZ Philippe, LINS Sébastien, REYNERS Gaetan, STREEL Xavier
 * @version 1.1
 */
public interface NodeUCC {
	
	NodeUCC INSTANCE = NodeUCCImpl.getInstance();

	void addNeighbour(NodeDTO node, NodeDTO neighbour);

}
