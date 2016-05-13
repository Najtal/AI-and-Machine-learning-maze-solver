package model;

import ai.maze.Generator;
import ucc.MazeDTO;
import ucc.MazeUCC;

import java.util.Random;

/**
 * Created by jvdur on 09/05/2016.
 */
public class MazeLearningModel {

    private GlobalLearningModel glm;
    private boolean goForNextStep;
    private MazeDTO mazeOmniscient;
    private MazeDTO mazeNinja;


    /**
     * Constructor: generate maze and make it a ninja maze copy
     */
    public MazeLearningModel(GlobalLearningModel glm) {

        this.glm = glm;

        Random rdm = new Random();

        int sizex = glm.getmXmin() + rdm.nextInt(glm.getmXmax()-glm.getmXmin());
        int sizey = glm.getmYmin() + rdm.nextInt(glm.getmYmax()-glm.getmYmin());
        int level = glm.getmMinLevel() + rdm.nextInt(glm.getmMaxLevel()-glm.getmMinLevel());

        Generator gm = new Generator(sizex, sizey, level);
        mazeOmniscient = gm.generate();

        mazeNinja = MazeUCC.INSTANCE.getNinjaMazeFromOmniscientMaze(mazeOmniscient);
    }


    /*
     * GETTERS
     */
    public boolean isGoForNextStep() {
        return goForNextStep;
    }

    public MazeDTO getMazeOmniscient() {
        return mazeOmniscient;
    }

    public MazeDTO getMazeNinja() {
        return mazeNinja;
    }



    /*
     * SETTERS
     */
    public void setGoForNextStep(boolean goForNextStep) {
        this.goForNextStep = goForNextStep;
    }
}
