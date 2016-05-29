package ai.maze;

import bizz.BizzFactory;
import exception.MazeException;
import exception.MazeLevelException;
import ucc.MazeDTO;
import ucc.NodeDTO;
import ucc.NodeUCC;
import util.Position;
import util.Section;

import java.util.*;

import constant.NodeCondition;

/**
 * Created by jvdur on 12/05/2016.
 */
public class Generator {


    private static final int SECTION_MIN_VALID_SIZE = 3;

    private int sizex;
    private int sizey;
    private int level;

    private MazeGen mazeGen;
    private int[][] intStructure;
    private NodeDTO[][] nodeStructure;
    private Position mazeStartPosition;
    private NodeDTO mazeStartNode;

    private int[][][] mazeMultiData;
    private static int MAZE_MULTI_DATA_THIRD_DIM_DEPTH = 5;
    // Dimension 1 : position x
    // Dimension 2 : position y
    // Dimension 3 : {
    //  [][][0] dijkstra distance from source ,
    //  [][][1] is on solving path = 1 ,
    //  [][][2] section number,
    //  [][][3] doors,
    //  [][][4] keys,
    // }
    private Position mazeGoalPosition;
    private NodeDTO mazeGoalNode;

    private Section startSection;
    private List<Section> sections;
    private List<Section> validSection;
    private List<Section> validSectionByLength;
    private List<Section> validSectionById;

    private Section[] doorSection;
    private Section[] keySection;


    /**
     * Constructor : set a new maze-generator object
     * @param sizex the x axis length of the maze
     * @param sizey
     * @param level
     */
    public Generator(int sizex, int sizey, int level) {
        this.sizex = sizex;
        this.sizey = sizey;
        this.level = level;
    }

    /**
     * Generate and return the maze
     * @return a generated MazeDTO
     */
    public MazeDTO generate() {

        // Build maze
        mazeGen = new MazeGen(sizex, sizey);

        // Display maze
        mazeGen.display();
        // Display maze int structure
        mazeGen.displayNumbers();

        intStructure = mazeGen.getMaze();
        nodeStructure = new NodeDTO[sizex][sizey];

        // Build graph
        // - Fill up with empty nodes.
        fillWithEmptyNodes();
        // - Create link link between nodes
        createLinks();

        // Define start and goal
        // - Define start position (default at 0:0)
        defineStartPosition();
        // - Define goal position (default, the furthest away from start position
        defineGoalPosition();

        // Display dijkstraValues
//        displayDijkstra();

        // # Set keys and doors
        // - Analyse Key-Door amount
        makeBestWay();
//        displayBestWay();

        // Cut maze in sections
        makeSections();
//        displaySection();

        // Check that the maze can handle the level of doors and keys
        validSection = new ArrayList<>();
        sections = new ArrayList<>();
        if (!checkNbValidSection()) {
            throw new MazeLevelException("Cannot generate maze, level is too high");
        }

        // Order valid sections by length and id
        sortValidSections();

        // Select the sections that could handle a door
        selectAndPositionDoors();

        selectAndPositionKeys();

        displayDoors();
        displayKeys();
        displayStartEnd();


        return getMaze();
    }

    
    /*
     * generate a preconceived maze for tests
     */
    public MazeDTO generateTest(int size, int[][]struct, List<Position> keys, List<Position> doors, Position start) {
    	sizex = size;
    	sizey = size;
    	intStructure = struct;
    	
    	MazeGen maze = new MazeGen(sizex, sizey, intStructure);
    	maze.display();
    	
        nodeStructure = new NodeDTO[sizex][sizey];

        // Build graph
        // - Fill up with empty nodes.
        fillWithEmptyNodes();
        // - Create link link between nodes
        createLinks();
        
        // Define start and goal
        mazeStartPosition = start;
        mazeStartNode = nodeStructure[mazeStartPosition.getX()][mazeStartPosition.getY()];
        // - Define goal position (default, the furthest away from start position
        defineGoalPosition();
        
        // # Set keys and doors
        for (int i=0; i < doors.size(); i++){
        	nodeStructure[doors.get(i).getX()][doors.get(i).getY()].setIsDoor(i+1);
        	nodeStructure[doors.get(i).getX()][doors.get(i).getY()].setCondition(NodeCondition.NEED_KEY);
        	mazeMultiData[doors.get(i).getX()][doors.get(i).getY()][3] = i+1;
        }
        for (int i=0; i < keys.size(); i++){
        	nodeStructure[keys.get(i).getX()][keys.get(i).getY()].setHasKey(i+1);
        	mazeMultiData[keys.get(i).getX()][keys.get(i).getY()][4] = i+1;
        }
        
        displayDoors();
        displayKeys();

        return getMaze();
    }


    private MazeDTO  getMaze() {

        int[][] mazeKeys = new int[sizex][sizey];
        int[][] mazeDoors = new int[sizex][sizey];

        Map<Integer, NodeDTO> doorPosition = new HashMap<>();
        Map<Integer, NodeDTO> keyPosition = new HashMap<>();

        for(int i=0; i<sizex; i++) {
            for(int j=0; j<sizey; j++) {
                mazeKeys[i][j] = mazeMultiData[i][j][4];
                mazeDoors[i][j] = mazeMultiData[i][j][3];

                if (mazeMultiData[i][j][3] != 0) {
                    doorPosition.put(mazeMultiData[i][j][3], nodeStructure[i][j]);
                }

                if (mazeMultiData[i][j][4] != 0) {
                    keyPosition.put(mazeMultiData[i][j][4], nodeStructure[i][j]);
                }
            }
        }

        MazeDTO newMaze = BizzFactory.INSTANCE.createMaze(mazeStartNode, sizex, sizey, intStructure, mazeKeys, mazeDoors);

        newMaze.setDoorPosition(doorPosition);
        newMaze.setNbdoor(doorPosition.size());

        newMaze.setKeyPosition(keyPosition);
        newMaze.setNbKey(keyPosition.size());

        newMaze.setGoalNode(mazeGoalNode);

        return newMaze;
    }



    /**
     * Fill up with empty nodes.
     */
    private void fillWithEmptyNodes() {
        for (int i=0; i<sizex; i++) {
            for (int j=0; j<sizey; j++) {
                nodeStructure[i][j] = BizzFactory.INSTANCE.createNode(i,j);
            }
        }
    }

    /**
     * Create link link between nodes
     */
    private void createLinks() {

        for (int j=0; j<sizex; j++) {
            for (int i=0; i<sizey; i++) {

                int val = intStructure[j][i];

                // Add left neighbour
                if (val > 8) {
                    NodeUCC.INSTANCE.addNeighbour(nodeStructure[i][j],nodeStructure[i][j-1]);
                }
                // Add top neighbour
                if ((val%2) != 0) {
                    NodeUCC.INSTANCE.addNeighbour(nodeStructure[i][j], nodeStructure[i-1][j]);
                }
                // Add bottom neighbour
                if ((val-2)%4==0 || (val-3)%4==0) {
                    NodeUCC.INSTANCE.addNeighbour(nodeStructure[i][j], nodeStructure[i+1][j]);
                }
                // Add right neighbour
                if ((val >= 4 && val <= 7) || (val >= 12)) {
                    NodeUCC.INSTANCE.addNeighbour(nodeStructure[i][j], nodeStructure[i][j+1]);
                }
            }
        }
    }

    /**
     * Define start position (default at 0:0)
     */
    private void defineStartPosition() {
        Random rdm = new Random();
        mazeStartPosition = new Position(rdm.nextInt(sizex-1),rdm.nextInt(sizey-1));
        mazeStartNode = nodeStructure[mazeStartPosition.getX()][mazeStartPosition.getY()];
    }

    /**
     * Define goal position (default, the furthest away from start position
     */
    private void defineGoalPosition() {

        mazeMultiData = new int[sizex][sizey][MAZE_MULTI_DATA_THIRD_DIM_DEPTH];
        int maxLength = 0;

        int[] maxPath = getMaxDistanceFrom(null, mazeStartNode, 0);

        mazeGoalPosition = new Position(maxPath[0],maxPath[1]);
        mazeGoalNode = nodeStructure[maxPath[0]][maxPath[1]];
        mazeGoalNode.setGoal();
    }

    /**
     * Dijkstra algo to determine the longest path.
     * @param pastNode the previous visited node (null to start)
     * @param node the node from where to check
     * @param i the initial value
     * @return an array with the furthest position {posx ,posy, #hops to get there}
     */
    private int[] getMaxDistanceFrom(NodeDTO pastNode, NodeDTO node, int i) {

        // int[] path = {x,y,maxPathVal}

        int[] maxPath = {node.getPosx(), node.getPosy(), i};

        // If node not yet visited (=0) or best value
        if ( mazeMultiData[node.getPosx()][node.getPosy()][0] == 0
                || mazeMultiData[node.getPosx()][node.getPosy()][0] > i+1) {

            // Save new best value
            mazeMultiData[node.getPosx()][node.getPosy()][0] = i + 1;

            // Propagate to neighbours
            for (NodeDTO neighbour : node.getNeighbours()) {
                // except the way we came
                if (neighbour != pastNode) {

                    int[] path = getMaxDistanceFrom(node, neighbour, i+1);

                    // Only save longest path
                    if (path[2] > maxPath[2]) {
                        maxPath = path;
                    }
                }

            }

        }
        return maxPath;
    }


    /**
     * Draw best path
     */
    private void makeBestWay() {
        printPath(mazeGoalNode);
    }

    /**
     * Start from the goal and print best path on mazeMultiData[x][y][1]
     * @param node
     */
    private void printPath(NodeDTO node) {

        // print my step
        mazeMultiData[node.getPosx()][node.getPosy()][1] = 1;

        int actualDistance = mazeMultiData[node.getPosx()][node.getPosy()][0];
        for (NodeDTO neighbour : node.getNeighbours()) {
            if(mazeMultiData[neighbour.getPosx()][neighbour.getPosy()][0] < actualDistance) {
                printPath(neighbour);
            }
        }
    }


    /**
     * Devide the maze into sections (one section between two way-splits)
     */
    private void makeSections() {

        int nextId = 1;
        startSection = new Section(nextId,true,null,mazeStartNode);
        startSection.setLength(1);
        mazeMultiData[mazeStartNode.getPosx()][mazeStartNode.getPosy()][2] = nextId;

        // First the nodes that are not one the best way
        NodeDTO nodeOnbestWay = null;
        for (NodeDTO node : mazeStartNode.getNeighbours()) {
            if (mazeMultiData[node.getPosx()][node.getPosy()][1] == 1) {
                nodeOnbestWay = node;
            } else {
                nextId++;
                Section son = buildSections(startSection, node, nextId);
                startSection.addSon(son);
                nextId = son.getId();
            }
        }

        // Then eventually the node on the best way
        if (nodeOnbestWay != null) {
            Section son = buildSections(startSection, nodeOnbestWay, nextId+1);
            startSection.addSon(son);
        }

    }

    private Section buildSections(Section fatherSection, NodeDTO sectionStartNode, int sectionId) {

        int sectionLength = 0;
        boolean onBW = mazeMultiData[sectionStartNode.getPosx()][sectionStartNode.getPosy()][1] == 1 ? true : false;

        // Create new section
        Section newSection = new Section(sectionId, onBW, fatherSection, sectionStartNode);
        mazeMultiData[sectionStartNode.getPosx()][sectionStartNode.getPosy()][2] = sectionId;

        NodeDTO prevNode = null;
        NodeDTO walkerNode = sectionStartNode;
        NodeDTO tmpNode;

        // Go through the section
        while(walkerNode.getNeighbours().size() == 2) {
            tmpNode = walkerNode;

            for (NodeDTO neighbour : tmpNode.getNeighbours()) {
                if (neighbour != prevNode && mazeMultiData[neighbour.getPosx()][neighbour.getPosy()][2] == 0) {
                    walkerNode = neighbour;
                    prevNode = tmpNode;
                    sectionLength++;
                    mazeMultiData[walkerNode.getPosx()][walkerNode.getPosy()][2] = sectionId;
                }
            }
        }

        // If deadEnd
        if (walkerNode.getNeighbours().size() == 1) {
            newSection.setDeadEndNode(walkerNode);
            newSection.setLength(sectionLength+1);
            return newSection;
        }

        // If crossroad
        if (walkerNode.getNeighbours().size() > 2) {

            int nextId = sectionId;
            NodeDTO nodeOnbestWay = null;
            newSection.setLength(sectionLength+1);

            // First nodes that are not on the best way
            for (NodeDTO node : walkerNode.getNeighbours()) {

                // If backward path, continue
                if (mazeMultiData[node.getPosx()][node.getPosy()][2] != 0) {
                    continue;
                }

                if (mazeMultiData[node.getPosx()][node.getPosy()][1] == 1) {
                    nodeOnbestWay = node;
                } else {
                    nextId++;
                    Section son = buildSections(newSection, node, nextId);
                    newSection.addSon(son);
                    nextId = son.getId();
                }
            }

            // Then eventually the node on the best way
            if (nodeOnbestWay != null) {
                Section son = buildSections(newSection, nodeOnbestWay, nextId+1);
                newSection.addSon(son);
            }

            return newSection;

        }

        return newSection;

    }


    /**
     * Look where to position the doors.
     * The door key must always be on a section with an id < than the door section id.
     */
    private void selectAndPositionDoors() {

        int nbSectionLeft = validSectionById.size();

        doorSection = new Section[level];


        int doorsLeft = level;
        int keyLeft = level;

        // Last door before to reach the goal
        for (int i=validSectionById.size()-1; i>=1 ; i--) {
            if (validSectionById.get(i).isOnPath()) {
                doorSection[doorsLeft-1] = validSectionById.get(i);

                //positionDoorInSection(doorSection[doorsLeft-1], level);

                nbSectionLeft = i;
                doorsLeft--;
                i=0;
            }
        }

        if (doorsLeft == 0)
            return;

        Random rand = new Random();
        int doorGap = (int) Math.floor(nbSectionLeft/doorsLeft);

        // Decide which section has a door
        for (int i= Math.min(nbSectionLeft,doorsLeft); i>0 ; i--) {
            int rdmSectionId = doorGap*(i-1) + rand.nextInt(Math.max(1,(doorGap-1)))+1;
            doorSection[i-1] = validSectionById.get(rdmSectionId);
        }

        // Set door position in sections
        for (int i=0; i<=level-1;i++) {
            doorSection[i].setFreeOfEvent(false);
            positionDoorInSection(doorSection[i], i+1);
        }


    }

    private void positionDoorInSection(Section section, int doorLevel) {

        NodeDTO walker = section.getStartNode();
        NodeDTO nextStep = null;

        int nbMoves = (int) Math.floor(section.getLength()/2);

        for (int j=0; j<nbMoves; j++) {
            for (NodeDTO node : walker.getNeighbours()) {
                if (mazeMultiData[node.getPosx()][node.getPosy()][0] >
                        mazeMultiData[walker.getPosx()][walker.getPosy()][0]) {
                    nextStep = node;
                }
            }
            walker = nextStep;
        }

        nextStep.setIsDoor(doorLevel);
        nextStep.setCondition(NodeCondition.NEED_KEY);
        mazeMultiData[nextStep.getPosx()][nextStep.getPosy()][3] = doorLevel;
    }

    /**
     * Place all keys before doors
     */
    private void selectAndPositionKeys() {

        keySection = new Section[level];
        int keyLeft = level;
        Random rand = new Random();

        for(int i=0; i<level; i++) {

            if (doorSection[i] == null) {
                continue;
            }

            // If door has been places
            int maxSection = doorSection[i].getId()-1;

            // Decide which section has to put the key
            int rdmSectionId = rand.nextInt(maxSection-1)+1;

            keySection[i] = sections.get(rdmSectionId);

        }

        // Set keys in sections
        for (int i=0; i<level; i++) {
            if (keySection[i] == null) {
                continue;
            }
            keySection[i].setFreeOfEvent(false);
            positionKeyInSection(keySection[i], i+1);
        }

    }

    private void positionKeyInSection(Section section, int i) {

        NodeDTO walker = section.getStartNode();
        NodeDTO nextStep;
        NodeDTO prevStep = null;

        do {
            nextStep = null;
            for (NodeDTO node : walker.getNeighbours()) {
                if (mazeMultiData[node.getPosx()][node.getPosy()][0] >
                        mazeMultiData[walker.getPosx()][walker.getPosy()][0]) {
                    nextStep = node;
                }
            }

            if (nextStep == null
                    || mazeMultiData[nextStep.getPosx()][nextStep.getPosy()][2] != section.getId()
                    || mazeMultiData[nextStep.getPosx()][nextStep.getPosy()][4] != 0) {
                break;
            }

            if (walker.getIsDoor() > 0 && (nextStep == null || mazeMultiData[nextStep.getPosx()][nextStep.getPosy()][2] != section.getId())) {
                if (prevStep != null && mazeMultiData[nextStep.getPosx()][nextStep.getPosy()][4] == 0)
                   walker = prevStep;
                else
                    if (section.getStartNode() != null && mazeMultiData[section.getStartNode().getPosx()][section.getStartNode().getPosy()][4] == 0
                            && section.getStartNode().getIsDoor() == 0)
                        walker = section.getStartNode();
                    else
                        throw new MazeException("Could not build the maze, not enough place for key.");
                break;
            }

            prevStep = walker;
            walker = nextStep;
        } while(mazeMultiData[nextStep.getPosx()][nextStep.getPosy()][2] == section.getId());

        mazeMultiData[walker.getPosx()][walker.getPosy()][4] = i;
        nodeStructure[walker.getPosx()][walker.getPosy()].setHasKey(i);

    }

    /**
     * Check that the maze can handle the level of doors and keys
     * @return
     */
    private boolean checkNbValidSection() {
        int nbValidSons = getSectionOfValidSize(startSection);
        System.out.println(": valid sons = " + nbValidSons + "\n");
        return nbValidSons >= level;
    }

    private int getSectionOfValidSize(Section section) {
        int nbValidSons = 0;

        sections.add(section.getId()-1, section);

        if (section.getLength() >= SECTION_MIN_VALID_SIZE
                && (section.getSonSection().size() > 1 || section.isOnPath())) {
            nbValidSons++;
            validSection.add(section);
            System.out.print(section.getId() + " ");
        }

        for (Section son:section.getSonSection()) {
            nbValidSons += getSectionOfValidSize(son);
        }
        return nbValidSons;
    }
    
    private void sortValidSections() {
        validSectionByLength = new ArrayList<>(validSection);
        validSectionByLength.sort(new Comparator<Section>() {
            @Override
            public int compare(Section o1, Section o2) {
                return o1.getLength()-o2.getLength();
            }
        });

        validSectionById = new ArrayList<>(validSection);
        validSectionById.sort(new Comparator<Section>() {
            @Override
            public int compare(Section o1, Section o2) {
                return o1.getId()-o2.getId();
            }
        });
    }

    /**
     * MAIN, for testing and dev purposes
     * @param args
     */
    public static void main(String args[]) {
        Generator g = new Generator(7, 7, 3);
        g.generate();
    }

    private void displayDijkstra() {

        System.out.println("DIJKSTRA");
        for (int i = 0; i < sizex; i++) {
            for (int j = 0; j < sizey; j++) {
                System.out.print(mazeMultiData[i][j][0] + ((mazeMultiData[i][j][0] < 10) ? "  " : " "));
            }
            System.out.println("");
        }
        System.out.println("Longest path in position:" + mazeGoalPosition.getX() + " " + mazeGoalPosition.getY());
        System.out.println("");

    }

    private void displayBestWay() {
        System.out.println("BEST WAY");
        for (int i = 0; i < sizex; i++) {
            for (int j = 0; j < sizey; j++) {
                System.out.print((mazeMultiData[i][j][1] > 0) ? "#" : ".");
            }
            System.out.println("");
        }
        System.out.println("");

    }

    private void displaySection() {
        System.out.println("SECTIONS");
        for (int i = 0; i < sizex; i++) {
            for (int j = 0; j < sizey; j++) {
                System.out.print(mazeMultiData[i][j][2] + ((mazeMultiData[i][j][2] >= 10) ? " " : "  "));
            }
            System.out.println("");
        }
        System.out.println("");

    }

    private void displayDoors() {
        System.out.println("DOORS");
        for (int i = 0; i < sizex; i++) {
            for (int j = 0; j < sizey; j++) {
                System.out.print(mazeMultiData[i][j][3] + ((mazeMultiData[i][j][3] >= 10) ? " " : "  "));
            }
            System.out.println("");
        }
        System.out.println("");
    }

    private void displayKeys() {
        System.out.println("KEYS");
        for (int i = 0; i < sizex; i++) {
            for (int j = 0; j < sizey; j++) {
                System.out.print(mazeMultiData[i][j][4] + ((mazeMultiData[i][j][4] > 0) ? "-" : " "));
            }
            System.out.println("");
        }
        System.out.println("");
    }

    private void displayStartEnd() {
        System.out.println("START & END");

        System.out.println("Start : " + mazeStartPosition.getX() + ":" + mazeStartPosition.getY());
        System.out.println("Goal  : " + mazeGoalPosition.getX() + ":" + mazeGoalPosition.getY());
        System.out.println("");
    }
}
