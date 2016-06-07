package ai.solver;

import ai.maze.Generator;
import bizz.GoalLoadImpl;
import constant.NodeCondition;
import ucc.GoalDTO;
import ucc.MazeDTO;
import ucc.NodeDTO;
import util.Position;

import java.util.*;


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
	private final int level;
	private NodeDTO[] nodesWK;

	/*
	 * GETTERS
	 */
	private int getKey(){
		return this.key;
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
		this.treshold = Math.min(60,maze.getSizex()*maze.getSizey());
		this.pos = maze.getStartNode();
		
		this.pos.setUsefulNeighbour(this.pos.getNeighbours());
		this.level = maze.getNbKey();
		this.nodesWK = new NodeDTO[this.level];
    }

    @Override
    public boolean isSolved() {
        return this.pos.isGoal();
    }

    @Override
    public int doOneStep() throws Exception{
    	try {
			MyResult res = bestMove(this.pos, this.pos, 0, new ArrayList<Action>(), this.nodesWK, this.key, new HashSet<NodeDTO>());
			
			//assure the good disposition of keys
			for (NodeDTO node : res.getKeyRemoved()){
				if (node != null) node.setHasKey(0);
			}
			NodeDTO[] nodesWithKey = res.getNodesWithKey();
			for (int i=0; i < nodesWithKey.length; i++){
				if (nodesWithKey[i] != null) nodesWithKey[i].setHasKey(i+1);
			}
			this.nodesWK = Arrays.copyOf(nodesWithKey, nodesWithKey.length);
			
			List<Action> path = res.getPath();
			this.pos = path.get(path.size()-1).getDestination();
			
			this.key = res.getKey();
    		
			//We update the part of the maze we know
			if (this.pos.getUsefulNeighbours().isEmpty()){
				this.pos.setUsefulNeighbour(this.pos.getNeighbours());
			}			

//    		double reward = res.getReward();
//    		System.out.print("\nreward = "+ reward+ ", path : ");
//			for (int i = 0; i < path.size() ; i++){
//				System.out.print(path.get(i) + ", ");
//			}
//			System.out.print("\n");
			return path.size();
		} catch (Exception e) {
			throw e;
		}
    }
    
    
    private MyResult bestMove(NodeDTO node, NodeDTO from, double reward, 
    		List<Action> path, NodeDTO[] nodesKey, int myKey, Set<NodeDTO> keyRemoved) throws Exception{
    	List<Action> new_path = new ArrayList<Action>(path);
    	new_path.add(new Move(node));
		if (node.getPosx()==5 && node.getPosy()==5 && myKey==0){
			System.out.print("");
		}
    	NodeDTO[] nodesWithKey = Arrays.copyOf(nodesKey, nodesKey.length);
    	
		List<NodeDTO> neighbours = node.getNeighbours();
    	
    	if (path.size() > this.treshold){
    		return new MyResult(reward, new_path, nodesWithKey, myKey, keyRemoved);
    	}
    	
    	if (node.getUsefulNeighbours().isEmpty()) {
    		if (node.isGoal()){
    			return new MyResult(reward + goal_rwd*Math.pow(gamma,path.size()), new_path, nodesWithKey, myKey, keyRemoved);
    		}
    		if (node.getIsDoor()!=0){
    			node.setCondition(NodeCondition.NONE);
    			return new MyResult(reward + door_rwd*Math.pow(gamma,path.size()), new_path, nodesWithKey, myKey, keyRemoved);
    		}
    		else{
    			return new MyResult(reward + new_rwd*Math.pow(gamma,path.size()), new_path, nodesWithKey, myKey, keyRemoved);
    		}
    	}
    	
    	else{
    		//If node is a dead-end
			boolean deadend = false;
			if (node.getUsefulNeighbours().size() == 1){
				NodeDTO ngb = node.getUsefulNeighbours().get(0);
				deadend = true;
//				System.out.print(node+" is a DEAD_END for "+ngb+"\n");
				ngb.removeUsefulNeighbour(node);
			}
			
			// WITHOUT ANY KEY ACTION (no key or key both on the floor and in our hands)
			if ((myKey==0 && node.getHasKey()==0)
					|| (myKey!=0 && node.getHasKey()!=0)){
				
				//if there's a key on the floor, we should remember it
				if (node.getHasKey()!=0){
					try{
						nodesWithKey[node.getHasKey()-1] = node;
					}catch(Exception e){
						System.out.print(node+","+node.getHasKey());
						throw e;
					}
				}
				
				// Move in the best direction
				MyResult best_res = bestDirection(node, from, nodesWithKey, myKey, reward, new_path, deadend, neighbours, keyRemoved);
				return new MyResult(best_res.getReward(), best_res.getPath(), best_res.getNodesWithKey(), best_res.getKey(), best_res.getKeyRemoved());
			}
			
			
			// WITH KEY 
			else {

				// WITH KEY ON THE FLOOR
				if (myKey==0 && node.getHasKey()!=0){
					nodesWithKey[node.getHasKey()-1] = node;
					
					// Move in the best direction with or without taking the key
					    			
					// Without the key
					MyResult best_res = bestDirection(node, from, nodesWithKey, myKey, reward, new_path, deadend, neighbours, keyRemoved);
					List<Action> p = best_res.getPath();
					if (p.get(p.size()-1).getDestination()==null) best_res.reward = -1;
					double max = best_res.getReward();
					
					//With the key
					//takeKey();
					int k = node.getHasKey();
					node.setHasKey(0);
					myKey = k;
					nodesWithKey[k-1] = null;
					new_path.add(KeyAction.TAKE_KEY);
					double new_reward = reward+key_rwd*Math.pow(gamma, path.size());
					keyRemoved.add(node);

					
		    		if (node.getPosx()==5 && node.getPosy()==5 && myKey==0){
		    			System.out.print("");
		    		}
					
					MyResult best_res2 = bestDirection(node, node, nodesWithKey, myKey, new_reward, new_path, deadend, neighbours, keyRemoved);
					double max2 = best_res2.getReward();
					
					//Is it better to take the key ? 
					if (max > max2){
						//NO
						//dropKey();
						node.setHasKey(myKey);
						nodesWithKey[node.getHasKey()-1] = node;
						myKey = 0;
						return new MyResult(max, best_res.getPath(), best_res.getNodesWithKey(), best_res.getKey(), best_res.getKeyRemoved());
					}else{
						//YES
						node.setHasKey(myKey);//on higher levels the action has not been made yet
						return new MyResult(max2, best_res2.getPath(), best_res2.getNodesWithKey(), best_res2.getKey(), best_res2.getKeyRemoved());
					}
				}
				
				//WITH KEY IN OUR HANDS
				else {
					// Move in the best direction with or without dropping the key
					
					// Keeping the key					
					MyResult best_res = bestDirection(node, from, nodesWithKey, myKey, reward, new_path, deadend, neighbours, keyRemoved);
					List<Action> p = best_res.getPath();
					if (p.get(p.size()-1).getDestination()==null) best_res.reward = -1;
					double max = best_res.getReward();
					// Dropping the key
					//dropKey();
					node.setHasKey(myKey);
					nodesWithKey[node.getHasKey()-1] = node;
					myKey = 0;
					new_path.add(KeyAction.DROP_KEY);

					MyResult best_res2 = bestDirection(node, from, nodesWithKey, myKey, reward, new_path, deadend, neighbours, keyRemoved);
					double max2 = best_res2.getReward();
					
					// Is it better to drop the key ?
					if (max > max2){
						//NO
						//takeKey();
						int k = node.getHasKey();
						nodesWithKey[k-1] = null;
						node.setHasKey(0);
						myKey = k;
						return new MyResult(max, best_res.getPath(), best_res.getNodesWithKey(), best_res.getKey(), best_res.getKeyRemoved());
					}else{
						//YES
						node.setHasKey(0);//on higher levels the actions has not been made yet
						return new MyResult(max2, best_res2.getPath(), best_res2.getNodesWithKey(), best_res2.getKey(), best_res.getKeyRemoved());
					}	
				}
			}
		}
	}
    
    
    private MyResult jumpToKey(int j, NodeDTO[] nodesWithKey, NodeDTO node, List<Action> path,
    		int myKey, double reward, Set<NodeDTO>keyRemoved) throws Exception{
    	if (nodesWithKey[j] != null
				&& nodesWithKey[j].equals(node) == false 
				&& node.getNeighbours().contains(nodesWithKey[j])==false){
			
    		List<Action>new_path = new ArrayList<Action>(path);
    		List<Action> p = pathFromTo(node, nodesWithKey[j], new ArrayList<Action>(), node);
    		
			new_path.addAll(p);
			double new_reward = reward+key_rwd*Math.pow(gamma, new_path.size());
			new_path.add(KeyAction.TAKE_KEY);

    		if (node.getPosx()==5 && node.getPosy()==5 && myKey==0){
    			System.out.print("");
    		}
			myKey = j+1;
			nodesWithKey[j].setHasKey(0);
			nodesWithKey[j] = null;
			keyRemoved.add(nodesWithKey[j]);
			
			for (int i = p.size()-2; i>=0; i--){
				new_path.add(p.get(i));				
			}
			new_path.add(new Move(node));
			
			int nodeKey = node.getHasKey();
			MyResult res = bestMove(node, node, new_reward, new_path, nodesWithKey, myKey, keyRemoved);
			node.setHasKey(nodeKey);
			List<Action> res_p = res.getPath();
				if (res_p.get(res_p.size()-1).getDestination()==null){
					res.setReward(-1);
				}
			return res;
			}
        else {
        	return null;
        }
	}

    private List<Action> pathFromTo(NodeDTO start, NodeDTO end, List<Action> path, NodeDTO from){
    	List<Action> new_path = new ArrayList<Action>(path);
    	if (start != from){
    		new_path.add(new Move(start));
    	}
    	if (start==end) {
    		return new_path;
    	}
    	if (start != from && start.getNeighbours().size()==1){
    		return null;
    	}
    	List<Action> best_path = null;
    	for (NodeDTO ngb: start.getNeighbours()){
    		if (ngb != from){
    			List<Action> p = pathFromTo(ngb, end, new_path, start);
    			if (p != null){
    				best_path = p;
    				break;
    			}
    		}
    	}
    	return best_path;
    }
    
    
    private MyResult bestDirection(NodeDTO node, NodeDTO from, NodeDTO[] nodesWithKey, int myKey, double reward,
    		List<Action> new_path, boolean deadend, List<NodeDTO> neighbours, Set<NodeDTO> keyRemoved) throws Exception{
		double max = -1;
		MyResult best_res = new MyResult(reward, new_path, nodesWithKey, myKey, keyRemoved);
	
		boolean[] door = new boolean[this.level];
	
		//If node is a dead-end
		if (!deadend && node.getUsefulNeighbours().size() == 1){
			NodeDTO ngb = node.getUsefulNeighbours().get(0);
			deadend = true;
//			System.out.print(node+" is a DEAD_END for "+ngb+"\n");
			ngb.removeUsefulNeighbour(node);
		}
		
		if (deadend){
			NodeDTO next_node = node.getUsefulNeighbours().get(0);
			if (next_node.getCondition() == NodeCondition.NONE
					|| next_node.getIsDoor() == myKey){
				int nodeKey = node.getHasKey();
				MyResult res = bestMove(node.getUsefulNeighbours().get(0), node, reward, new_path, nodesWithKey, myKey, keyRemoved);
				List<Action> res_p = res.getPath();
				if (res_p.get(res_p.size()-1).getDestination()==null){
					res.setReward(-1);
				}
				max = res.getReward();
				best_res = res;
				node.setHasKey(nodeKey);
			}else{
				if (myKey != 0) {
					//dropKey();
					node.setHasKey(myKey);
					nodesWithKey[node.getHasKey()-1] = node;
					myKey = 0;
					new_path.add(KeyAction.DROP_KEY);
				}
				door[next_node.getIsDoor()-1] = true;
			}
		}
		else {
			//iterate on neighbours and not on usefulNeighbour 
			//because usefulNeighbours can change during the iteration 
			for (int i = 0; i < neighbours.size() ; i++){
				// Only if we can move to the neighbour
				if (from != neighbours.get(i) &&
						node.getUsefulNeighbours().contains(neighbours.get(i))) {
					if (neighbours.get(i).getCondition() == NodeCondition.NEED_KEY){
						door[neighbours.get(i).getIsDoor()-1] = true;
					}
					if (neighbours.get(i).getCondition() == NodeCondition.NONE
						|| neighbours.get(i).getIsDoor() == myKey){
						if (node.getPosx()==4 && node.getPosy()==3){
							System.out.print("");
						}
						int nodeKey = node.getHasKey();
						MyResult res = bestMove(neighbours.get(i), node, reward, new_path, nodesWithKey, myKey, keyRemoved);
						node.setHasKey(nodeKey);
						List<Action> res_p = res.getPath();
						if (res_p.get(res_p.size()-1).getDestination()==null){
							res.setReward(-1);
						}
						double r = res.getReward();
						if (r > max){
							max = r;
							best_res = res;
						}
					}
				}
			}
		}
		if (myKey == 0){
			for (int j = 0; j < this.level; j++){
				if (door[j]){
					MyResult res = jumpToKey(j, nodesWithKey, node, new_path, myKey, reward, keyRemoved);
					if (res != null){
						double r = res.getReward();
						if (r > max){
							max = r;
							best_res = res;
						}
					}
				}
			}
		}
		List<Action> path = best_res.getPath();
		if (path.get(path.size()-1).getDestination()==null) best_res.reward = -1;
		return best_res;
    }
    
    /*
     * class just to return the reward and the path in bestMove()
     */
    final class MyResult {
        private double reward;
        private List<Action> path;
        private NodeDTO[] nodesWithKey;
        private int key;
        private Set<NodeDTO> keyRemoved;

        public MyResult(double rwd, List<Action> actions, NodeDTO[] nodes, int k, Set<NodeDTO> remove) {
            this.reward = rwd;
            this.path = actions;
            this.nodesWithKey = nodes;
            this.key = k;
            this.keyRemoved = remove;
        }

        public double getReward() {
            return this.reward;
        }

		public void setReward(double r) { this.reward = r; }

        public List<Action> getPath() {
            return this.path;
        }

        public NodeDTO[] getNodesWithKey(){
        	return this.nodesWithKey;
        }

        public int getKey(){
        	return this.key;
        }
        
        public Set<NodeDTO> getKeyRemoved(){
        	return this.keyRemoved;
        }
        @Override
        public String toString(){
        	return reward+", "+path+", "+key;
        }
        
    }    
    
    
    private static MazeDTO test1(){
    	int size = 5;
    	int[][] structure = new int[size][size];
    	structure[0][0] = 2;
    	structure[1][0] = 4;
    	structure[2][0] = 14;
    	structure[3][0] = 12;
    	structure[4][0] = 10;
    	structure[0][1] = 5;
    	structure[1][1] = 12;
    	structure[2][1] = 9;
    	structure[3][1] = 2;
    	structure[4][1] = 3;
    	structure[0][2] = 6;
    	structure[1][2] = 12;
    	structure[2][2] = 10;
    	structure[3][2] = 7;
    	structure[4][2] = 9;
    	structure[0][3] = 3;
    	structure[1][3] = 6;
    	structure[2][3] = 9;
    	structure[3][3] = 5;
    	structure[4][3] = 10;
    	structure[0][4] = 1;
    	structure[1][4] = 5;
    	structure[2][4] = 12;
    	structure[3][4] = 12;
    	structure[4][4] = 9;
    	
    	List<Position> doors = new ArrayList<Position>();
    	doors.add(new Position(3,1));
    	List<Position> keys = new ArrayList<Position>();
    	keys.add(new Position(0,1));
    	keys.add(new Position(1,3));


    	Generator gen = new Generator(size,size,keys.size());
    	MazeDTO maze = gen.generateTest(size, structure, keys, doors, new Position(1,1));
    	return maze;
    }
    
    private static MazeDTO test2(){
    	int size = 5;
    	int[][] structure = new int[size][size];
    	structure[0][0] = 2;
    	structure[1][0] = 4;
    	structure[2][0] = 14;
    	structure[3][0] = 12;
    	structure[4][0] = 10;
    	structure[0][1] = 5;
    	structure[1][1] = 12;
    	structure[2][1] = 9;
    	structure[3][1] = 6;
    	structure[4][1] = 9;
    	structure[0][2] = 6;
    	structure[1][2] = 14;
    	structure[2][2] = 10;
    	structure[3][2] = 5;
    	structure[4][2] = 10;
    	structure[0][3] = 3;
    	structure[1][3] = 3;
    	structure[2][3] = 1;
    	structure[3][3] = 6;
    	structure[4][3] = 9;
    	structure[0][4] = 1;
    	structure[1][4] = 5;
    	structure[2][4] = 12;
    	structure[3][4] = 13;
    	structure[4][4] = 8;
    	
    	List<Position> doors = new ArrayList<Position>();
    	doors.add(new Position(2,3));
    	doors.add(new Position(3,0));
    	List<Position> keys = new ArrayList<Position>();
    	keys.add(new Position(0,0));
    	keys.add(new Position(2,1));


    	Generator gen = new Generator(size,size,keys.size());
    	MazeDTO maze = gen.generateTest(size, structure, keys, doors, new Position(0,0));
    	return maze;
    }    
    
    private static MazeDTO test3(){
    	int size = 5;
    	int[][] structure = new int[size][size];
    	structure[0][0] = 2;
    	structure[1][0] = 6;
    	structure[2][0] = 12;
    	structure[3][0] = 12;
    	structure[4][0] = 10;
    	structure[0][1] = 3;
    	structure[1][1] = 3;
    	structure[2][1] = 2;
    	structure[3][1] = 6;
    	structure[4][1] = 11;
    	structure[0][2] = 3;
    	structure[1][2] = 5;
    	structure[2][2] = 9;
    	structure[3][2] = 3;
    	structure[4][2] = 3;
    	structure[0][3] = 5;
    	structure[1][3] = 12;
    	structure[2][3] = 10;
    	structure[3][3] = 3;
    	structure[4][3] = 3;
    	structure[0][4] = 4;
    	structure[1][4] = 12;
    	structure[2][4] = 13;
    	structure[3][4] = 9;
    	structure[4][4] = 1;
    	
    	List<Position> doors = new ArrayList<Position>();
    	doors.add(new Position(3,3));
    	doors.add(new Position(2,0));
    	List<Position> keys = new ArrayList<Position>();
    	keys.add(new Position(4,4));
    	keys.add(new Position(4,2));


    	Generator gen = new Generator(size,size,keys.size());
    	MazeDTO maze = gen.generateTest(size, structure, keys, doors, new Position(1,1));
    	return maze;
    }    
    
    private static MazeDTO test4(){
    	int size = 5;
    	int[][] structure = new int[size][size];
    	structure[0][0] = 4;
    	structure[1][0] = 10;
    	structure[2][0] = 6;
    	structure[3][0] = 10;
    	structure[4][0] = 2;
    	structure[0][1] = 2;
    	structure[1][1] = 5;
    	structure[2][1] = 9;
    	structure[3][1] = 3;
    	structure[4][1] = 3;
    	structure[0][2] = 7;
    	structure[1][2] = 8;
    	structure[2][2] = 6;
    	structure[3][2] = 9;
    	structure[4][2] = 3;
    	structure[0][3] = 7;
    	structure[1][3] = 12;
    	structure[2][3] = 9;
    	structure[3][3] = 4;
    	structure[4][3] = 11;
    	structure[0][4] = 5;
    	structure[1][4] = 12;
    	structure[2][4] = 12;
    	structure[3][4] = 12;
    	structure[4][4] = 9;
    	
    	List<Position> doors = new ArrayList<Position>();
    	doors.add(new Position(4,3));
    	doors.add(new Position(1,4));
    	List<Position> keys = new ArrayList<Position>();
    	keys.add(new Position(0,0));
    	keys.add(new Position(0,3));


    	Generator gen = new Generator(size,size,keys.size());
    	MazeDTO maze = gen.generateTest(size, structure, keys, doors, new Position(2,3));
    	return maze;
    }

    private static MazeDTO test5(){
    	int size = 5;
    	int[][] structure = new int[size][size];
    	structure[0][0] = 4;
    	structure[1][0] = 12;
    	structure[2][0] = 10;
    	structure[3][0] = 4;
    	structure[4][0] = 10;
    	structure[0][1] = 6;
    	structure[1][1] = 8;
    	structure[2][1] = 5;
    	structure[3][1] = 12;
    	structure[4][1] = 11;
    	structure[0][2] = 3;
    	structure[1][2] = 6;
    	structure[2][2] = 12;
    	structure[3][2] = 12;
    	structure[4][2] = 9;
    	structure[0][3] = 7;
    	structure[1][3] = 9;
    	structure[2][3] = 4;
    	structure[3][3] = 14;
    	structure[4][3] = 10;
    	structure[0][4] = 5;
    	structure[1][4] = 12;
    	structure[2][4] = 12;
    	structure[3][4] = 9;
    	structure[4][4] = 1;
    	
    	List<Position> doors = new ArrayList<Position>();
    	doors.add(new Position(2,3));
    	doors.add(new Position(0,2));
    	List<Position> keys = new ArrayList<Position>();
    	keys.add(new Position(3,0));
    	keys.add(new Position(3,2));


    	Generator gen = new Generator(size,size,keys.size());
    	MazeDTO maze = gen.generateTest(size, structure, keys, doors, new Position(3,3));
    	return maze;
    }    
 
    private static MazeDTO test6(){
    	int size = 6;
    	int[][] structure = new int[size][size];
    	structure[0][0] = 2;
    	structure[1][0] = 2;
    	structure[2][0] = 6;
    	structure[3][0] = 12;
    	structure[4][0] = 12;
    	structure[5][0] = 10;
    	
    	structure[0][1] = 3;
    	structure[1][1] = 5;
    	structure[2][1] = 9;
    	structure[3][1] = 6;
    	structure[4][1] = 10;
    	structure[5][1] = 3;
    	
    	structure[0][2] = 5;
    	structure[1][2] = 12;
    	structure[2][2] = 12;
    	structure[3][2] = 9;
    	structure[4][2] = 5;
    	structure[5][2] = 11;
    	
    	structure[0][3] = 2;
    	structure[1][3] = 6;
    	structure[2][3] = 14;
    	structure[3][3] = 8;
    	structure[4][3] = 6;
    	structure[5][3] = 9;
    	
    	structure[0][4] = 3;
    	structure[1][4] = 3;
    	structure[2][4] = 5;
    	structure[3][4] = 10;
    	structure[4][4] = 5;
    	structure[5][4] = 10;
    	
    	structure[0][5] = 5;
    	structure[1][5] = 13;
    	structure[2][5] = 8;
    	structure[3][5] = 5;
    	structure[4][5] = 12;
    	structure[5][5] = 9;
    	
    	List<Position> doors = new ArrayList<Position>();
    	doors.add(new Position(5,4));
    	doors.add(new Position(4,1));
    	doors.add(new Position(4,0));
    	List<Position> keys = new ArrayList<Position>();
    	keys.add(new Position(0,1));
    	keys.add(new Position(2,5));
    	keys.add(new Position(2,4));


    	Generator gen = new Generator(size,size,keys.size());
    	MazeDTO maze = gen.generateTest(size, structure, keys, doors, new Position(2,1));
    	return maze;
    }    

    
    /**
     * MAIN, for testing and dev purposes
     * @param args
     * @throws Exception 
     */
    public static void main(String args[]) throws Exception {
    	
    	long startTime = System.currentTimeMillis();

    	
    	for (int j=0; j< 100; j++){
    		MazeDTO maze = null;
    		while (maze == null){
    			try{
    				Generator gen = new Generator(7,7,3);
    				maze = gen.generate();
    		    	if (maze.getNbdoor()>maze.getNbKey()) maze=null;
    			}catch (Exception e){
    				continue;
    			}
    		}
	    	//maze = test6();
	        GoalDTO goals = new GoalLoadImpl(10, 20, 30, 400, 1);
	        System.out.print("discover :" + goals.getLoadDiscoverPath() + "\n");
	        System.out.print("key :" + goals.getLoadGrabKey() + "\n");
	        System.out.print("door :" + goals.getLoadOpenDoor() + "\n");
	        System.out.print("goal :" + goals.getLoadReachGoal() + "\n\n");

			for (int k = 0; k<100;k++) {

				SolverImpl s = new SolverImpl(maze, goals);

				int i = 0;
				int n = 0;
				while (s.isSolved() == false && i < 100) {
					i++;
					n += s.doOneStep();

					long endTime = System.currentTimeMillis();
					long totalTime_m = endTime - startTime;
					if (totalTime_m > 1000000) break;
				}
				if (s.isSolved()) {
					long endTime = System.currentTimeMillis();
					long totalTime_m = endTime - startTime;
					System.out.print("\nYOUHOUOU ! in " + i + " big steps and " + n + " little steps (" + totalTime_m + "ms)\n");
				}
			}
		}

        long endTime = System.currentTimeMillis();
    	long totalTime_m = endTime - startTime;
    	long totalTime_s = totalTime_m / 1000;
    	System.out.println("\nTotalTime : "+totalTime_m+" ms ("+totalTime_s+"sec) ");
    }
    
}