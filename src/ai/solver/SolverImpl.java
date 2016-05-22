package ai.solver;

import ai.maze.Generator;
import bizz.GoalLoadImpl;
import constant.NodeCondition;
import ucc.GoalDTO;
import ucc.MazeDTO;
import ucc.NodeDTO;

import java.util.ArrayList;
import java.util.List;


//import exception.NoKeyException;

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
		this.goal_rwd = goals.getLoadReachGoal();

		this.gamma = 0.5;
		this.visited = new boolean[maze.getSizex()][maze.getSizey()];
		for (int i = 0; i < maze.getSizex(); i++){
			for (int j = 0; j < maze.getSizey(); j++){
				visited[i][j] = false;
			}
		}
		this.treshold = 30;//maze.getSizex()*maze.getSizey();
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
			//for each "big step" of doOneStep(), we put back to 0 the number of "little steps" and the reward
    		visited[this.pos.getPosx()][this.pos.getPosy()]=true;
    		//We update the part of the maze we know
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    private double bestMove(NodeDTO node, int nsteps, double reward){
    	if (nsteps > this.treshold){
    		return reward;
    	}
    	if (visited[node.getPosx()][node.getPosy()]==false){
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

    			double max ;
    			try {
    	    	max = bestMove(node.getNeighbours().get(0), nsteps+1, reward);
    			}catch (Exception e){
    				System.out.print(node.getNeighbours().size());
    				throw e;
    			}
    			int best_ngb = 0;
    			for (int i = 1; i < node.getNeighbours().size() ; i++){
    				// Only if we can move to the neighbour
    				if (node.getNeighbours().get(i).getCondition() == NodeCondition.NONE
    						|| node.getNeighbours().get(i).getIsDoor() == this.key){				
    					double r = bestMove(node.getNeighbours().get(i), nsteps+1, reward);
    					if (r > max){
    						max = r;
    						best_ngb = i;
    					}
    				}
    			}

    			move(node.getNeighbours().get(best_ngb));
				return max;
    		}
    		
    		if (this.key==0 && node.getHasKey()!=0){
    			// Move in the best direction with or without taking the key
    			    			
    			// Without the key
    			
    	    	double max = bestMove(node.getNeighbours().get(0), nsteps+1, reward);
    			int best_ngb = 0;
    			for (int i = 1; i < node.getNeighbours().size() ; i++){
    				// Only if we can move to the neighbour
    				if (node.getNeighbours().get(i).getCondition() == NodeCondition.NONE
    						|| node.getNeighbours().get(i).getIsDoor() == this.key){				
    					double r = bestMove(node.getNeighbours().get(i), nsteps+1, reward);
    					if (r > max){
    						max = r;
    						best_ngb = i;
    					}
    				}
    			}
    			
    			//With the key

    			//takeKey();
    			this.pos.setHasKey(0);
    			this.key = this.pos.getHasKey();

    			double new_reward = reward+key_rwd*Math.pow(gamma, nsteps);
    			
    	    	double max2 = bestMove(node.getNeighbours().get(0), nsteps+2, new_reward);
    			int best_ngb2 = 0;
    			for (int i = 1; i < node.getNeighbours().size() ; i++){
    				// Only if we can move to the neighbour
    				if (node.getNeighbours().get(i).getCondition() == NodeCondition.NONE
    						|| node.getNeighbours().get(i).getIsDoor() == this.key){				
    					double r = bestMove(node.getNeighbours().get(i), nsteps+2, new_reward);
    					if (r > max2){
    						max2 = r;
    						best_ngb2 = i;
    					}
    				}
    			}
    			
    			//Is it better to take the key ? 
    			if (max > max2){
    				//NO
    				//dropKey();
    				this.pos.setHasKey(this.key);
    				this.key = 0;
    				move(node.getNeighbours().get(best_ngb));
    				return max;
    			}else{
    				//YES
    				move(node.getNeighbours().get(best_ngb2));
    				return max2;
    			}
    		}
    		
    		else {
    			// Move in the best direction with or without dropping the key
    			
    			// Keeping the key
    			
    	    	double max = bestMove(node.getNeighbours().get(0), nsteps+1, reward);
    			int best_ngb = 0;
    			for (int i = 1; i < node.getNeighbours().size() ; i++){
    				// Only if we can move to the neighbour
    				if (node.getNeighbours().get(i).getCondition() == NodeCondition.NONE
    						|| node.getNeighbours().get(i).getIsDoor() == this.key){				
    					double r = bestMove(node.getNeighbours().get(i), nsteps+1, reward);
    					if (r > max){
    						max = r;
    						best_ngb = i;
    					}
    				}
    			}
    			
    			// Dropping the key

    			//dropKey();
    			this.pos.setHasKey(this.key);
    			this.key = 0;
    			
    	    	double max2 = bestMove(node.getNeighbours().get(0), nsteps+2, reward);
    			int best_ngb2 = 0;
    			for (int i = 1; i < node.getNeighbours().size() ; i++){
    				// Only if we can move to the neighbour
    				if (node.getNeighbours().get(i).getCondition() == NodeCondition.NONE
    						|| node.getNeighbours().get(i).getIsDoor() == this.key){				
    					double r = bestMove(node.getNeighbours().get(i), nsteps+2, reward);
    					if (r > max2){
    						max2 = r;
    						best_ngb2 = i;
    					}
    				}
    			}

    			// Is it better to drop the key ?
    			if (max > max2){
    				//NO
    				//takeKey();
        			this.pos.setHasKey(0);
        			this.key = this.pos.getHasKey();
    				move(node.getNeighbours().get(best_ngb));
    				return max;
    			}else{
    				//YES
    				move(node.getNeighbours().get(best_ngb2));
    				return max2;
    			}	
    		}
		}
	}
    
    /**
     * MAIN, for testing and dev purposes
     * @param args
     */
    public static void main(String args[]) {
    	Generator gen = new Generator(7,7,2);
        MazeDTO maze = gen.generate();
        GoalDTO goals = new GoalLoadImpl(10, 20, 30, 400, 1);
        System.out.print("discover :" + goals.getLoadDiscoverPath() + "\n");
        System.out.print("key :" + goals.getLoadGrabKey() + "\n");
        System.out.print("door :" + goals.getLoadOpenDoor() + "\n");
        System.out.print("goal :" + goals.getLoadReachGoal() + "\n\n");
        SolverImpl s = new SolverImpl(maze, goals);
        
        int i = 0;
        while (s.isSolved() == false && i<100){
        	i++;
        	s.doOneStep();
        
        	System.out.print("position : (" + s.getPosition().getPosx() + ","
        		+ s.getPosition().getPosy() + ")\n");
            System.out.print("key : " + s.getKey() + "\n");
            System.out.print("voisins : " + s.getPosition().getNeighbours().size() + "\n");
        }
        if (s.isSolved()){
        	System.out.print("YOUHOUOU ! "+i);
        }
    }

    
}
