package ai.solver;

import bizz.Node;
import constant.NodeCondition;
import ucc.GoalDTO;
import ucc.MazeDTO;
import ucc.NodeDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jvdur on 13/05/2016.
 */
public class SolverImpl implements Solver {

	private Node pos;
	private int key;
	private double new_rwd;
	private double door_rwd;
	private double key_rwd;
	private double gamma;
	private List<NodeDTO> visited;
	
	
	/*
	 * SETTERS
	 */	
	private void takeKey() throws Exception{
		int k = this.pos.getHasKey();
		if (this.key != 0 || k == 0){
			throw new Exception();
		}
		this.pos.setHasKey(0);
		this.key = k;
	}
	
	private void dropKey() throws Exception{
		if (this.key == 0 || this.pos.getHasKey() != 0){
			throw new Exception();
		}
		this.pos.setHasKey(this.key);
		this.key = 0;
	}
	
	private void move(Node destination){
		this.pos = destination;
	}
	    
	
	/**
     *
     * @param maze
     */
    public SolverImpl(MazeDTO maze, GoalDTO goals) {

        /**
         * The MazeDTO received only contains the
         *          maze.getStartNode()
         *          maze.getSizex()
         *          maze.getSizey()
         */

		this.new_rwd = goals.getLoadDiscoverPath();
		this.door_rwd = goals.getLoadOpenDoor();
		this.key_rwd = goals.getLoadGrabKey();

		this.gamma = 1;
		this.visited = new ArrayList<NodeDTO>();

    }

    @Override
    public boolean isSolved() {
        return this.pos.isGoal();
    }

    @Override
    public void doOneStep() {
    	try {
			bestMove(this.pos, 0, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    
    // boucles ? ex : droite, gauche, droite, gauche, etc
    private double bestMove(NodeDTO node, int nsteps, double reward) throws Exception{
    	if (visited.contains(node)==false){
    		visited.add(node);
    		if (node.getIsDoor()==0){
    			return reward + new_rwd*Math.pow(gamma,nsteps);
    		}else{
    			return reward + door_rwd*Math.pow(gamma,nsteps);
    		}
    	}
    	
    	else{
    		if ((this.key==0 && node.getHasKey()==0)
    				|| (this.key!=0 && node.getHasKey()!=0)){
    			// Move in the best direction
    			List<NodeDTO> ngbs = node.getNeighbours();
    			List<Number> l = bestNeighbour(ngbs, nsteps, reward);
    			double max = ((double) l.get(1));
    			int best_ngb = ((int) l.get(0));
				move((Node) ngbs.get(best_ngb));
				return max;
    		}
    		
    		if (this.key==0 && node.getHasKey()!=0){
    			// Move in the best direction with or without taking the key
    			List<NodeDTO> ngbs = node.getNeighbours();
    			
    			// Without the key
    			List<Number> l = bestNeighbour(ngbs, nsteps, reward);
    			double max = ((double) l.get(1));
    			int best_ngb = ((int) l.get(0));
    			
    			//With the key
    			takeKey();
    			double new_reward = reward+key_rwd*Math.pow(gamma, nsteps);
    			List<Number> l2 = bestNeighbour(ngbs, nsteps+1, new_reward);
    			double max2 = ((double) l2.get(1));
    			int best_ngb2 = ((int) l2.get(0));
    			
    			//Is it better to take the key ? 
    			if (max > max2){
    				dropKey();
    				move((Node) ngbs.get(best_ngb));
    				return max;
    			}else{
    				move((Node) ngbs.get(best_ngb2));
    				return max2;
    			}
    		}
    		
    		else if (node.getHasKey()==0 && this.key != 0){
    			// Move in the best direction with or without dropping the key
    			List<NodeDTO> ngbs = node.getNeighbours();

    			// Keeping the key
    			List<Number> l = bestNeighbour(ngbs, nsteps, reward);
    			double max = ((double) l.get(1));
    			int best_ngb = ((int) l.get(0));
    			
    			// Dropping the key
    			dropKey();
    			List<Number> l2 = bestNeighbour(ngbs, nsteps+1, reward);
    			double max2 = ((double) l2.get(1));
    			int best_ngb2 = ((int) l2.get(0));

    			// Is it better to drop the key ?
    			if (max > max2){
    				takeKey();
    				move((Node) ngbs.get(best_ngb));
    				return max;
    			}else{
    				move((Node) ngbs.get(best_ngb2));
    				return max2;
    			}	
    		}
    	}
    	throw new Exception();
    }
    
    //Find the best direction 
    private List<Number> bestNeighbour(List<NodeDTO> ngbs, int nsteps, double reward) throws Exception{
    	double max = bestMove(ngbs.get(0), nsteps+1, reward);
		int best_ngb = 0;
		for (int i = 1; i < ngbs.size() ; i++){
			// Only if we can move to the neighbour
			if (ngbs.get(i).getCondition() == NodeCondition.NONE
					|| ngbs.get(i).getIsDoor() == this.key){				
				double r = bestMove(ngbs.get(i), nsteps+1, reward);
				if (r > max){
					max = r;
					best_ngb = i;
				}
			}
		}
		List<Number> result = new ArrayList<Number>();
		result.add(best_ngb);
		result.add(max);
    	return result;
    }
    

    
}
