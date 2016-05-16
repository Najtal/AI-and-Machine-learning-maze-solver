package ai.solver;

import ai.maze.Generator;
import bizz.GoalLoadImpl;
import constant.NodeCondition;
import ucc.GoalDTO;
import ucc.MazeDTO;
import ucc.NodeDTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import exception.NoKeyException;

/**
 * Created by jvdur on 13/05/2016.
 */
public class SolverImpl implements Solver {

	private NodeDTO pos;
	private int key;
	private double new_rwd;
	private double door_rwd;
	private double key_rwd;
	private double goal_rwd;
	private double gamma;
	private boolean[][] visited;
	private int treshold;

	/*
	 * GETTERS
	 */
	private int getKey(){
		return this.key;
	}
	
	private NodeDTO getPosition(){
		return this.pos;
	}
	
	/*
	 * SETTERS
	 */	
	private void takeKey() throws NoKeyException{
		int k = this.pos.getHasKey();
		if (this.key != 0) {
			throw new NoKeyException("takeKey at (" 
							+ this.pos.getPosx() + "," + this.pos.getPosy() 
							+ ") failed : already have key " + this.key + "\n");
		}else if (k == 0){
			throw new NoKeyException("takeKey at (" 
							+ this.pos.getPosx() + "," + this.pos.getPosy() 
							+ ") failed : there is no key to take : "
							+ k + "\n");
		}
		this.pos.setHasKey(0);
		this.key = k;
	}
	
	private void dropKey() throws NoKeyException{
		if (this.key == 0){
			throw new NoKeyException("dropKey at (" 
							+ this.pos.getPosx() + "," + this.pos.getPosy() 
							+ ") failed : we don't have any key : "
							+ this.key);
		} else if (this.pos.getHasKey() != 0) {
			throw new NoKeyException("dropKey at (" 
					+ this.pos.getPosx() + "," + this.pos.getPosy() 
					+ ") failed : there's already a key " + this.pos.getHasKey() + "\n");			
		}
		this.pos.setHasKey(this.key);
		this.key = 0;
	}
	
	private void move(NodeDTO destination){
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
		this.key_rwd = goals.getLoadReachGoal();

		this.gamma = 1;
		this.visited = new boolean[maze.getSizex()][maze.getSizey()];
		for (int i = 0; i < maze.getSizex(); i++){
			for (int j = 0; j < maze.getSizey(); j++){
				visited[i][j] = false;
			}
		}
		this.treshold = maze.getSizex()*maze.getSizey();
		this.pos = maze.getStartNode();
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
			e.printStackTrace();
		}
    }
    
    
    private double bestMove(NodeDTO node, int nsteps, double reward) throws NoKeyException{
    	if (nsteps > this.treshold){
    		return reward;
    	}
    	if (visited[node.getPosx()][node.getPosy()]==false){
    		visited[node.getPosx()][node.getPosy()]=true;
    		if (node.isGoal()){
    			return reward + goal_rwd*Math.pow(gamma,nsteps);
    		}
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
    			//System.out.print("ngbs of node "+ node +" at (" 
				//			+ this.pos.getPosx() + "," + this.pos.getPosy() 
				//			+ "): " + ngbs.size() + "\n");
    			
    			//List<Number> l = bestNeighbour(ngbs, nsteps, reward);
    			double max ;
    			try {
    	    	max = bestMove(ngbs.get(0), nsteps+1, reward);
    			}catch (Exception e){
    				System.out.print(ngbs.size());
    				throw e;
    			}
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

    			move(ngbs.get(best_ngb));
				return max;
    		}
    		
    		if (this.key==0 && node.getHasKey()!=0){
    			// Move in the best direction with or without taking the key
    			List<NodeDTO> ngbs = node.getNeighbours();
    			//System.out.print("ngbs : " + ngbs.size() + "\n");
    			
    			// Without the key
    			//List<Number> l = bestNeighbour(ngbs, nsteps, reward);
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
    			
    			//With the key
    			//System.out.print("a) do we have a key at (" 
				//			+ this.pos.getPosx() + "," + this.pos.getPosy() 
				//			+ ") ? "+this.key + "\n");
    			//System.out.print("b) is there a key to take at (" 
				//			+ this.pos.getPosx() + "," + this.pos.getPosy() 
				//			+ ") ? "+node.getHasKey() + "\n");

    			//takeKey();
    			this.pos.setHasKey(0);
    			this.key = this.pos.getHasKey();
    			
    			double new_reward = reward+key_rwd*Math.pow(gamma, nsteps);
    			
    			//List<Number> l2 = bestNeighbour(ngbs, nsteps+1, new_reward);
    	    	double max2 = bestMove(ngbs.get(0), nsteps+2, new_reward);
    			int best_ngb2 = 0;
    			for (int i = 1; i < ngbs.size() ; i++){
    				// Only if we can move to the neighbour
    				if (ngbs.get(i).getCondition() == NodeCondition.NONE
    						|| ngbs.get(i).getIsDoor() == this.key){				
    					double r = bestMove(ngbs.get(i), nsteps+2, new_reward);
    					if (r > max2){
    						max2 = r;
    						best_ngb2 = i;
    					}
    				}
    			}
    			
    			//Is it better to take the key ? 
    			if (max > max2){
    				//dropKey();
    				this.pos.setHasKey(this.key);
    				this.key = 0;
    				move(ngbs.get(best_ngb));
    				return max;
    			}else{
    				move(ngbs.get(best_ngb2));
    				return max2;
    			}
    		}
    		
    		else {//if (node.getHasKey()==0 && this.key != 0){
    			// Move in the best direction with or without dropping the key
    			List<NodeDTO> ngbs = node.getNeighbours();
    			//System.out.print("ngbs : " + ngbs.size() + "\n");

    			// Keeping the key
    			//List<Number> l = bestNeighbour(ngbs, nsteps, reward);
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
    			
    			// Dropping the key
    			//System.out.print("a) do we have a key to drop at (" 
				//			+ this.pos.getPosx() + "," + this.pos.getPosy() 
				//			+ ") ? "+this.key + "\n");
    			//System.out.print("b) is there a key at (" 
				//			+ this.pos.getPosx() + "," + this.pos.getPosy() 
				//			+ ") ? "+node.getHasKey() + "\n");

    			//dropKey();
    			this.pos.setHasKey(this.key);
    			this.key = 0;
    			
    			//List<Number> l2 = bestNeighbour(ngbs, nsteps+1, reward);
    	    	double max2 = bestMove(ngbs.get(0), nsteps+2, reward);
    			int best_ngb2 = 0;
    			for (int i = 1; i < ngbs.size() ; i++){
    				// Only if we can move to the neighbour
    				if (ngbs.get(i).getCondition() == NodeCondition.NONE
    						|| ngbs.get(i).getIsDoor() == this.key){				
    					double r = bestMove(ngbs.get(i), nsteps+2, reward);
    					if (r > max2){
    						max2 = r;
    						best_ngb2 = i;
    					}
    				}
    			}

    			// Is it better to drop the key ?
    			if (max > max2){
    				//takeKey();
        			this.pos.setHasKey(0);
        			this.key = this.pos.getHasKey();
    				move(ngbs.get(best_ngb));
    				return max;
    			}else{
    				move(ngbs.get(best_ngb2));
    				return max2;
    			}	
    		}
		}
	}
    
    //Find the best direction 
    private List<Number> bestNeighbour(List<NodeDTO> ngbs, int nsteps, double reward) throws NoKeyException{
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
    
    /**
     * MAIN, for testing and dev purposes
     * @param args
     */
    public static void main(String args[]) {
    	Generator gen = new Generator(3,3,0);
        MazeDTO maze = gen.generate();
        GoalDTO goals = new GoalLoadImpl(10, 20, 30, 400, 1);
        System.out.print("discover :" + goals.getLoadDiscoverPath() + "\n");
        System.out.print("key :" + goals.getLoadGrabKey() + "\n");
        System.out.print("door :" + goals.getLoadOpenDoor() + "\n");
        System.out.print("goal :" + goals.getLoadReachGoal() + "\n");
        SolverImpl s = new SolverImpl(maze, goals);
        
        int i = 0;
        while (s.isSolved() == false && i<10){
        	i++;
        	s.doOneStep();
        
        	System.out.print("position : (" + s.getPosition().getPosx() + ","
        		+ s.getPosition().getPosy() + ")\n");
            System.out.print("key : " + s.getKey() + "\n");
            System.out.print("voisins : " + s.getPosition().getNeighbours().size() + "\n");
        }
    }

    
}
