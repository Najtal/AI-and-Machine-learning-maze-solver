package ai.solver;

import ucc.NodeDTO;

public class Action {

	private typeAction TypeAction;
	private NodeDTO destination;
	private int key;

	public Action(typeAction type, NodeDTO destination, int key){
		this.destination = destination;
		this.key = key;
		this.TypeAction = type;
	}

	public typeAction getTypeAction() { return this.TypeAction; }

	public NodeDTO getDestination() {
		return this.destination;
	}

	public int getKey() { return this.key; }

	public String toString() {
		if (TypeAction == typeAction.MOVE) return "Move to " + destination;
		else if (TypeAction == typeAction.DROP_KEY) return "Drop key "+key+" at " + destination;
		else return "Take key "+key+" at " + destination;
	}

}
