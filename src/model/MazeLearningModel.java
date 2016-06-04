package model;

import ai.algorithm.LAGridSearch;
import ai.algorithm.LARandomSearch;
import ai.algorithm.LearnAlgorithm;
import ai.maze.Generator;
import app.AppContext;
import bizz.GoalLoadImpl;
import ucc.GoalDTO;
import ucc.MazeDTO;
import ucc.MazeUCCImpl;

import java.util.Random;

/**
 * Created by jvdur on 09/05/2016.
 */
public class MazeLearningModel {

    private GlobalLearningModel glm;
    private boolean goForNextStep;
    private MazeDTO mazeOmniscient;
    private MazeDTO mazeNinja;

    private int nbStepsToResolveMaze;
    private GoalDTO lastGoals;

    private LearnAlgorithm algo;


    /**
     * Constructor: generate maze and make it a ninja maze copy
     */
    public MazeLearningModel(GlobalLearningModel glm) {
        this.glm = glm;
        setAlgorithm();
        generateMaze();
    }

    private void setAlgorithm() {
        switch(glm.getAlgorithm()) {
            case RANDOM_SEARCH :
                this.algo = new LARandomSearch(new GoalLoadImpl(10, 20, 30, 200, 1));
                break;
            case GRID_SEARCH :
                this.algo = new LAGridSearch();
                break;
        }
    }

    private void generateMaze() {

        int trials = 1;
        int trialsMax = Math.max((Integer.parseInt(AppContext.INSTANCE.getProperty("mazeGenTrials"))), 1);

        Random rdm = new Random();

        int sizex = Math.max(2, (glm.getmXmax()-glm.getmXmin() <= 0) ?  glm.getmXmin() : glm.getmXmin() + rdm.nextInt(glm.getmXmax()-glm.getmXmin()));
        int sizey = Math.max(2, (glm.getmYmax()-glm.getmYmin() <= 0) ? glm.getmYmin() : glm.getmYmin() + rdm.nextInt(glm.getmYmax()-glm.getmYmin()));
        int level = Math.max(0, (glm.getmMaxLevel()-glm.getmMinLevel() <= 0) ? glm.getmMinLevel() : glm.getmMinLevel() + rdm.nextInt(glm.getmMaxLevel()-glm.getmMinLevel()));

        // Init new generator
        Generator gm = new Generator(sizex, sizey, level);

        // Generate Maze
        mazeOmniscient = gm.generate();
        // Make Ninja maze out of maze
        mazeNinja = MazeUCCImpl.getNinjaMazeFromOmniscientMaze(mazeOmniscient);

        /*
        while (true) {
            try {
                mazeOmniscient = gm.generate();
                break;
            } catch (MazeException me) {
                if (trials == trialsMax) {
                    Log.logSevere("Could not create the maze, parameters are to restrictive !");
                    throw new FatalException("Could not create the maze, parameters are to restrictive !");
                } else {
                    trials++;
                }
            }
            break;
        }
        mazeNinja = MazeUCCImpl.getNinjaMazeFromOmniscientMaze(mazeOmniscient);
        */
    }


    /*
     * GETTERS
     */
    public MazeDTO getMazeOmniscient() {
        return mazeOmniscient;
    }

    public MazeDTO getMazeNinja() {
        return mazeNinja;
    }



    /*
     * SETTERS
     */
    public void setNbStepsToResolveMaze(int nbStepsToResolveMaze) {
        this.nbStepsToResolveMaze = nbStepsToResolveMaze;
    }

    public GoalDTO getNextGoalLoad() {
        return algo.getNextGoal();
    }

    public boolean isGoForNextStep() {
        return goForNextStep;
    }
}
