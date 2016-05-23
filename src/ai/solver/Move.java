package ai.solver;

import ucc.NodeDTO;

public class Move implements Action{

	private NodeDTO destination;
	
	public Move(NodeDTO dest){
		this.destination = dest;
	}
	
	public NodeDTO getDestination() {
		return this.destination;
	}
	
	public String toString() {
		return "Move to (" + destination.getPosx()+","+destination.getPosy()+")";
	}
}
