package ai.solver;

import ucc.NodeDTO;

public enum KeyAction implements Action{
	TAKE_KEY,
	DROP_KEY;

	@Override
	public NodeDTO getDestination() {
		return null;
	}
}
