package ai.solver;

import ai.maze.Generator;
import bizz.GoalLoadImpl;
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

	private NodeDTO pos;
	private int key;
	private double new_rwd;
	private double door_rwd;
	private double key_rwd;
	private double goal_rwd;
	private double gamma;
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
		this.treshold = maze.getSizex()*maze.getSizey();
		this.pos = maze.getStartNode();
		
		this.pos.setUsefulNeighbour(this.pos.getNeighbours());
    }

    @Override
    public boolean isSolved() {
        return this.pos.isGoal();
    }

    @Override
    public void doOneStep() throws Exception{
    	try {
			//for each "big step" of doOneStep(), we put back to 0 the number of "little steps" and the reward
			MyResult res = bestMove(this.pos, 0, 0, new ArrayList<Action>());
			List<Action> path = res.getPath();
			this.pos = path.get(path.size()-1).getDestination();
    		//We update the part of the maze we know
			if (this.pos.getUsefulNeighbours().isEmpty()){
				this.pos.setUsefulNeighbour(this.pos.getNeighbours());
			}else System.out.print("LALALA");				

    		double reward = res.getReward();
    		System.out.print("\nreward = "+ reward+ ", path : ");
			for (int i = 0; i < path.size() ; i++){
				System.out.print(path.get(i) + ", ");
			}
			System.out.print("\n");
		} catch (Exception e) {
			throw e;
		}
    }
    
    
    private MyResult bestMove(NodeDTO node, int nsteps, double reward, List<Action> path) throws Exception{
    	List<Action> new_path = new ArrayList<Action>(path);
    	new_path.add(new Move(node));
    	
    	if (nsteps > this.treshold){
    		return new MyResult(reward, new_path);
    	}
    	
    	if (node.getUsefulNeighbours().isEmpty()) {
    		if (node.isGoal()){
    			return new MyResult(reward + goal_rwd*Math.pow(gamma,nsteps), new_path);
    		}
    		if (node.getIsDoor()==0){
    			return new MyResult(reward + new_rwd*Math.pow(gamma,nsteps), new_path);
    		}
    		else{
    			return new MyResult(reward + door_rwd*Math.pow(gamma,nsteps), new_path);
    		}
    	}
    	
    	else{
    		List<NodeDTO> neighbours = node.getNeighbours();
			if (neighbours.size() == 0){
				throw new Exception("no neighbours for " + node + "\n");
			}

			//Remove dead-ends of possible ways
			if (node.getUsefulNeighbours().size() == 1){
				node.getUsefulNeighbours().get(0).removeUsefulNeighbour(node);
			}
			
			// WITHOUT ANY KEY
			if ((this.key==0 && node.getHasKey()==0)
					|| (this.key!=0 && node.getHasKey()!=0)){
				// Move in the best direction

				double max = 0;
				List<Action> best_path = new ArrayList<Action>();
				for (int i = 0; i < neighbours.size() ; i++){
					// Only if we can move to the neighbour
					if (node.getUsefulNeighbours().contains(neighbours.get(i))
							&& (neighbours.get(i).getCondition() == NodeCondition.NONE
							|| neighbours.get(i).getIsDoor() == this.key)){
						MyResult res = bestMove(neighbours.get(i), nsteps+1, reward, new_path);
						double r = res.getReward();
						if (r > max){
							max = r;
							best_path = res.getPath();
						}
					}
				}

				return new MyResult(max, best_path);
			}
			
			
			// WITH KEY ON THE FLOOR
			else {
		    	System.out.print("\n"+nsteps+ " steps (key = "+this.key+"), "
		    			+"node at ("+node.getPosx()+","+node.getPosy()+"), ("+node.getNeighbours().size()+" ngbs), "
		    			+"(key = "+node.getHasKey()+") path : "+path+"\n");

				if (this.key==0 && node.getHasKey()!=0){
					// Move in the best direction with or without taking the key
					    			
					// Without the key
					
					double max = 0;
					List<Action> best_path = new ArrayList<Action>();
					for (int i = 0; i < neighbours.size() ; i++){
						// Only if we can move to the neighbour
						if (node.getUsefulNeighbours().contains(neighbours.get(i))
								&& (neighbours.get(i).getCondition() == NodeCondition.NONE
								|| neighbours.get(i).getIsDoor() == this.key)){				
							MyResult res = bestMove(neighbours.get(i), nsteps+1, reward, new_path);
							double r = res.getReward();
							if (r > max){
								max = r;
								best_path = res.getPath();
							}
						}
					}
	
					
					//With the key
	
					//takeKey();
					int k = node.getHasKey();
					System.out.print("TAKE KEY "+k+" (step "+nsteps+")\n");
					node.setHasKey(0);
					this.key = k;
					new_path.add(KeyAction.TAKE_KEY);
					
					double new_reward = reward+key_rwd*Math.pow(gamma, nsteps);
					
					double max2 = 0;
					List<Action> best_path2 = new ArrayList<Action>();
					for (int i = 0; i < neighbours.size() ; i++){
						// Only if we can move to the neighbour
						if (node.getUsefulNeighbours().contains(neighbours.get(i))
								&& (neighbours.get(i).getCondition() == NodeCondition.NONE
								|| neighbours.get(i).getIsDoor() == this.key)){				
							MyResult res = bestMove(neighbours.get(i), nsteps+2, new_reward, new_path);
							double r = res.getReward();
							if (r > max2){
								max2 = r;
								best_path2 = res.getPath();
							}
						}
					}
					
					//Is it better to take the key ? 
					if (max > max2){
						//NO
						//dropKey();
						System.out.print("DON'T TAKE THE KEY");
						node.setHasKey(this.key);
						this.key = 0;
						return new MyResult(max, best_path);
					}else{
						//YES
						return new MyResult(max2, best_path2);
					}
				}
				
				else {
					// Move in the best direction with or without dropping the key
					
					// Keeping the key
					
					double max = 0;
					List<Action> best_path = new ArrayList<Action>();
					for (int i = 0; i < neighbours.size() ; i++){
						// Only if we can move to the neighbour
						if (node.getUsefulNeighbours().contains(neighbours.get(i))
								&& (neighbours.get(i).getCondition() == NodeCondition.NONE
								|| neighbours.get(i).getIsDoor() == this.key)){				
							MyResult res = bestMove(neighbours.get(i), nsteps+1, reward, new_path);
							double r = res.getReward();
							if (r > max){
								max = r;
								best_path = res.getPath();
							}
						}
					}
					
					// Dropping the key
	
					//dropKey();
					node.setHasKey(this.key);
					this.key = 0;
					System.out.print("DROP THE KEY "+node.getHasKey());
					new_path.add(KeyAction.DROP_KEY);
					
					double max2 = 0;
					List<Action> best_path2 = new ArrayList<Action>();
					for (int i = 0; i < neighbours.size() ; i++){
						// Only if we can move to the neighbour
						if (node.getUsefulNeighbours().contains(neighbours.get(i))
								&& (neighbours.get(i).getCondition() == NodeCondition.NONE
								|| neighbours.get(i).getIsDoor() == this.key)){				
							MyResult res = bestMove(neighbours.get(i), nsteps+2, reward, new_path);
							double r = res.getReward();
							if (r > max2){
								max2 = r;
								best_path2 = res.getPath();
							}
						}
					}
	
					// Is it better to drop the key ?
					if (max > max2){
						//NO
						//takeKey();
						System.out.print(" DON'T DROP THE KEY ");
						int k = node.getHasKey();
						node.setHasKey(0);
						this.key = k;
						return new MyResult(max, best_path);
					}else{
						//YES
						return new MyResult(max2, best_path2);
					}	
				}
			}
		}
	}
    
    
    /*
     * class just to return the reward and the path in bestMove()
     */
    final class MyResult {
        private final double reward;
        private final List<Action> path;

        public MyResult(double rwd, List<Action> actions) {
            this.reward = rwd;
            this.path = actions;
        }

        public double getReward() {
            return this.reward;
        }

        public List<Action> getPath() {
            return this.path;
        }
    }    
    
    
    
    /**
     * MAIN, for testing and dev purposes
     * @param args
     * @throws Exception 
     */
    public static void main(String args[]) throws Exception {
    	long startTime = System.currentTimeMillis();
    	
    	Generator gen = new Generator(5,5,2);
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
        }
        if (s.isSolved()){
        	System.out.print("\nYOUHOUOU ! in "+i+" big steps\n");
        }

        long endTime   = System.currentTimeMillis();
    	long totalTime_m = endTime - startTime;
    	long totalTime_s = totalTime_m / 1000;
    	System.out.println("TotalTime : "+totalTime_m+" ms ("+totalTime_s+"sec) ");
    }
    
}
