package ai.learner;

import ai.solver.Solver;
import ai.solver.SolverImpl;
import constant.RunningMode;
import constant.RunningStatus;
import model.GlobalLearningModel;
import model.MazeLearningModel;
import ucc.GoalDTO;
import util.Log;

/**
 * Created by jvdur on 13/05/2016.
 */
public class Learner implements Runnable, LearnerInt {

    private static GlobalLearningModel glm;
    private MazeLearningModel mlm;

    public Learner(GlobalLearningModel glmv) {
        glm = glmv;
    }

    /**
     * Run the Learner. Will LEARN_MAZE for the amount of mazes defined
     */
    @Override
    public void run() {

        // For the # of maze we have to solve
        for(int i=0; i<glm.getNbMaze(); i++) {

            // Create a new Model (with a maze)
            this.mlm = new MazeLearningModel(glm);

            // learn maze
            learnMaze();

            // TODO Analyse results from MazeLearningModel

        }

        System.out.println("Best step: " + mlm.getBestSteps());

    }

    /**
     * Will execute and solve the maze the amount of time defined
     */
    private void learnMaze(){

        // For the # of time to execute the maze
        for(int i=0; i<glm.getNbIterationPerMaze() ; i++) {

            // Get the goals
            GoalDTO goals = mlm.getNextGoalLoad();

            // Instantiate a solver
            //Solver s = new SolverImpl(mlm.getMazeNinja(), goals);
            // TODO : The solver should take a ninja maze and not a ominshient maze
            Solver s = new SolverImpl(mlm.getMazeOmniscient(), goals);

            // Learn from maze
            int nbStepsNeeded = executeMaze(s);

            System.out.println("Maze " + i + " # steps : "  + nbStepsNeeded);

            mlm.setNewBestMvmt(nbStepsNeeded);
        }


    }

    private int executeMaze(Solver s) {

        int nbSteps = 0;

        while(glm.getrStatus() == RunningStatus.RUNNING
                && (glm.getrMode() != RunningMode.STEP_BY_STEP_MANUAL
                    || mlm.isGoForNextStep())) {

            if (glm.getrMode() == RunningMode.STEP_BY_STEP_AUTO) {
                try {
                    Thread.sleep(glm.getAutoRunningSpeed());
                } catch (InterruptedException e) {
                    Log.logWarning("Maze execution core could not SLEEP in STEP_BY_STEP_AUTO");
                }
            }


            try {
                s.doOneStep();
                nbSteps++;
            } catch (Exception e) {
                Log.logSevere("Error while executing maze solver next step : " + e.getMessage());
                e.printStackTrace();
            }

            if (s.isSolved()) {
                Log.logFine("Maze solved");
                return nbSteps;
            }
        }
        return 0;
    }

    @Override
    public void pause() {
        // TODO
    }

    @Override
    public void start() {
        // TODO
    }

    @Override
    public void makeOneStep() {
        // TODO
    }

    @Override
    public void stop() {
        // TODO
    }

    public MazeLearningModel getMazeLearningModel() {
        return mlm;
    }
}
