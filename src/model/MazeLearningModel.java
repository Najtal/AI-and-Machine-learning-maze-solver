package model;

import ai.learner.ERSimpleLearner;
import ai.maze.Generator;
import ai.solver.MSSimpleSolver;
import ai.solver.Solver;
import constant.RunningMode;
import constant.RunningStatus;
import ucc.MazeDTO;
import ucc.MazeUCC;

import java.util.Random;

/**
 * Created by jvdur on 09/05/2016.
 */
public class MazeLearningModel {

    private GlobalLearningModel glm;
    private final ERSimpleLearner erSimpleLearner;
    private boolean goForNextStep;
    private MazeDTO mazeOmniscient;
    private MazeDTO mazeNinja;


    /**
     * private constructor
     */
    public MazeLearningModel(GlobalLearningModel glm, ERSimpleLearner erSimpleLearner) {

        this.glm = glm;
        this.erSimpleLearner = erSimpleLearner;

        Random rdm = new Random();

        int sizex = glm.getmXmin() + rdm.nextInt(glm.getmXmax()-glm.getmXmin());
        int sizey = glm.getmYmin() + rdm.nextInt(glm.getmYmax()-glm.getmYmin());
        int level = glm.getmMinLevel() + rdm.nextInt(glm.getmMaxLevel()-glm.getmMinLevel());

        Generator gm = new Generator(sizex, sizey, level);
        mazeOmniscient = gm.generate();

        mazeNinja = MazeUCC.INSTANCE.getNinjaMazeFromOmniscientMaze(mazeOmniscient);

    }


    public void start() {

        Solver s = new MSSimpleSolver(mazeNinja);

        while(glm.getrStatus() == RunningStatus.RUNNING
                && ( glm.getrMode() == RunningMode.FULL_SPEED
                || glm.getrMode() == RunningMode.MAZE_BY_MAZE
                || goForNextStep)) {

            // TODO : must call solver over here


            s.doOneStep();

            if (s.isSolved()) {
                return;
            }

        }

    }
}
