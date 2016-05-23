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


/**
 * Created by jvdur on 13/05/2016.
 */
public class SolverImpl implements Solver {

	private NodeDTO pos;
	private int key;
	private final double new_rwd;
	private final double door_rwd;
	private final double key_rwd;
	private final double goal_rwd;
	private final double gamma;
	private final int treshold;
	private Set<NodeDTO> nodesWithKey;

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
		
		this.nodesWithKey = new HashSet<NodeDTO>(); 
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
    
    
    private MyResult bestMove(NodeDTO node, int cpt, double reward, List<Action> path) throws Exception{
//    	System.out.print("bestMove("+node+","+cpt+","+reward+","+path+"), keys at "+nodesWithKey+"\n");
    	List<Action> new_path = new ArrayList<Action>(path);
    	new_path.add(new Move(node));
    	
    	if (path.size() > this.treshold){
    		return new MyResult(reward, new_path);
    	}
    	
    	if (node.getUsefulNeighbours().isEmpty()) {
    		if (node.isGoal()){
    			return new MyResult(reward + goal_rwd*Math.pow(gamma,path.size()), new_path);
    		}
    		if (node.getIsDoor()==0){
    			return new MyResult(reward + new_rwd*Math.pow(gamma,path.size()), new_path);
    		}
    		else{
    			return new MyResult(reward + door_rwd*Math.pow(gamma,path.size()), new_path);
    		}
    	}
    	
    	else{
    		List<NodeDTO> neighbours = node.getNeighbours();
			if (neighbours.size() == 0){
				throw new Exception("no neighbours for " + node + "\n");
			}

			//Remove dead-ends of possible ways
			if (node.getUsefulNeighbours().size() == 1){
				NodeDTO ngb = node.getUsefulNeighbours().get(0);
//				System.out.print(node+" is a DEAD_END for "+ngb+"\n");
				ngb.removeUsefulNeighbour(node);
			}
			
			// WITHOUT ANY KEY ACTION
			if ((this.key==0 && node.getHasKey()==0)
					|| (this.key!=0 && node.getHasKey()!=0)){
				
				if (node.getHasKey()!=0){
					nodesWithKey.add(node);
				}
				
				// Move in the best direction
				
				double max = 0;
				List<Action> best_path = new ArrayList<Action>();

				boolean door = false;

				//iterate on neighbours and not on usefulNeighbour 
				//because usefulNeighbours can change during the iteration 
				for (int i = 0; i < neighbours.size() ; i++){
					// Only if we can move to the neighbour
					if (node.getUsefulNeighbours().contains(neighbours.get(i))) {
						if (neighbours.get(i).getCondition() == NodeCondition.NEED_KEY){
							door = true;
						}
						if (neighbours.get(i).getCondition() == NodeCondition.NONE
							|| neighbours.get(i).getIsDoor() == this.key){
							MyResult res = bestMove(neighbours.get(i), cpt+1, reward, new_path);
							double r = res.getReward();
							if (r > max){
								max = r;
								best_path = res.getPath();
							}
						}
					}
				}
				if (door){
					Set<NodeDTO> nwk = new HashSet<NodeDTO>(nodesWithKey); 
					for (NodeDTO n : nwk){
						if (n.equals(node) == false && node.getNeighbours().contains(n)==false){
							new_path.add(new Move(n));
							new_path.add(KeyAction.TAKE_KEY);
							this.key = n.getHasKey();
							n.setHasKey(0);
							nodesWithKey.remove(n);
							new_path.add(new Move(node));
							//TODO adjust reward and path
							MyResult res = bestMove(node, cpt+1, reward, new_path);
							double r = res.getReward();
							if (r > max){
								max = r;
								best_path = res.getPath();
							}
						}
					}
				}

				return new MyResult(max, best_path);
			}
			
			
			// WITH KEY 
			else {

				// WITH KEY ON THE FLOOR
				if (this.key==0 && node.getHasKey()!=0){
//					System.out.print("KEY "+ node.getHasKey()+" ON THE FLOOR\n");
					nodesWithKey.add(node);
					// Move in the best direction with or without taking the key
					    			
					// Without the key
					
					double max = 0;
					List<Action> best_path = new ArrayList<Action>();
					for (int i = 0; i < neighbours.size() ; i++){
						// Only if we can move to the neighbour
						if (node.getUsefulNeighbours().contains(neighbours.get(i))
								&& (neighbours.get(i).getCondition() == NodeCondition.NONE
								|| neighbours.get(i).getIsDoor() == this.key)){				
							MyResult res = bestMove(neighbours.get(i), cpt+1, reward, new_path);
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
//					System.out.print("TAKE KEY "+k+" (iteration "+cpt+")\n");
					node.setHasKey(0);
					this.key = k;
					nodesWithKey.remove(node);
					new_path.add(KeyAction.TAKE_KEY);
					
					double new_reward = reward+key_rwd*Math.pow(gamma, path.size());
					
					double max2 = 0;
					List<Action> best_path2 = new ArrayList<Action>();
					for (int i = 0; i < neighbours.size() ; i++){
						// Only if we can move to the neighbour
						if (node.getUsefulNeighbours().contains(neighbours.get(i))
								&& (neighbours.get(i).getCondition() == NodeCondition.NONE
								|| neighbours.get(i).getIsDoor() == this.key)){				
							MyResult res = bestMove(neighbours.get(i), cpt+1, new_reward, new_path);
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
//						System.out.print("DON'T TAKE THE KEY\n");
						nodesWithKey.add(node);
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
							MyResult res = bestMove(neighbours.get(i), cpt+1, reward, new_path);
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
					nodesWithKey.add(node);
					this.key = 0;
//					System.out.print("DROP THE KEY "+node.getHasKey()+"\n");
					new_path.add(KeyAction.DROP_KEY);					
					
					double max2 = 0;
					List<Action> best_path2 = new ArrayList<Action>();

					boolean door = false;
					
					for (int i = 0; i < neighbours.size() ; i++){
						// Only if we can move to the neighbour
						if (node.getUsefulNeighbours().contains(neighbours.get(i))) {
							if (neighbours.get(i).getCondition() == NodeCondition.NEED_KEY){
								door = true;
							}
							if (neighbours.get(i).getCondition() == NodeCondition.NONE 
									|| neighbours.get(i).getIsDoor() == this.key){
								MyResult res = bestMove(neighbours.get(i), cpt+1, reward, new_path);
								double r = res.getReward();
								if (r > max2){
									max2 = r;
									best_path2 = res.getPath();
								}
							}
						}
					}
					if (door){
						Set<NodeDTO> nwk = new HashSet<NodeDTO>(nodesWithKey); 
						for (NodeDTO n : nwk){
							if (n.equals(node) == false && node.getNeighbours().contains(n)==false){
								new_path.add(new Move(n));
								new_path.add(KeyAction.TAKE_KEY);
								this.key = n.getHasKey();
								n.setHasKey(0);
								nodesWithKey.remove(n);
								new_path.add(new Move(node));
								//TODO adjust reward and path
								MyResult res = bestMove(node, cpt+1, reward, new_path);
								double r = res.getReward();
								if (r > max2){
									max2 = r;
									best_path2 = res.getPath();
								}
							}
						}
					}
					
					// Is it better to drop the key ?
					if (max > max2){
						//NO
						//takeKey();
//						System.out.print("DON'T DROP THE KEY\n");
						nodesWithKey.remove(node);
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
//    	MazeDTO maze = gen.generate();
    	MazeDTO maze = gen.generateTest1(1);
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
        
        	System.out.print("iteration : " +i+ "\n");
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